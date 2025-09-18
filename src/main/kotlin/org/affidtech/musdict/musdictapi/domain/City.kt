package org.affidtech.musdict.musdictapi.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "city")
data class City(
	@Id
	val id: UUID,
	
	@Column(nullable = false)
	var name: String
)
