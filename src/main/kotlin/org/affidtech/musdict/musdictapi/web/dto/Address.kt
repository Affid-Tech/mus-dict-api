package org.affidtech.musdict.musdictapi.web.dto

import java.util.*

data class AddressReadDTO(
	val id: UUID,
	val readableAddress: String?,
	val latitude: Double?,
	val longitude: Double?
)
