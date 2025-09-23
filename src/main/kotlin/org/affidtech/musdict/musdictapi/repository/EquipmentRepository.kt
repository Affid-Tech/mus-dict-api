package org.affidtech.musdict.musdictapi.repository

import org.affidtech.musdict.musdictapi.domain.Equipment
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface EquipmentRepository : JpaRepository<Equipment, UUID>
