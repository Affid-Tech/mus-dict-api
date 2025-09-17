package org.affidtech.musdict.musdictapi.domain

import jakarta.persistence.*
import org.locationtech.jts.geom.Point
import java.util.*

@Entity
@Table(name = "address")
data class Address(
	@Id
	val id: UUID,
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "city_id")
	val city: City? = null,
	
	@Column(columnDefinition = "geography(Point,4326)")
	val coordinates: Point? = null,
	
	@Column(name = "readable_address")
	val readableAddress: String? = null
)