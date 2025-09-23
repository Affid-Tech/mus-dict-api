package org.affidtech.musdict.musdictapi.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "equipment")
class Equipment(
	@Id
	@GeneratedValue
	var id: UUID? = null,
	
	@Column(nullable = false)
	var name: String,
	
	var cover: String? = null,
	var description: String? = null
)
