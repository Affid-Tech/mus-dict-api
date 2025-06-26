package org.affidtech.musdict.musdictapi.web.dto

import java.util.*

data class ConcertVenueCreateDTO(
	val id: UUID,
	val name: String,
	val cover: String?,
	val addressId: UUID,
	val description: String?,
	val contacts: String?,
	val capacity: Int?,
	val terms: String?
)

data class ConcertVenueUpdateDTO(
	val name: String?,
	val cover: String?,
	val addressId: UUID?,
	val description: String?,
	val contacts: String?,
	val capacity: Int?,
	val terms: String?
)

data class ConcertVenueReadDTO(
	val id: UUID,
	val name: String,
	val cover: String?,
	val address: AddressReadDTO,
	val description: String?,
	val contacts: String?,
	val capacity: Int?,
	val terms: String?,
	val equipment: List<ConcertVenueEquipmentReadDTO>
)

