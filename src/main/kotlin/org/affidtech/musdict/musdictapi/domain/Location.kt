package org.affidtech.musdict.musdictapi.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "location")
@Inheritance(strategy = InheritanceType.JOINED)
class Location(
	@Id
	@GeneratedValue
	val id: UUID? = null,
	
	@Column(nullable = false)
	val name: String,
	
	val cover: String? = null,
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id", nullable = false)
	val address: Address,
	
	val description: String? = null,
	val contacts: String? = null
)
