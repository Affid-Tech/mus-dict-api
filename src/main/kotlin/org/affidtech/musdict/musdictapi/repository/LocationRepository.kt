package org.affidtech.musdict.musdictapi.repository

import org.affidtech.musdict.musdictapi.domain.Location
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface LocationRepository : JpaRepository<Location, UUID>, JpaSpecificationExecutor<Location>
