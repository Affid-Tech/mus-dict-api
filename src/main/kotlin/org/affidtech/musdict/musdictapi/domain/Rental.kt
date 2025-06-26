package org.affidtech.musdict.musdictapi.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "rental")
@PrimaryKeyJoinColumn(name = "location_id")
class Rental(
	id: UUID,
	name: String,
	cover: String?,
	address: Address,
	description: String?,
	contacts: String?
) : Location(id, name, cover, address, description, contacts) {
	
	@OneToMany(mappedBy = "rental", cascade = [CascadeType.ALL], orphanRemoval = true)
	val equipment: MutableSet<RentalEquipment> = mutableSetOf()
}
