package org.affidtech.musdict.musdictapi.repository

import org.affidtech.musdict.musdictapi.domain.Address
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface AddressRepository : JpaRepository<Address, UUID>, JpaSpecificationExecutor<Address>
