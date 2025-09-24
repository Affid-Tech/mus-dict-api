package org.affidtech.musdict.musdictapi.service

import org.affidtech.musdict.musdictapi.domain.RentalEquipment
import org.affidtech.musdict.musdictapi.domain.RentalEquipmentId
import org.affidtech.musdict.musdictapi.domain.RentalProfile
import org.affidtech.musdict.musdictapi.repository.EquipmentRepository
import org.affidtech.musdict.musdictapi.repository.RentalEquipmentRepository
import org.affidtech.musdict.musdictapi.repository.RentalProfileRepository
import org.affidtech.musdict.musdictapi.web.RentalEquipmentMapper
import org.affidtech.musdict.musdictapi.web.dto.EquipmentLinkPatchDto
import org.affidtech.musdict.musdictapi.web.dto.EquipmentLinkReadDto
import org.affidtech.musdict.musdictapi.web.dto.EquipmentLinkUpsertDto
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class RentalEquipmentService(
	private val rentalRepo: RentalProfileRepository,
	private val equipmentRepo: EquipmentRepository,
	private val linkRepo: RentalEquipmentRepository,
	private val mapper: RentalEquipmentMapper
) {
	@Transactional(readOnly = true)
	fun list(rentalId: UUID): List<EquipmentLinkReadDto> {
		ensureRentalExists(rentalId)
		return linkRepo.findAllByRental_Id(rentalId).map(mapper::toReadDto)
	}
	
	/** PUT replace collection (idempotent). Removes absent, upserts provided items. */
	@Transactional
	fun replace(rentalId: UUID, items: List<EquipmentLinkUpsertDto>): List<EquipmentLinkReadDto> {
		val rental = ensureRentalExists(rentalId)
		val ids = items.map { it.equipmentId }
		if (ids.size != ids.toSet().size) {
			throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate equipmentId in payload")
		}
		val existingEquip = equipmentRepo.findAllById(ids).toList()
		if (existingEquip.size != ids.size) {
			throw ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more equipmentId not found")
		}
		
		val current = linkRepo.findAllByRental_Id(rentalId).associateBy { it.equipment.id!! }.toMutableMap()
		
		for ((eqId, link) in current.toMap()) {
			if (!ids.contains(eqId)) {
				linkRepo.delete(link)
				current.remove(eqId)
			}
		}
		
		val result = mutableListOf<EquipmentLinkReadDto>()
		for (dto in items) {
			val eq = existingEquip.first { it.id == dto.equipmentId }
			val link = current[dto.equipmentId]?.apply { quantity = dto.quantity }
				?: RentalEquipment(
					id = RentalEquipmentId(rentalId, dto.equipmentId),
					rental = rental,
					equipment = eq,
					quantity = dto.quantity
				)
			val saved = linkRepo.save(link)
			result += mapper.toReadDto(saved)
		}
		return result.sortedBy { it.equipment?.name ?: "" }
	}
	
	@Transactional
	fun addOne(rentalId: UUID, dto: EquipmentLinkUpsertDto): EquipmentLinkReadDto {
		val rental = ensureRentalExists(rentalId)
		val eq = equipmentRepo.findById(dto.equipmentId).orElseThrow {
			ResponseStatusException(HttpStatus.BAD_REQUEST, "equipmentId not found: ${dto.equipmentId}")
		}
		if (linkRepo.existsByRental_IdAndEquipment_Id(rentalId, dto.equipmentId)) {
			throw ResponseStatusException(HttpStatus.CONFLICT, "Equipment already attached")
		}
		val link = RentalEquipment(
			id = RentalEquipmentId(rentalId, dto.equipmentId),
			rental = rental,
			equipment = eq,
			quantity = dto.quantity
		)
		return mapper.toReadDto(linkRepo.save(link))
	}
	
	@Transactional
	fun patchOne(rentalId: UUID, equipmentId: UUID, dto: EquipmentLinkPatchDto): EquipmentLinkReadDto {
		val link = linkRepo.findByRental_IdAndEquipment_Id(rentalId, equipmentId).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "Equipment not attached")
		}
		link.quantity = dto.quantity
		return mapper.toReadDto(linkRepo.save(link))
	}
	
	@Transactional
	fun removeOne(rentalId: UUID, equipmentId: UUID) {
		linkRepo.deleteByRental_IdAndEquipment_Id(rentalId, equipmentId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Equipment not attached")
	}
	
	private fun ensureRentalExists(id: UUID): RentalProfile =
		rentalRepo.findById(id).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "Rental profile not attached: $id")
		}
}
