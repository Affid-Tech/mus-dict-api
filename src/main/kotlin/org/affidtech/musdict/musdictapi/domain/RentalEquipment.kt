package org.affidtech.musdict.musdictapi.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "rental_equipment")
class RentalEquipment(
	@Id @GeneratedValue
	var id: UUID? = null,
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rental_id", nullable = false)
	var rental: RentalProfile,
	
	@Column(nullable = false, length = 120)
	var name: String,
	
	@Column(nullable = true, columnDefinition = "text")
	var details: String? = null
)