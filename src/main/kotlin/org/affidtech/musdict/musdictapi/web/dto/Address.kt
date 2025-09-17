package org.affidtech.musdict.musdictapi.web.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.util.*

data class AddressReadSummary(
	@field:NotNull
	val id: UUID,
	
	@field:Size(max = 500)
	val readableAddress: String? = null,
	
	@field:Valid
	val city: CityReadSummary? = null
)

data class AddressReadDetail(
	@field:NotNull
	val id: UUID,
	
	@field:Size(max = 500)
	val readableAddress: String? = null,
	
	@field:Valid
	val city: CityReadSummary? = null,
	
	@field:Valid
	val coordinates: CoordinatesDto? = null
)

data class AddressCreate(
	val cityId: UUID? = null,
	
	@field:Valid
	val coordinates: CoordinatesDto? = null,
	
	@field:Size(max = 500)
	val readableAddress: String? = null
)

data class CoordinatesDto(
	@field:NotNull
	@field:DecimalMin(value = "-90.0", inclusive = true)
	@field:DecimalMax(value = "90.0", inclusive = true)
	val lat: BigDecimal,
	
	@field:NotNull
	@field:DecimalMin(value = "-180.0", inclusive = true)
	@field:DecimalMax(value = "180.0", inclusive = true)
	val lon: BigDecimal
)