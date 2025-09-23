package org.affidtech.musdict.musdictapi.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "concert_venue_equipment")
class ConcertVenueEquipment(
	@Id @GeneratedValue
	var id: UUID? = null,
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "concert_venue_id", nullable = false)
	var concertVenue: ConcertVenueProfile,
	
	@Column(nullable = false, length = 120)
	var name: String,
	
	@Column(nullable = true, columnDefinition = "text")
	var details: String? = null
)