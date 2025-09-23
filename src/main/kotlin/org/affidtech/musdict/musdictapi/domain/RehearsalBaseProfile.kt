package org.affidtech.musdict.musdictapi.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "rehearsal_base_profile")
class RehearsalBaseProfile(
	@MapsId
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id")
	var location: Location,
	
	@Id
	var id: UUID? = null
)