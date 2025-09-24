package org.affidtech.musdict.musdictapi.service

import org.affidtech.musdict.musdictapi.domain.*
import org.affidtech.musdict.musdictapi.repository.*
import org.affidtech.musdict.musdictapi.web.ConcertVenueEquipmentMapper
import org.affidtech.musdict.musdictapi.web.dto.*
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class ConcertVenueEquipmentService(
	private val concertVenueRepo: ConcertVenueProfileRepository,
	private val equipmentRepo: EquipmentRepository,
	private val linkRepo: ConcertVenueEquipmentRepository,
	private val mapper: ConcertVenueEquipmentMapper
) {
	@Transactional(readOnly = true)
	fun list(venueId: UUID): List<EquipmentLinkReadDto> {
		ensureVenueExists(venueId)
		return linkRepo.findAllByConcertVenue_Id(venueId).map(mapper::toReadDto)
	}
	
	/** PUT replace collection (idempotent). Removes absent, upserts provided items. */
	@Transactional
	fun replace(venueId: UUID, items: List<EquipmentLinkUpsertDto>): List<EquipmentLinkReadDto> {
		val venue = ensureVenueExists(venueId)
		// sanity: no duplicate equipmentIds
		val ids = items.map { it.equipmentId }
		if (ids.size != ids.toSet().size) {
			throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate equipmentId in payload")
		}
		// validate all equipment exist
		val existingEquip = equipmentRepo.findAllById(ids).toList()
		if (existingEquip.size != ids.size) {
			throw ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more equipmentId not found")
		}
		
		val current = linkRepo.findAllByConcertVenue_Id(venueId).associateBy { it.equipment.id!! }.toMutableMap()
		
		// delete those not present anymore
		for ((eqId, link) in current.toMap()) {
			if (!ids.contains(eqId)) {
				linkRepo.delete(link)
				current.remove(eqId)
			}
		}
		
		// upsert provided
		val result = mutableListOf<EquipmentLinkReadDto>()
		for (dto in items) {
			val eq = existingEquip.first { it.id == dto.equipmentId }
			val link = current[dto.equipmentId]?.apply { quantity = dto.quantity }
				?: ConcertVenueEquipment(
					id = ConcertVenueEquipmentId(venueId, dto.equipmentId),
					concertVenue = venue,
					equipment = eq,
					quantity = dto.quantity
				)
			val saved = linkRepo.save(link)
			result += mapper.toReadDto(saved)
		}
		return result.sortedBy { it.equipment?.name ?: "" }
	}
	
	@Transactional
	fun addOne(venueId: UUID, dto: EquipmentLinkUpsertDto): EquipmentLinkReadDto {
		val venue = ensureVenueExists(venueId)
		val eq = equipmentRepo.findById(dto.equipmentId).orElseThrow {
			ResponseStatusException(HttpStatus.BAD_REQUEST, "equipmentId not found: ${dto.equipmentId}")
		}
		if (linkRepo.existsByConcertVenue_IdAndEquipment_Id(venueId, dto.equipmentId)) {
			throw ResponseStatusException(HttpStatus.CONFLICT, "Equipment already attached")
		}
		val link = ConcertVenueEquipment(
			id = ConcertVenueEquipmentId(venueId, dto.equipmentId),
			concertVenue = venue,
			equipment = eq,
			quantity = dto.quantity
		)
		return mapper.toReadDto(linkRepo.save(link))
	}
	
	@Transactional
	fun patchOne(venueId: UUID, equipmentId: UUID, dto: EquipmentLinkPatchDto): EquipmentLinkReadDto {
		val link = linkRepo.findByConcertVenue_IdAndEquipment_Id(venueId, equipmentId).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "Equipment not attached")
		}
		link.quantity = dto.quantity
		return mapper.toReadDto(linkRepo.save(link))
	}
	
	@Transactional
	fun removeOne(venueId: UUID, equipmentId: UUID) {
		linkRepo.deleteByConcertVenue_IdAndEquipment_Id(venueId, equipmentId)
			?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Equipment not attached")
	}
	
	private fun ensureVenueExists(id: UUID): ConcertVenueProfile =
		concertVenueRepo.findById(id).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "ConcertVenue profile not attached: $id")
		}
}
