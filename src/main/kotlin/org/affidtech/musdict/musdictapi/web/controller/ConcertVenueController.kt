package org.affidtech.musdict.musdictapi.web.controller

import jakarta.validation.Valid
import org.affidtech.musdict.musdictapi.service.ConcertVenueService
import org.affidtech.musdict.musdictapi.web.dto.ConcertVenueProfileCreateDto
import org.affidtech.musdict.musdictapi.web.dto.ConcertVenueProfileDto
import org.affidtech.musdict.musdictapi.web.dto.ConcertVenueProfilePatchDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/locations/{id}/concert-venue")
class ConcertVenueController(
	private val service: ConcertVenueService
) {
	@GetMapping
	fun get(@PathVariable id: UUID): ConcertVenueProfileDto = service.get(id)
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun attach(@PathVariable id: UUID, @RequestBody @Valid body: ConcertVenueProfileCreateDto): ConcertVenueProfileDto =
		service.attach(id, body)
	
	@PatchMapping
	fun patch(@PathVariable id: UUID, @RequestBody @Valid body: ConcertVenueProfilePatchDto): ConcertVenueProfileDto =
		service.patch(id, body)
	
	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun detach(@PathVariable id: UUID) = service.detach(id)
}
