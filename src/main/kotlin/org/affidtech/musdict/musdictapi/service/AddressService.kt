package org.affidtech.musdict.musdictapi.service

import org.affidtech.musdict.musdictapi.web.dto.AddressCreate
import org.affidtech.musdict.musdictapi.web.dto.AddressReadDetail
import org.affidtech.musdict.musdictapi.web.dto.AddressReadSummary
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class AddressService {
	
	fun create(dto: AddressCreate): AddressReadDetail {
		// TODO: create Address, return AddressReadDetail
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
	): List<AddressReadSummary> {
		// TODO: query addresses by filters/pagination, return summaries
		throw TODO("Not yet implemented")
	}
	
	fun get(id: UUID): AddressReadDetail {
		// TODO: fetch single address, return detail
		throw TODO("Not yet implemented")
	}
	
	fun delete(id: UUID) {
		// TODO: delete address if refCount == 0; otherwise throw 409
		throw TODO("Not yet implemented")
	}
}
