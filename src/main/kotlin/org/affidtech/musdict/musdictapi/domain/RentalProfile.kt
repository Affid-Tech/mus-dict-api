package org.affidtech.musdict.musdictapi.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "rental_profile")
class RentalProfile(
	@MapsId
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id")
	var location: Location,
	
	@Id
	var id: UUID? = null
) {
	@OneToMany(mappedBy = "rental", cascade = [CascadeType.ALL], orphanRemoval = true)
	var equipment: MutableSet<RentalEquipment> = mutableSetOf()
}