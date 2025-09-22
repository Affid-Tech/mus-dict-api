package org.affidtech.musdict.musdictapi.service

import org.affidtech.musdict.musdictapi.domain.Address
import org.affidtech.musdict.musdictapi.domain.Location
import org.affidtech.musdict.musdictapi.repository.AddressRepository
import org.affidtech.musdict.musdictapi.repository.LocationRepository
import org.affidtech.musdict.musdictapi.service.spec.LocationSpecifications
import org.affidtech.musdict.musdictapi.web.AddressMapper
import org.affidtech.musdict.musdictapi.web.LocationMapper
import org.affidtech.musdict.musdictapi.web.dto.LocationCreate
import org.affidtech.musdict.musdictapi.web.dto.LocationReadDetail
import org.affidtech.musdict.musdictapi.web.dto.LocationReadSummary
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
	private val addressMapper: AddressMapper,
	private val locationMapper: LocationMapper
) {
	
	@Transactional
	fun create(dto: LocationCreate): LocationReadDetail {
		val address = resolveAddressForCreate(dto)
		val entity = Location(
			// id is @GeneratedValue (nullable) in your entity
			name = dto.name,
			cover = dto.cover,
			address = address,
			description = dto.description,
			contacts = dto.contacts
		)
		val saved = locationRepository.save(entity)
		return locationMapper.toReadDetail(saved)
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
		return locationRepository.findAll(spec, pageable).map(locationMapper::toReadSummary)
	}
	
	@Transactional(readOnly = true)
	fun get(id: UUID): LocationReadDetail {
		val entity = locationRepository.findById(id).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found: $id")
		}
		return locationMapper.toReadDetail(entity)
	}
	
	@Transactional
	fun update(id: UUID, dto: LocationUpdate): LocationReadDetail {
		val entity = locationRepository.findById(id).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found: $id")
		}
		
		// MapStruct handles name, cover, description, contacts
		locationMapper.updateEntityFromDto(dto, entity)
		
		// Address handling (at most one of addressId/addressCreate)
		when {
			dto.addressId != null -> {
				val addr = addressRepository.findById(dto.addressId).orElseThrow {
					ResponseStatusException(HttpStatus.BAD_REQUEST, "addressId not found: ${dto.addressId}")
				}
				entity.address = addr
			}
			dto.addressCreate != null -> {
				val newAddress = addressService.create(dto.addressCreate).let { addressRepository.findById(it.id).orElseThrow() }
				entity.address = newAddress
			}
		}
		
		val saved = locationRepository.save(entity)
		
		return locationMapper.toReadDetail(saved)
	}
	
	@Transactional
	fun delete(id: UUID) {
		if (!locationRepository.existsById(id)) {
			throw ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found: $id")
		}
		try {
			locationRepository.deleteById(id)
		} catch (ex: DataIntegrityViolationException) {
			// In case of FKs to Location
			throw ResponseStatusException(
				HttpStatus.CONFLICT,
				"Location is referenced by other entities and cannot be deleted."
			)
		}
	}
	
	// --- helpers ---
	
	private fun toPageable(page: Int, size: Int, sort: String?): Pageable {
		if (sort.isNullOrBlank()) return PageRequest.of(page, size)
		val parts = sort.split(",")
		return if (parts.size == 2)
			PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(parts[1]), parts[0]))
		else
			PageRequest.of(page, size, Sort.by(sort))
	}
	
	private fun resolveAddressForCreate(dto: LocationCreate): Address =
		when {
			dto.addressId != null -> addressRepository.findById(dto.addressId).orElseThrow {
				ResponseStatusException(HttpStatus.BAD_REQUEST, "addressId not found: ${dto.addressId}")
			}
			dto.addressCreate != null -> addressService.create(dto.addressCreate).let { addressRepository.findById(it.id).orElseThrow() }
			else -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Exactly one of addressId or addressCreate must be provided")
		}
}
