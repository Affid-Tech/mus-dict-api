package org.affidtech.musdict.musdictapi.service


import org.affidtech.musdict.musdictapi.domain.Address
import org.affidtech.musdict.musdictapi.domain.Location
import org.affidtech.musdict.musdictapi.repository.*
import org.affidtech.musdict.musdictapi.service.spec.LocationSpecifications
import org.affidtech.musdict.musdictapi.web.LocationMapper
import org.affidtech.musdict.musdictapi.web.dto.AddressCreate
import org.affidtech.musdict.musdictapi.web.dto.LocationCreate
import org.affidtech.musdict.musdictapi.web.dto.LocationReadDetail
import org.affidtech.musdict.musdictapi.web.dto.LocationReadSummary
import org.affidtech.musdict.musdictapi.web.dto.LocationType
import org.affidtech.musdict.musdictapi.web.dto.LocationUpdate
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal
import java.util.*


@Service
class LocationService(
	private val locationRepository: LocationRepository,
	private val addressService: AddressService,
	private val addressRepository: AddressRepository,
	private val locationMapper: LocationMapper,
// NEW: profile repos to compute types[] cheaply (like roles for users)
	private val concertVenueRepo: ConcertVenueProfileRepository,
	private val rentalRepo: RentalProfileRepository,
	private val rehearsalBaseRepo: RehearsalBaseProfileRepository,
	private val studioRepo: StudioProfileRepository
) {
	
	
	@Transactional
	fun create(dto: LocationCreate): LocationReadDetail {
		val address = resolveAddressForCreate(dto.addressId to dto.addressCreate)
		val entity = Location(
			name = dto.name,
			cover = dto.cover,
			address = address,
			description = dto.description,
			contacts = dto.contacts
		)
		val saved = locationRepository.save(entity)
		val t = typesFor(saved.id!!)
		return locationMapper.toReadDetail(saved, t)
	}
	
	
	@Transactional(readOnly = true)
	fun list(
		cityId: UUID?,
		q: String?,
		nearLat: BigDecimal?,
		nearLon: BigDecimal?,
		radiusMeters: Int?,
		page: Int,
		size: Int,
		sort: String?
	): Page<LocationReadSummary> {
		val pageable = toPageable(page, size, sort)
		val spec: Specification<Location> =
			LocationSpecifications.and(
				LocationSpecifications.cityEquals(cityId),
				LocationSpecifications.qLike(q),
				LocationSpecifications.withinDistance(nearLat, nearLon, radiusMeters)
			)
		return locationRepository.findAll(spec, pageable).map { entity ->
			val id = entity.id ?: error("Persisted Location must have id")
			val t = typesFor(id)
			locationMapper.toReadSummary(entity, t)
		}
	}
	
	
	@Transactional(readOnly = true)
	fun get(id: UUID): LocationReadDetail {
		val entity = locationRepository.findById(id).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found: $id")
		}
		val t = typesFor(id)
		return locationMapper.toReadDetail(entity, t)
	}
	
	@Transactional
	fun update(id: UUID, dto: LocationUpdate): LocationReadDetail {
		val entity = locationRepository.findById(id).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found: $id")
		}
		
		locationMapper.updateEntityFromDto(dto, entity)
		
		Pair(dto.addressId, dto.addressCreate)
			.takeIf { addressData -> addressData.first != null || addressData.second != null }
			?.let { addressData -> entity.address = resolveAddressForCreate(addressData) }
		
		
		val saved = locationRepository.save(entity)
		val t = typesFor(saved.id!!)
		return locationMapper.toReadDetail(saved, t)
	}
	
	
	@Transactional
	fun delete(id: UUID) {
		if (!locationRepository.existsById(id)) {
			throw ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found: $id")
		}
		try {
			locationRepository.deleteById(id)
		} catch (ex: DataIntegrityViolationException) {
			throw ResponseStatusException(
				HttpStatus.CONFLICT, "Location is referenced by other entities and cannot be deleted."
			)
		}
	}
	
	private fun toPageable(page: Int, size: Int, sort: String?): Pageable {
		if (sort.isNullOrBlank()) return PageRequest.of(page, size)
		val parts = sort.split(",")
		return if (parts.size == 2)
			PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(parts[1]), parts[0]))
		else
			PageRequest.of(page, size, Sort.by(sort))
	}
	
	
	private fun resolveAddressForCreate(addressData: Pair<UUID?, AddressCreate?>): Address =
		when {
			addressData.first != null -> addressRepository.findById(addressData.first!!).orElseThrow {
				ResponseStatusException(HttpStatus.BAD_REQUEST, "addressId not found: ${addressData.first}")
			}
			addressData.second != null -> addressService.create(addressData.second!!).let { addressRepository.findById(it.id).orElseThrow() }
			else -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Exactly one of addressId or addressCreate must be provided")
		}
	
	
	private fun typesFor(locationId: UUID): List<LocationType> {
		val result = mutableListOf<LocationType>()
		if (concertVenueRepo.existsByLocationId(locationId)) result += LocationType.CONCERT_VENUE
		if (rentalRepo.existsByLocationId(locationId)) result += LocationType.RENTAL
		if (rehearsalBaseRepo.existsByLocationId(locationId)) result += LocationType.REHEARSAL_BASE
		if (studioRepo.existsByLocationId(locationId)) result += LocationType.STUDIO
		return result
	}
}
