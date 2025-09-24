package org.affidtech.musdict.musdictapi.repository

import org.affidtech.musdict.musdictapi.domain.RentalEquipment
import org.affidtech.musdict.musdictapi.domain.RentalEquipmentId
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RentalEquipmentRepository : JpaRepository<RentalEquipment, RentalEquipmentId> {
	fun findAllByRental_Id(rentalId: UUID): List<RentalEquipment>
	fun existsByRental_IdAndEquipment_Id(rentalId: UUID, equipmentId: UUID): Boolean
	fun findByRental_IdAndEquipment_Id(rentalId: UUID, equipmentId: UUID): java.util.Optional<RentalEquipment>
	fun deleteByRental_IdAndEquipment_Id(rentalId: UUID, equipmentId: UUID): RentalEquipment?
	fun deleteByRental_Id(rentalId: UUID): RentalEquipment?
}
