package org.affidtech.musdict.musdictapi.web.controller

import jakarta.validation.Valid
import org.affidtech.musdict.musdictapi.service.LocationService
import org.affidtech.musdict.musdictapi.web.dto.LocationCreate
import org.affidtech.musdict.musdictapi.web.dto.LocationReadDetail
import org.affidtech.musdict.musdictapi.web.dto.LocationReadSummary
import org.affidtech.musdict.musdictapi.web.dto.LocationUpdate
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.*

@Validated
@RestController
@RequestMapping("/api/v1/locations")
class LocationController(
	private val locationService: LocationService
) {
	
	// Note: a concrete type of location is determined by the concrete endpoint you expose per type.
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun create(@RequestBody @Valid body: LocationCreate): LocationReadDetail =
		locationService.create(body)
	
	@GetMapping
	fun list(
		@RequestParam(required = false) cityId: UUID?,
		@RequestParam(required = false) q: String?,
		@RequestParam(required = false) nearLat: BigDecimal?,
		@RequestParam(required = false) nearLon: BigDecimal?,
		@RequestParam(required = false) radiusMeters: Int?,
		@RequestParam(defaultValue = "0") page: Int,
		@RequestParam(defaultValue = "20") size: Int,
		@RequestParam(required = false) sort: String?
	): Page<LocationReadSummary> =
		locationService.list(
			cityId = cityId,
			q = q,
			nearLat = nearLat,
			nearLon = nearLon,
			radiusMeters = radiusMeters,
			page = page,
			size = size,
			sort = sort
		)
	
	@GetMapping("/{id}")
	fun get(@PathVariable id: UUID): LocationReadDetail =
		locationService.get(id)
	
	@PatchMapping("/{id}")
	fun update(@PathVariable id: UUID, @RequestBody @Valid body: LocationUpdate): LocationReadDetail =
		locationService.update(id, body)
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun delete(@PathVariable id: UUID) {
		locationService.delete(id)
	}
}
