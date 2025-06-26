package org.affidtech.musdict.musdictapi.web.dto

import java.util.*

data class LocationCreateDTO(
	val name: String,
	val cover: String?,
	val addressId: UUID,
	val description: String?,
	val contacts: String?
)

data class LocationUpdateDTO(
	val name: String?,
	val cover: String?,
	val addressId: UUID?,
	val description: String?,
	val contacts: String?
)

data class LocationReadDTO(
	val id: UUID,
	val name: String,
	val cover: String?,
	val address: AddressReadDTO,
	val description: String?,
	val contacts: String?
)
