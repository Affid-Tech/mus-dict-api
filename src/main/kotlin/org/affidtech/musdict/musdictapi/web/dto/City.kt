package org.affidtech.musdict.musdictapi.web.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID

data class CityReadSummary(
	val id: UUID,
	val name: String
)

typealias CityReadDetail = CityReadSummary

data class CityCreate(
	@field:NotBlank
	@field:Size(min = 1, max = 200)
	val name: String
)

data class CityUpdate(
	@field:Size(min = 1, max = 200)
	val name: String?
)
