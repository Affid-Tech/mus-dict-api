package org.affidtech.musdict.musdictapi.web.controller

import jakarta.validation.Valid
import org.affidtech.musdict.musdictapi.service.RehearsalBaseService
import org.affidtech.musdict.musdictapi.web.dto.RehearsalBaseProfileCreateDto
import org.affidtech.musdict.musdictapi.web.dto.RehearsalBaseProfileDto
import org.affidtech.musdict.musdictapi.web.dto.RehearsalBaseProfilePatchDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/locations/{id}/rehearsal-base")
class RehearsalBaseController(
	private val service: RehearsalBaseService
) {
	@GetMapping
	fun get(@PathVariable id: UUID): RehearsalBaseProfileDto = service.get(id)
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun attach(@PathVariable id: UUID, @RequestBody @Valid body: RehearsalBaseProfileCreateDto): RehearsalBaseProfileDto =
		service.attach(id, body)
	
	@PatchMapping
	fun patch(@PathVariable id: UUID, @RequestBody @Valid body: RehearsalBaseProfilePatchDto): RehearsalBaseProfileDto =
		service.patch(id, body)
	
	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun detach(@PathVariable id: UUID) = service.detach(id)
}
