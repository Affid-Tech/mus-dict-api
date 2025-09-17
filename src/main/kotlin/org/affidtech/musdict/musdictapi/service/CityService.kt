package org.affidtech.musdict.musdictapi.service

import org.affidtech.musdict.musdictapi.web.dto.CityCreate
import org.affidtech.musdict.musdictapi.web.dto.CityReadDetail
import org.affidtech.musdict.musdictapi.web.dto.CityReadSummary
import org.affidtech.musdict.musdictapi.web.dto.CityUpdate
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CityService {
	
	fun create(dto: CityCreate): CityReadDetail {
		// TODO: persist city, return detail
		throw TODO("Not yet implemented")
	}
	
	fun list(page: Int, size: Int, q: String?): List<CityReadSummary> {
		// TODO: query (optionally filter by q), paginate, map to summaries
		throw TODO("Not yet implemented")
	}
	
	fun get(id: UUID): CityReadDetail {
		// TODO: load city, map to detail
		throw TODO("Not yet implemented")
	}
	
	fun update(id: UUID, dto: CityUpdate): CityReadDetail {
		// TODO: apply partial update, map to detail
		throw TODO("Not yet implemented")
	}
	
	fun delete(id: UUID) {
		// TODO: ensure no addresses reference this city; delete or 409
		throw TODO("Not yet implemented")
	}
}
