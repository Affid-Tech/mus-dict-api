package org.affidtech.musdict.musdictapi.web.controller

import jakarta.validation.Valid
import org.affidtech.musdict.musdictapi.service.StudioService
import org.affidtech.musdict.musdictapi.web.dto.StudioProfileCreateDto
import org.affidtech.musdict.musdictapi.web.dto.StudioProfileDto
import org.affidtech.musdict.musdictapi.web.dto.StudioProfilePatchDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/locations/{id}/studio")
class StudioController(
	private val service: StudioService
) {
	@GetMapping
	fun get(@PathVariable id: UUID): StudioProfileDto = service.get(id)
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun attach(@PathVariable id: UUID, @RequestBody @Valid body: StudioProfileCreateDto): StudioProfileDto =
		service.attach(id, body)
	
	@PatchMapping
	fun patch(@PathVariable id: UUID, @RequestBody @Valid body: StudioProfilePatchDto): StudioProfileDto =
		service.patch(id, body)
	
	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun detach(@PathVariable id: UUID) = service.detach(id)
}
