package org.affidtech.musdict.musdictapi.web.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.*

data class EquipmentReadDto(
	val id: UUID,
	val name: String,
	val cover: String?,
	val description: String?
)

data class EquipmentCreateDto(
	@field:NotBlank
	@field:Size(min = 1, max = 200)
	val name: String,
	
	@field:Size(max = 1000)
	val cover: String? = null,
	
	@field:Size(max = 5000)
	val description: String? = null
)

data class EquipmentPatchDto(
	@field:Size(min = 1, max = 200)
	val name: String? = null,
	
	@field:Size(max = 1000)
	val cover: String? = null,
	
	@field:Size(max = 5000)
	val description: String? = null
)
