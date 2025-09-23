package org.affidtech.musdict.musdictapi.repository

import org.affidtech.musdict.musdictapi.domain.ConcertVenueProfile
import org.affidtech.musdict.musdictapi.domain.Location
import org.affidtech.musdict.musdictapi.domain.RehearsalBaseProfile
import org.affidtech.musdict.musdictapi.domain.RentalProfile
import org.affidtech.musdict.musdictapi.domain.StudioProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface LocationRepository : JpaRepository<Location, UUID>, JpaSpecificationExecutor<Location>

interface LocationProfileRepository{
	fun existsByLocationId(locationId: UUID): Boolean
	fun deleteByLocationId(locationId: UUID)
}

interface ConcertVenueProfileRepository : LocationProfileRepository, JpaRepository<ConcertVenueProfile, UUID>

interface RentalProfileRepository : LocationProfileRepository, JpaRepository<RentalProfile, UUID>

interface RehearsalBaseProfileRepository : LocationProfileRepository, JpaRepository<RehearsalBaseProfile, UUID>

interface StudioProfileRepository : LocationProfileRepository, JpaRepository<StudioProfile, UUID>