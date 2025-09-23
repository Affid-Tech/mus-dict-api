package org.affidtech.musdict.musdictapi.web.dto

/** No extra fields at the moment; expandable later. */
data class RehearsalBaseProfileDto(
	val placeholder: String?
)

data class RehearsalBaseProfileCreateDto(
	val placeholder: String? = null
)

data class RehearsalBaseProfilePatchDto(
	val placeholder: String? = null
)
