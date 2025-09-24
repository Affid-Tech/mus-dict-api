package org.affidtech.musdict.musdictapi.web.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.util.*

/** Returned for each attached equipment item. */
data class EquipmentLinkReadDto(
	val equipmentId: UUID,
	val quantity: Int,
	val equipment: EquipmentReadDto? = null // optional enrichment
)

/** For POST add-one or in PUT replace list. */
data class EquipmentLinkUpsertDto(
	@field:NotNull
	val equipmentId: UUID,
	@field:Min(1)
	val quantity: Int
)

/** For PATCH quantity of a single item. */
data class EquipmentLinkPatchDto(
	@field:Min(1)
	val quantity: Int
)
