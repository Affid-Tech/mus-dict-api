package org.affidtech.musdict.musdictapi.repository

import org.affidtech.musdict.musdictapi.domain.ConcertVenueEquipment
import org.affidtech.musdict.musdictapi.domain.ConcertVenueEquipmentId
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ConcertVenueEquipmentRepository : JpaRepository<ConcertVenueEquipment, ConcertVenueEquipmentId> {
	fun findAllByConcertVenue_Id(venueId: UUID): List<ConcertVenueEquipment>
	fun existsByConcertVenue_IdAndEquipment_Id(venueId: UUID, equipmentId: UUID): Boolean
	fun findByConcertVenue_IdAndEquipment_Id(venueId: UUID, equipmentId: UUID): java.util.Optional<ConcertVenueEquipment>
	fun deleteByConcertVenue_IdAndEquipment_Id(venueId: UUID, equipmentId: UUID): ConcertVenueEquipment?
	fun deleteByConcertVenue_Id(venueId: UUID): ConcertVenueEquipment?
}
