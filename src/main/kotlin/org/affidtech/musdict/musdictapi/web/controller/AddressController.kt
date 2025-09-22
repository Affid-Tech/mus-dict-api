package org.affidtech.musdict.musdictapi.web.controller

import jakarta.validation.Valid
import org.affidtech.musdict.musdictapi.service.AddressService
import org.affidtech.musdict.musdictapi.web.dto.AddressCreate
import org.affidtech.musdict.musdictapi.web.dto.AddressReadDetail
import org.affidtech.musdict.musdictapi.web.dto.AddressReadSummary
import org.affidtech.musdict.musdictapi.web.dto.AddressUpdate
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.*

@Validated
@RestController
@RequestMapping("/api/v1/addresses")
class AddressController(
	private val addressService: AddressService
) {
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun create(@RequestBody @Valid body: AddressCreate): AddressReadDetail =
		addressService.create(body)
	
	@PatchMapping("/{id}")
	fun update(@PathVariable id: UUID, @RequestBody @Valid body: AddressUpdate): AddressReadDetail =
		addressService.update(id, body)
	
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
	): Page<AddressReadSummary> =
		addressService.list(
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
	fun get(@PathVariable id: UUID): AddressReadDetail =
		addressService.get(id)
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun delete(@PathVariable id: UUID) {
		addressService.delete(id)
	}
}
