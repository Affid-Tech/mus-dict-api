package org.affidtech.musdict.musdictapi.web.dto

import java.util.UUID

data class RehearsalBaseCreateDTO(
	val id: UUID,
	val name: String,
	val cover: String?,
	val addressId: UUID,
	val description: String?,
	val contacts: String?
)

data class RehearsalBaseUpdateDTO(
	val name: String?,
	val cover: String?,
	val addressId: UUID?,
	val description: String?,
	val contacts: String?
)

data class RehearsalBaseReadDTO(
	val id: UUID,
	val name: String,
	val cover: String?,
	val address: AddressReadDTO,
	val description: String?,
	val contacts: String?
)
