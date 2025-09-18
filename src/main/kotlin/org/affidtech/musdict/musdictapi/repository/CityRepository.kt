package org.affidtech.musdict.musdictapi.repository

import org.affidtech.musdict.musdictapi.domain.City
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CityRepository : JpaRepository<City, UUID> {
	
	/** Fast existence check to keep names unique at the service layer if you want. */
	fun existsByNameIgnoreCase(name: String): Boolean
	
	/** Direct lookup by exact name (case-insensitive). */
	fun findByNameIgnoreCase(name: String): Optional<City>
	
	/** Simple search used for list(q) with pagination. */
	fun findAllByNameContainingIgnoreCase(name: String, pageable: Pageable): Page<City>
	
}
