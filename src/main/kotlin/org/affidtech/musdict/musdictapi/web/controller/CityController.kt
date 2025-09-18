package org.affidtech.musdict.musdictapi.web.controller

import jakarta.validation.Valid
import org.affidtech.musdict.musdictapi.service.CityService
import org.affidtech.musdict.musdictapi.web.dto.CityCreate
import org.affidtech.musdict.musdictapi.web.dto.CityReadDetail
import org.affidtech.musdict.musdictapi.web.dto.CityReadSummary
import org.affidtech.musdict.musdictapi.web.dto.CityUpdate
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.UUID

@Validated
@RestController
@RequestMapping("/api/v1/cities")
class CityController(
	private val cityService: CityService
) {
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun create(@RequestBody @Valid body: CityCreate): CityReadDetail =
		cityService.create(body)
	
	@GetMapping
	fun list(
		@RequestParam(defaultValue = "0") page: Int,
		@RequestParam(defaultValue = "50") size: Int,
		@RequestParam(required = false) q: String?
	): Page<CityReadSummary> =
		cityService.list(page = page, size = size, q = q)
	
	@GetMapping("/{id}")
	fun get(@PathVariable id: UUID): CityReadDetail =
		cityService.get(id)
	
	@PatchMapping("/{id}")
	fun update(@PathVariable id: UUID, @RequestBody @Valid body: CityUpdate): CityReadDetail =
		cityService.update(id, body)
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun delete(@PathVariable id: UUID) {
		cityService.delete(id)
	}
}
