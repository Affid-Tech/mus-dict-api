package org.affidtech.musdict.musdictapi.domain

import jakarta.persistence.*
import java.io.Serializable
import java.util.*

@Entity
@Table(name = "concert_venue_equipment")
class ConcertVenueEquipment(
	
	@EmbeddedId
	var id: ConcertVenueEquipmentId = ConcertVenueEquipmentId(),
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId("venueId")
	@JoinColumn(name = "venue_id", nullable = false)
	var concertVenue: ConcertVenueProfile,
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId("equipmentId")
	@JoinColumn(name = "equipment_id", nullable = false)
	var equipment: Equipment,
	
	@Column(nullable = false)
	var quantity: Int
)

@Embeddable
data class ConcertVenueEquipmentId(
	var venueId: UUID? = null,
	var equipmentId: UUID? = null
) : Serializable