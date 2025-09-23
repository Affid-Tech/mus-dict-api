package org.affidtech.musdict.musdictapi.domain

import jakarta.persistence.*
import java.util.*


@Entity
@Table(name = "studio_profile")
class StudioProfile(
	@MapsId
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id")
	var location: Location,
	
	@Id
	var id: UUID? = null
)