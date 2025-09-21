package org.affidtech.musdict.musdictapi.service.spec

import org.affidtech.musdict.musdictapi.domain.Address
import org.locationtech.jts.geom.Point
import org.springframework.data.jpa.domain.Specification
import java.math.BigDecimal
import java.util.UUID

object AddressSpecifications {
	
	fun cityEquals(cityId: UUID?): Specification<Address>? =
		if (cityId == null) null else Specification { root, _, cb ->
			cb.equal(root.get<Any>("city").get<UUID>("id"), cityId)
		}
	
	fun qLike(q: String?): Specification<Address>? =
		if (q.isNullOrBlank()) null else Specification { root, _, cb ->
			cb.like(cb.lower(root.get("readableAddress")), "%${q.lowercase()}%")
		}
	
	/**
	 * Uses PostGIS ST_DWithin(geography) via CriteriaBuilder.function
	 * radiusMeters is required if lat/lon present.
	 */
	fun withinDistance(lat: BigDecimal?, lon: BigDecimal?, radiusMeters: Int?): Specification<Address>? {
		if (lat == null || lon == null || radiusMeters == null) return null
		return Specification { root, _, cb ->
			// ST_DWithin(coordinates, ST_SetSRID(ST_MakePoint(lon, lat),4326)::geography, radius)
			val coordinatesPath = root.get<Point>("coordinates")
			
			val makePoint = cb.function(
				"ST_SetSRID",
				Any::class.java,
				cb.function("ST_MakePoint", Any::class.java, cb.literal(lon.toDouble()), cb.literal(lat.toDouble())),
				cb.literal(4326)
			)
			
			val geographyPoint = cb.function("geography", Any::class.java, makePoint)
			
			cb.isTrue(
				cb.function(
					"ST_DWithin",
					Boolean::class.java,
					cb.function("geography", Any::class.java, coordinatesPath),
					geographyPoint,
					cb.literal(radiusMeters.toDouble())
				)
			)
		}
	}
	
	fun and(vararg specs: Specification<Address>?): Specification<Address> {
		return specs.filterNotNull().fold(initial = Specification{_,_,_ -> null}) { acc, spec -> acc.and(spec) }
	}
}