package org.affidtech.musdict.musdictapi.service.spec

import org.affidtech.musdict.musdictapi.domain.Location
import org.locationtech.jts.geom.Point
import org.springframework.data.jpa.domain.Specification
import java.math.BigDecimal
import java.util.*

object LocationSpecifications {
	
	fun cityEquals(cityId: UUID?): Specification<Location>? =
		if (cityId == null) null else Specification { root, _, cb ->
			val address = root.get<Any>("address")
			cb.equal(address.get<Any>("city").get<UUID>("id"), cityId)
		}
	
	fun qLike(q: String?): Specification<Location>? =
		if (q.isNullOrBlank()) null else Specification { root, _, cb ->
			val address = root.get<Any>("address")
			val byName = cb.like(cb.lower(root.get("name")), "%${q.lowercase()}%")
			val byAddr = cb.like(cb.lower(address.get("readableAddress")), "%${q.lowercase()}%")
			cb.or(byName, byAddr)
		}
	
	fun withinDistance(lat: BigDecimal?, lon: BigDecimal?, radiusMeters: Int?): Specification<Location>? {
		if (lat == null || lon == null || radiusMeters == null) return null
		return Specification { root, _, cb ->
			val address = root.get<Any>("address")
			val coords = address.get<Point>("coordinates")
			
			val makePoint = cb.function(
				"ST_SetSRID", Any::class.java,
				cb.function("ST_MakePoint", Any::class.java, cb.literal(lon.toDouble()), cb.literal(lat.toDouble())),
				cb.literal(4326)
			)
			val geographyPoint = cb.function("geography", Any::class.java, makePoint)
			
			cb.isTrue(
				cb.function(
					"ST_DWithin", Boolean::class.java,
					cb.function("geography", Any::class.java, coords),
					geographyPoint,
					cb.literal(radiusMeters.toDouble())
				)
			)
		}
	}
	
	fun and(vararg specs: Specification<Location>?): Specification<Location> {
		return specs.filterNotNull().fold(initial = Specification{_,_,_ -> null}) { acc, spec -> acc.and(spec) }
	}
}
