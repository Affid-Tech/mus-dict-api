package org.affidtech.musdict.musdictapi.web.controller

import jakarta.validation.Valid
import org.affidtech.musdict.musdictapi.service.ConcertVenueEquipmentService
import org.affidtech.musdict.musdictapi.web.dto.EquipmentLinkPatchDto
import org.affidtech.musdict.musdictapi.web.dto.EquipmentLinkReadDto
import org.affidtech.musdict.musdictapi.web.dto.EquipmentLinkUpsertDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/locations/{id}/concert-venue/equipment")
class ConcertVenueEquipmentController(
	private val service: ConcertVenueEquipmentService
) {
	@GetMapping
	fun list(@PathVariable id: UUID): List<EquipmentLinkReadDto> =
		service.list(id)
	
	/** Replace collection (idempotent). */
	@PutMapping
	fun replace(@PathVariable id: UUID, @RequestBody @Valid body: List<EquipmentLinkUpsertDto>): List<EquipmentLinkReadDto> =
		service.replace(id, body)
	
	/** Add single item. */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun addOne(@PathVariable id: UUID, @RequestBody @Valid body: EquipmentLinkUpsertDto): EquipmentLinkReadDto =
		service.addOne(id, body)
	
	/** Update quantity of a single item. */
	@PatchMapping("/{equipmentId}")
	fun patchOne(
		@PathVariable id: UUID,
		@PathVariable equipmentId: UUID,
		@RequestBody @Valid body: EquipmentLinkPatchDto
	): EquipmentLinkReadDto = service.patchOne(id, equipmentId, body)
	
	/** Remove one. */
	@DeleteMapping("/{equipmentId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun removeOne(@PathVariable id: UUID, @PathVariable equipmentId: UUID) =
		service.removeOne(id, equipmentId)
}
