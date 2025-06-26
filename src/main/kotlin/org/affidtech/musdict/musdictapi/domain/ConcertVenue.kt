package org.affidtech.musdict.musdictapi.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "concert_venue")
@PrimaryKeyJoinColumn(name = "location_id")
class ConcertVenue(
	id: UUID,
	name: String,
	cover: String?,
	address: Address,
	description: String?,
	contacts: String?,
	
	@Column(nullable = true)
	val capacity: Int? = null,
	
	@Column(nullable = true)
	val terms: String? = null
) : Location(id, name, cover, address, description, contacts) {
	
	@OneToMany(mappedBy = "concertVenue", cascade = [CascadeType.ALL], orphanRemoval = true)
	val equipment: MutableSet<ConcertVenueEquipment> = mutableSetOf()
}

