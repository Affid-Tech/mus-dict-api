package org.affidtech.musdict.musdictapi.web.dto

/** No extra fields at the moment; expandable later. */
data class StudioProfileDto(
	val placeholder: String?
)

data class StudioProfileCreateDto(
	val placeholder: String? = null
)

data class StudioProfilePatchDto(
	val placeholder: String? = null
)
