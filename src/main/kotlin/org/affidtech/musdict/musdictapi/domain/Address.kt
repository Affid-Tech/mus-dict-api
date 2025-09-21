package org.affidtech.musdict.musdictapi.domain

import jakarta.persistence.*
import org.locationtech.jts.geom.Point
import java.util.*

@Entity
@Table(name = "address")
data class Address(
	@Id
	@GeneratedValue
	var id: UUID? = null,
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "city_id")
	var city: City? = null,
	
	@Column(columnDefinition = "geography(Point,4326)")
	var coordinates: Point? = null,
	
	@Column(name = "readable_address")
	var readableAddress: String? = null
)