package org.affidtech.musdict.musdictapi.service

import org.affidtech.musdict.musdictapi.repository.AddressRepository
import org.affidtech.musdict.musdictapi.repository.CityRepository
import org.affidtech.musdict.musdictapi.service.spec.AddressSpecifications
import org.affidtech.musdict.musdictapi.web.AddressMapper
import org.affidtech.musdict.musdictapi.web.dto.AddressCreate
import org.affidtech.musdict.musdictapi.web.dto.AddressReadDetail
import org.affidtech.musdict.musdictapi.web.dto.AddressReadSummary
import org.affidtech.musdict.musdictapi.web.dto.AddressUpdate
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
class AddressService(
	private val addressRepository: AddressRepository,
	private val cityRepository: CityRepository,
	private val mapper: AddressMapper
) {
	
	@Transactional
	fun create(dto: AddressCreate): AddressReadDetail {
		val city = dto.cityId?.let { id ->
			cityRepository.findById(id).orElseThrow {
				ResponseStatusException(HttpStatus.BAD_REQUEST, "cityId not found: $id")
			}
		}
		val entity = mapper.toEntity(dto).also {
			it.city = city
		}
		val saved = addressRepository.save(entity)
		return mapper.toReadDetail(saved)
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
	): Page<AddressReadSummary> {
		val pageable = toPageable(page, size, sort)
		val spec: Specification<org.affidtech.musdict.musdictapi.domain.Address> =
			AddressSpecifications.and(
				AddressSpecifications.cityEquals(cityId),
				AddressSpecifications.qLike(q),
				AddressSpecifications.withinDistance(nearLat, nearLon, radiusMeters)
			)
		return addressRepository.findAll(spec, pageable).map { mapper.toReadSummary(it) }
	}
	
	@Transactional(readOnly = true)
	fun get(id: UUID): AddressReadDetail {
		val entity = addressRepository.findById(id).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found: $id")
		}
		return mapper.toReadDetail(entity)
	}
	
	/**
	 * Updates an existing Address:
	 *  - load the managed entity from the database
	 *  - optionally update its City
	 *  - use MapStruct to update mutable fields (coordinates, readableAddress)
	 */
	@Transactional
	fun update(id: UUID, dto: AddressUpdate): AddressReadDetail {
		val entity = addressRepository.findById(id).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found: $id")
		}
		
		val city = dto.cityId?.let { cityId ->
			cityRepository.findById(cityId).orElseThrow {
				ResponseStatusException(HttpStatus.BAD_REQUEST, "cityId not found: $cityId")
			}
		}
		
		mapper.updateEntityFromDto(dto, entity)
		entity.city = city ?: entity.city
		
		val saved = addressRepository.save(entity)
		
		return mapper.toReadDetail(saved)
	}
	
	@Transactional
	fun delete(id: UUID) {
		if (!addressRepository.existsById(id)) {
			throw ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found: $id")
		}
		try {
			addressRepository.deleteById(id)
		} catch (ex: DataIntegrityViolationException) {
			throw ResponseStatusException(
				HttpStatus.CONFLICT,
				"Address is referenced by other entities and cannot be deleted."
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
}
