package org.affidtech.musdict.musdictapi.web.dto

import java.util.*

data class EquipmentCreateDTO(
	val name: String,
	val cover: String? = null,
	val description: String? = null
)

data class EquipmentUpdateDTO(
	val name: String? = null,
	val cover: String? = null,
	val description: String? = null
)

data class EquipmentReadDTO(
	val id: UUID,
	val name: String,
	val cover: String?,
	val description: String?
)
