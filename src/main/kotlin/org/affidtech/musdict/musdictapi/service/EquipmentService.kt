package org.affidtech.musdict.musdictapi.service

import org.affidtech.musdict.musdictapi.repository.EquipmentRepository
import org.affidtech.musdict.musdictapi.web.EquipmentMapper
import org.affidtech.musdict.musdictapi.web.dto.EquipmentCreateDto
import org.affidtech.musdict.musdictapi.web.dto.EquipmentPatchDto
import org.affidtech.musdict.musdictapi.web.dto.EquipmentReadDto
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class EquipmentService(
	private val repository: EquipmentRepository,
	private val mapper: EquipmentMapper
) {
	
	@Transactional
	fun create(dto: EquipmentCreateDto): EquipmentReadDto {
		if (repository.existsByNameIgnoreCase(dto.name)) {
			throw ResponseStatusException(HttpStatus.CONFLICT, "Equipment with name '${dto.name}' already exists")
		}
		val saved = repository.save(mapper.toEntity(dto))
		return mapper.toReadDto(saved)
	}
	
	@Transactional(readOnly = true)
	fun list(q: String?, page: Int, size: Int, sort: String?): Page<EquipmentReadDto> {
		val pageable = toPageable(page, size, sort)
		val pageEntities = if (!q.isNullOrBlank()) {
			repository.findByNameContainingIgnoreCase(q, pageable)
		} else {
			repository.findAll(pageable)
		}
		return pageEntities.map(mapper::toReadDto)
	}
	
	@Transactional(readOnly = true)
	fun get(id: UUID): EquipmentReadDto {
		val entity = repository.findById(id).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "Equipment not found: $id")
		}
		return mapper.toReadDto(entity)
	}
	
	@Transactional
	fun patch(id: UUID, dto: EquipmentPatchDto): EquipmentReadDto {
		val entity = repository.findById(id).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "Equipment not found: $id")
		}
		// if name is changing, check for conflicts
		dto.name?.let { newName ->
			if (!newName.equals(entity.name, ignoreCase = true) &&
				repository.existsByNameIgnoreCase(newName)
			) {
				throw ResponseStatusException(HttpStatus.CONFLICT, "Equipment with name '$newName' already exists")
			}
		}
		mapper.updateFromPatch(dto, entity)
		val saved = repository.save(entity)
		return mapper.toReadDto(saved)
	}
	
	@Transactional
	fun delete(id: UUID) {
		val entity = repository.findById(id).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "Equipment not found: $id")
		}
		try {
			repository.delete(entity)
		} catch (ex: DataIntegrityViolationException) {
			// Prevent deleting if referenced by concert_venue_equipment or rental_equipment
			throw ResponseStatusException(
				HttpStatus.CONFLICT,
				"Equipment is referenced by locations/profiles and cannot be deleted"
			)
		}
	}
	
	// ---- helpers ----
	
	private fun toPageable(page: Int, size: Int, sort: String?): Pageable {
		if (sort.isNullOrBlank()) return PageRequest.of(page, size)
		val parts = sort.split(",")
		return if (parts.size == 2)
			PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(parts[1]), parts[0]))
		else
			PageRequest.of(page, size, Sort.by(sort))
	}
}
