package org.affidtech.musdict.musdictapi.web.dto

import java.util.*

data class StudioCreateDTO(
	val id: UUID,
	val name: String,
	val cover: String?,
	val addressId: UUID,
	val description: String?,
	val contacts: String?
)


data class StudioUpdateDTO(
	val name: String?,
	val cover: String?,
	val addressId: UUID?,
	val description: String?,
	val contacts: String?
)

data class StudioReadDTO(
	val id: UUID,
	val name: String,
	val cover: String?,
	val address: AddressReadDTO,
	val description: String?,
	val contacts: String?
)
