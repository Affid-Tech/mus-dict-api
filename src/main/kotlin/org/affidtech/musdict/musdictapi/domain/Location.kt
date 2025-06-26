package org.affidtech.musdict.musdictapi.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "location")
@Inheritance(strategy = InheritanceType.JOINED)
open class Location(
	@Id
	open val id: UUID,
	
	@Column(nullable = false)
	open val name: String,
	
	open val cover: String? = null,
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id", nullable = false)
	open val address: Address,
	
	open val description: String? = null,
	open val contacts: String? = null
)
