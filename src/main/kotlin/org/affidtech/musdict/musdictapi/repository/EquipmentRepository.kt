package org.affidtech.musdict.musdictapi.repository

import org.affidtech.musdict.musdictapi.domain.Equipment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface EquipmentRepository : JpaRepository<Equipment, UUID> {
	
	fun existsByNameIgnoreCase(name: String): Boolean
	
	fun findByNameContainingIgnoreCase(name: String, pageable: Pageable): Page<Equipment>
}
