package org.affidtech.musdict.musdictapi.service

import org.affidtech.musdict.musdictapi.web.dto.LocationCreate
import org.affidtech.musdict.musdictapi.web.dto.LocationReadDetail
import org.affidtech.musdict.musdictapi.web.dto.LocationReadSummary
import org.affidtech.musdict.musdictapi.web.dto.LocationUpdate
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class LocationService {
	
	fun create(dto: LocationCreate): LocationReadDetail {
		// TODO:
		// - if dto.addressId -> assert exists; use it
		// - if dto.addressCreate -> create new Address and link (copy-on-write semantics on create)
		// - create Location of the endpoint's type; return detail
		throw TODO("Not yet implemented")
	}
	
	fun list(
		cityId: UUID?,
		q: String?,
		nearLat: BigDecimal?,
		nearLon: BigDecimal?,
		radiusMeters: Int?,
		page: Int,
		size: Int,
		sort: String?
	): List<LocationReadSummary> {
		// TODO: query locations with filters/pagination; map to summaries
		throw TODO("Not yet implemented")
	}
	
	fun get(id: UUID): LocationReadDetail {
		// TODO: fetch single location; map to detail
		throw TODO("Not yet implemented")
	}
	
	fun update(id: UUID, dto: LocationUpdate): LocationReadDetail {
		// TODO:
		// - apply simple fields (name/cover/description/contacts)
		// - if dto.addressId -> repoint to existing Address
		// - if dto.addressCreate -> create new Address and repoint (copy-on-write)
		// - return updated detail
		throw TODO("Not yet implemented")
	}
	
	fun delete(id: UUID) {
		// TODO: delete location; preserve address unless orphan cleanups apply
		throw TODO("Not yet implemented")
	}
}
