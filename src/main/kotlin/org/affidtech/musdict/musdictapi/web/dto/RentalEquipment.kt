package org.affidtech.musdict.musdictapi.web.dto

import java.util.UUID

data class RentalEquipmentCreateDTO(
	val equipmentId: UUID,
	val quantity: Int
)

data class RentalEquipmentUpdateDTO(
	val quantity: Int
)

data class RentalEquipmentReadDTO(
	val equipmentId: UUID,
	val name: String,
	val quantity: Int
)
