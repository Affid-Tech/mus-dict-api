package org.affidtech.musdict.musdictapi.domain

import jakarta.persistence.*
import java.io.Serializable
import java.util.*

@Entity
@Table(name = "rental_equipment")
class RentalEquipment(
	
	@EmbeddedId
	var id: RentalEquipmentId = RentalEquipmentId(),
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId("rentalId")
	@JoinColumn(name = "rental_id", nullable = false)
	var rental: RentalProfile,
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId("equipmentId")
	@JoinColumn(name = "equipment_id", nullable = false)
	var equipment: Equipment,
	
	@Column(nullable = false)
	var quantity: Int
)

@Embeddable
data class RentalEquipmentId(
	var rentalId: UUID? = null,
	var equipmentId: UUID? = null
) : Serializable