package org.affidtech.musdict.musdictapi.web.dto

import java.util.*

data class ConcertVenueEquipmentCreateDTO(
	val equipmentId: UUID,
	val quantity: Int
)

data class ConcertVenueEquipmentUpdateDTO(
	val quantity: Int
)

data class ConcertVenueEquipmentReadDTO(
	val equipmentId: UUID,
	val name: String,
	val quantity: Int
)
