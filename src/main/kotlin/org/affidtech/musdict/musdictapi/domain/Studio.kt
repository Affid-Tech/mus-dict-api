package org.affidtech.musdict.musdictapi.domain

import jakarta.persistence.Entity
import jakarta.persistence.PrimaryKeyJoinColumn
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "studio")
@PrimaryKeyJoinColumn(name = "location_id")
class Studio(
	id: UUID,
	name: String,
	cover: String?,
	address: Address,
	description: String?,
	contacts: String?
) : Location(id, name, cover, address, description, contacts)
