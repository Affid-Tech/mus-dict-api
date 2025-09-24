package org.affidtech.musdict.musdictapi.web

import org.affidtech.musdict.musdictapi.domain.Address
import org.affidtech.musdict.musdictapi.domain.City
import org.affidtech.musdict.musdictapi.domain.ConcertVenueProfile
import org.affidtech.musdict.musdictapi.domain.Equipment
import org.affidtech.musdict.musdictapi.domain.Location
import org.affidtech.musdict.musdictapi.domain.RehearsalBaseProfile
import org.affidtech.musdict.musdictapi.domain.RentalProfile
import org.affidtech.musdict.musdictapi.domain.StudioProfile
import org.affidtech.musdict.musdictapi.web.dto.*
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.PrecisionModel
import org.mapstruct.*
import java.math.BigDecimal
import java.util.*


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
interface CityMapper {
	@Named("toReadDetail")
	fun toReadDetail(entity: City): CityReadDetail
	
	@Named("toReadSummary")
	fun toReadSummary(entity: City): CityReadSummary
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "id", ignore = true)
	fun update(dto: CityUpdate, @MappingTarget entity: City)
}

@Mapper(componentModel = "spring")
abstract class PointMapper {
	private val geometryFactory = GeometryFactory(PrecisionModel(), 4326)
	
	@Named("toPoint")
	fun toPoint(dto: CoordinatesDto?): Point? {
		if (dto == null) return null
		val coord = Coordinate(dto.lon.toDouble(), dto.lat.toDouble()) // x=lon, y=lat
		return geometryFactory.createPoint(coord)
	}
	
	@Named("toDto")
	fun toDto(point: Point?): CoordinatesDto? {
		if (point == null) return null
		val lat = BigDecimal.valueOf(point.y)
		val lon = BigDecimal.valueOf(point.x)
		return CoordinatesDto(lat = lat, lon = lon)
	}
}


@Mapper(componentModel = "spring", uses = [PointMapper::class, CityMapper::class])
abstract class AddressMapper {
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "city", expression = "java(city)")
	@Mapping(target = "coordinates", source = "coordinates", qualifiedByName = ["toPoint"])
	abstract fun toEntity(dto: AddressCreate, @Context city: City): Address
	
	@Mapping(target = "coordinates", source = "coordinates", qualifiedByName = ["toDto"])
	@Mapping(target = "city", source = "city", qualifiedByName = ["toReadDetail"])
	abstract fun toReadDetail(entity: Address): AddressReadDetail
	
	@Mapping(target = "city", source = "city", qualifiedByName = ["toReadSummary"])
	abstract fun toReadSummary(entity: Address): AddressReadSummary
	
	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "coordinates", source = "coordinates", qualifiedByName = ["toPoint"])
	@Mapping(target = "readableAddress", source = "readableAddress")
	@Mapping(target = "city", source = "cityId", conditionExpression = "java(city != null)")
	abstract fun updateEntityFromDto(dto: AddressUpdate, @MappingTarget entity: Address, @Context city: City?)
	
	fun provideCity(cityId: UUID, @Context city: City): City = city
}


@Mapper(componentModel = "spring", uses = [AddressMapper::class, CityMapper::class, PointMapper::class])
interface LocationMapper {
	
	@Mapping(target = "types", expression = "java(types)")
	fun toReadSummary(entity: Location, @Context types: List<LocationType>): LocationReadSummary
	
	@Mapping(target = "types", expression = "java(types)")
	fun toReadDetail(entity: Location, @Context types: List<LocationType>): LocationReadDetail
	
	/**
	 * Updates only the simple fields (name, cover, description, contacts).
	 * Address is handled in the service.
	 */
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "address", ignore = true)
	fun updateEntityFromDto(dto: LocationUpdate, @MappingTarget entity: Location)
}

@Mapper(componentModel = "spring")
interface ConcertVenueProfileMapper {
	
	fun toDto(entity: ConcertVenueProfile): ConcertVenueProfileDto
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	fun updateFromPatch(dto: ConcertVenueProfilePatchDto, @MappingTarget entity: ConcertVenueProfile)
}

@Mapper(componentModel = "spring")
interface RentalProfileMapper {
	fun toDto(entity: RentalProfile): RentalProfileDto
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	fun updateFromPatch(dto: RentalProfilePatchDto, @MappingTarget entity: RentalProfile)
}

@Mapper(componentModel = "spring")
interface RehearsalBaseProfileMapper {
	fun toDto(entity: RehearsalBaseProfile): RehearsalBaseProfileDto
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	fun updateFromPatch(dto: RehearsalBaseProfilePatchDto, @MappingTarget entity: RehearsalBaseProfile)
}

@Mapper(componentModel = "spring")
interface StudioProfileMapper {
	fun toDto(entity: StudioProfile): StudioProfileDto
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	fun updateFromPatch(dto: StudioProfilePatchDto, @MappingTarget entity: StudioProfile)
}

@Mapper(componentModel = "spring")
interface EquipmentMapper {
	
	fun toReadDto(entity: Equipment): EquipmentReadDto
	
	fun toEntity(dto: EquipmentCreateDto): Equipment
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	fun updateFromPatch(dto: EquipmentPatchDto, @MappingTarget entity: Equipment)
}