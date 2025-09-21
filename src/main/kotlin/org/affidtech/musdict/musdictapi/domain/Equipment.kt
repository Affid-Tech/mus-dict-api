package org.affidtech.musdict.musdictapi.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "equipment")
data class Equipment(
	@Id
	@GeneratedValue
	val id: UUID? = null,
	
	@Column(nullable = false)
	val name: String,
	
	val cover: String? = null,
	val description: String? = null
)
