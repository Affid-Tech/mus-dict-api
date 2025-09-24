package org.affidtech.musdict.musdictapi.web.controller

import jakarta.validation.Valid
import org.affidtech.musdict.musdictapi.service.EquipmentService
import org.affidtech.musdict.musdictapi.web.dto.EquipmentCreateDto
import org.affidtech.musdict.musdictapi.web.dto.EquipmentPatchDto
import org.affidtech.musdict.musdictapi.web.dto.EquipmentReadDto
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@Validated
@RestController
@RequestMapping("/api/v1/equipment")
class EquipmentController(
	private val service: EquipmentService
) {
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun create(@RequestBody @Valid body: EquipmentCreateDto): EquipmentReadDto =
		service.create(body)
	
	@GetMapping
	fun list(
		@RequestParam(required = false) q: String?,
		@RequestParam(defaultValue = "0") page: Int,
		@RequestParam(defaultValue = "20") size: Int,
		@RequestParam(required = false) sort: String?
	): Page<EquipmentReadDto> =
		service.list(q, page, size, sort)
	
	@GetMapping("/{id}")
	fun get(@PathVariable id: UUID): EquipmentReadDto =
		service.get(id)
	
	@PatchMapping("/{id}")
	fun patch(@PathVariable id: UUID, @RequestBody @Valid body: EquipmentPatchDto): EquipmentReadDto =
		service.patch(id, body)
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun delete(@PathVariable id: UUID) =
		service.delete(id)
}
