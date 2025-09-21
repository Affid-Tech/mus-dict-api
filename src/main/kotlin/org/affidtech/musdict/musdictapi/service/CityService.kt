package org.affidtech.musdict.musdictapi.service

import org.affidtech.musdict.musdictapi.domain.City
import org.affidtech.musdict.musdictapi.repository.CityRepository
import org.affidtech.musdict.musdictapi.web.CityMapper
import org.affidtech.musdict.musdictapi.web.dto.CityCreate
import org.affidtech.musdict.musdictapi.web.dto.CityReadDetail
import org.affidtech.musdict.musdictapi.web.dto.CityReadSummary
import org.affidtech.musdict.musdictapi.web.dto.CityUpdate
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

private const val CITY_NOT_FOUND = "City not found"

@Service
@Transactional
class CityService(
	private val repo: CityRepository,
	private val mapper: CityMapper, // MapStruct mapper (to be generated later)
) {
	
	fun create(dto: CityCreate): CityReadDetail {
		// Optional uniqueness guard (service-level); drop if DB constraint is present
		if (repo.existsByNameIgnoreCase(dto.name)) {
			throw ResponseStatusException(HttpStatus.CONFLICT, "City with this name already exists")
		}
		
		// We construct the entity here to control ID generation,
		// then let MapStruct handle the DTO mapping.
		val entity = City(
			name = dto.name.trim()
		)
		
		val saved = repo.save(entity)
		return mapper.toReadDetail(saved)
	}
	
	@Transactional(readOnly = true)
	fun list(page: Int, size: Int, q: String?): Page<CityReadSummary> {
		val pageable = PageRequest.of(page.coerceAtLeast(0), size.coerceAtLeast(1))
		
		val cities = if (q.isNullOrBlank()) {
			repo.findAll(pageable)
		} else {
			repo.findAllByNameContainingIgnoreCase(q.trim(), pageable)
		}
		
		// MapStruct mapping per item
		return cities.map { mapper.toReadSummary(it) }
	}
	
	@Transactional(readOnly = true)
	fun get(id: UUID): CityReadDetail {
		val city = repo.findById(id).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, CITY_NOT_FOUND)
		}
		return mapper.toReadDetail(city)
	}
	
	fun update(id: UUID, dto: CityUpdate): CityReadDetail {
		val city = repo.findById(id).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, CITY_NOT_FOUND)
		}
		
		mapper.update(dto, city)
		
		val saved = repo.save(city)
		return mapper.toReadDetail(saved)
	}
	
	fun delete(id: UUID) {
		try {
			repo.deleteById(id)
		} catch (ex: EmptyResultDataAccessException) {
			// Idempotent-ish delete: surface 404 if preferred
			throw ResponseStatusException(HttpStatus.NOT_FOUND, CITY_NOT_FOUND)
		}
	}
}
