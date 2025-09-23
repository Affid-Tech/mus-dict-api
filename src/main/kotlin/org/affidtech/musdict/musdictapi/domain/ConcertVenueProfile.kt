package org.affidtech.musdict.musdictapi.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "concert_venue_profile")
class ConcertVenueProfile(
	@MapsId
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id")
	var location: Location,
	
	@Id
	var id: UUID? = null,
	
	@Column(nullable = true)
	var capacity: Int? = null,
	
	@Column(nullable = true, columnDefinition = "text")
	var terms: String? = null
) {
	@OneToMany(mappedBy = "concertVenue", cascade = [CascadeType.ALL], orphanRemoval = true)
	var equipment: MutableSet<ConcertVenueEquipment> = mutableSetOf()
}

