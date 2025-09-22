package org.affidtech.musdict.musdictapi.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "location")
@Inheritance(strategy = InheritanceType.JOINED)
class Location(
	@Id
	@GeneratedValue
	var id: UUID? = null,
	
	@Column(nullable = false)
	var name: String,
	
	var cover: String? = null,
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id", nullable = false)
	var address: Address,
	
	var description: String? = null,
	var contacts: String? = null
)
