package org.affidtech.musdict.musdictapi.web.dto

import java.time.DayOfWeek
import java.time.LocalTime
import java.util.*

data class OpeningHoursCreateDTO(
	val dayOfWeek: DayOfWeek,
	val openTime: LocalTime,
	val closeTime: LocalTime
)

data class OpeningHoursUpdateDTO(
	val openTime: LocalTime?,
	val closeTime: LocalTime?
)

data class OpeningHoursReadDTO(
	val id: UUID,
	val dayOfWeek: DayOfWeek,
	val openTime: LocalTime,
	val closeTime: LocalTime
)
