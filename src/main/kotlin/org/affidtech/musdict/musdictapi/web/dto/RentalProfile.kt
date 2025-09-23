package org.affidtech.musdict.musdictapi.web.dto

/** Currently no extra fields for Rental; DTO left expandable for future needs. */
data class RentalProfileDto(
	val placeholder: String?
)

data class RentalProfileCreateDto(
	val placeholder: String? = null
)

data class RentalProfilePatchDto(
	val placeholder: String? = null
)
