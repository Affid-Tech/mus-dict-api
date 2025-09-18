package org.affidtech.musdict.musdictapi.web

import org.affidtech.musdict.musdictapi.domain.City
import org.affidtech.musdict.musdictapi.web.dto.CityReadDetail
import org.affidtech.musdict.musdictapi.web.dto.CityReadSummary
import org.affidtech.musdict.musdictapi.web.dto.CityUpdate
import org.mapstruct.*

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
interface CityMapper {
	fun toReadDetail(entity: City): CityReadDetail
	fun toReadSummary(entity: City): CityReadSummary
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	fun update(dto: CityUpdate, @MappingTarget entity: City)
}