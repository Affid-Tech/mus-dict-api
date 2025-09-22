package org.affidtech.musdict.musdictapi.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "concert_venue_equipment")
data class ConcertVenueEquipment(
	@Id
	@GeneratedValue
	var id: UUID? = null,
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "venue_id", nullable = false)
	val concertVenue: ConcertVenue,
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "equipment_id", nullable = false)
	val equipment: Equipment,
	
	@Column(nullable = false)
	val quantity: Int
)
