package org.affidtech.musdict.musdictapi.web.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

data class ConcertVenueProfileDto(
	val capacity: Int?,
	val terms: String?
)

data class ConcertVenueProfileCreateDto(
	@field:Min(1)
	val capacity: Int? = null,
	
	@field:Size(max = 5000)
	val terms: String? = null
)

data class ConcertVenueProfilePatchDto(
	@field:Min(1)
	val capacity: Int? = null,
	
	@field:Size(max = 5000)
	val terms: String? = null
)
