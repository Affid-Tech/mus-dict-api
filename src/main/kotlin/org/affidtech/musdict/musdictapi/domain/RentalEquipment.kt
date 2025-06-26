package org.affidtech.musdict.musdictapi.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "rental_equipment")
data class RentalEquipment(
	@Id
	@GeneratedValue
	val id: UUID? = null,
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rental_id", nullable = false)
	val rental: Rental,
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "equipment_id", nullable = false)
	val equipment: Equipment,
	
	@Column(nullable = false)
	val quantity: Int
)
