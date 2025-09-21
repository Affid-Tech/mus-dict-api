package org.affidtech.musdict.musdictapi.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "city")
data class City(
	@Id
	@GeneratedValue
	var id: UUID? = null,
	
	@Column(nullable = false)
	var name: String
)
