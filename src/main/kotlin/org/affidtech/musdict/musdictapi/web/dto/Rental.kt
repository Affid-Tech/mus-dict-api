package org.affidtech.musdict.musdictapi.web.dto

import java.util.UUID

data class RentalCreateDTO(
	val id: UUID,
	val name: String,
	val cover: String?,
	val addressId: UUID,
	val description: String?,
	val contacts: String?
)

data class RentalUpdateDTO(
	val name: String?,
	val cover: String?,
	val addressId: UUID?,
	val description: String?,
	val contacts: String?
)

data class RentalReadDTO(
	val id: UUID,
	val name: String,
	val cover: String?,
	val address: AddressReadDTO,
	val description: String?,
	val contacts: String?,
	val equipment: List<RentalEquipmentReadDTO>
)
