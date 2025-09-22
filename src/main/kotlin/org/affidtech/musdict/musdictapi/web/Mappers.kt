package org.affidtech.musdict.musdictapi.web

import org.affidtech.musdict.musdictapi.domain.Address
import org.affidtech.musdict.musdictapi.domain.City
import org.affidtech.musdict.musdictapi.domain.Location
import org.affidtech.musdict.musdictapi.web.dto.*
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.PrecisionModel
import org.mapstruct.*
import java.math.BigDecimal

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

@Mapper(
	componentModel = "spring",
	uses = [PointMapper::class, CityMapper::class]
)
interface AddressMapper {
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "city", ignore = true) // проставим в сервисе после загрузки City
	@Mapping(target = "coordinates", source = "coordinates", qualifiedByName = ["toPoint"])
	fun toEntity(dto: AddressCreate): Address
	
	@Mapping(target = "coordinates", source = "coordinates", qualifiedByName = ["toDto"])
	@Mapping(target = "city", source = "city", qualifiedByName = ["toReadDetail"])
	fun toReadDetail(entity: Address): AddressReadDetail
	
	@Mapping(target = "city", source = "city", qualifiedByName = ["toReadSummary"])
	fun toReadSummary(entity: Address): AddressReadSummary
	
	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "coordinates", source = "coordinates", qualifiedByName = ["toPoint"])
	@Mapping(target = "readableAddress", source = "readableAddress")
	fun updateEntityFromDto(dto: AddressUpdate, @MappingTarget entity: Address)
}

@Mapper(
	componentModel = "spring",
	uses = [AddressMapper::class, CityMapper::class, PointMapper::class]
)
interface LocationMapper {
	
	fun toReadSummary(entity: Location): LocationReadSummary
	
	fun toReadDetail(entity: Location): LocationReadDetail
	
	/**
	 * Updates only the "simple" fields of Location (name, cover, description, contacts).
	 * Address must be handled manually in the service, since it needs DB lookups.
	 */
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "address", ignore = true) // leave address handling to the service
	fun updateEntityFromDto(dto: LocationUpdate, @MappingTarget entity: Location)
}