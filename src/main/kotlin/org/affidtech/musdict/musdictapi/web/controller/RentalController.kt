package org.affidtech.musdict.musdictapi.web.controller

import jakarta.validation.Valid
import org.affidtech.musdict.musdictapi.service.RentalService
import org.affidtech.musdict.musdictapi.web.dto.RentalProfileCreateDto
import org.affidtech.musdict.musdictapi.web.dto.RentalProfileDto
import org.affidtech.musdict.musdictapi.web.dto.RentalProfilePatchDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/locations/{id}/rental")
class RentalController(
	private val service: RentalService
) {
	@GetMapping
	fun get(@PathVariable id: UUID): RentalProfileDto = service.get(id)
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun attach(@PathVariable id: UUID, @RequestBody @Valid body: RentalProfileCreateDto): RentalProfileDto =
		service.attach(id, body)
	
	@PatchMapping
	fun patch(@PathVariable id: UUID, @RequestBody @Valid body: RentalProfilePatchDto): RentalProfileDto =
		service.patch(id, body)
	
	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun detach(@PathVariable id: UUID) = service.detach(id)
}
