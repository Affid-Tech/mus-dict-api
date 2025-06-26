package org.affidtech.musdict.musdictapi.domain

import jakarta.persistence.*
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.*

@Entity
@Table(name = "opening_hours")
data class OpeningHours(
	@Id
	val id: UUID,
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id")
	val location: Location,
	
	@Column(name = "day_of_week", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	val dayOfWeek: DayOfWeek,
	
	@Column(name = "open_time", nullable = false)
	val openTime: LocalTime,
	
	@Column(name = "close_time", nullable = false)
	val closeTime: LocalTime
)
