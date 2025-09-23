package org.affidtech.musdict.musdictapi.service

import org.affidtech.musdict.musdictapi.domain.Location
import org.affidtech.musdict.musdictapi.domain.RehearsalBaseProfile
import org.affidtech.musdict.musdictapi.repository.LocationRepository
import org.affidtech.musdict.musdictapi.repository.RehearsalBaseProfileRepository
import org.affidtech.musdict.musdictapi.web.RehearsalBaseProfileMapper
import org.affidtech.musdict.musdictapi.web.dto.RehearsalBaseProfileCreateDto
import org.affidtech.musdict.musdictapi.web.dto.RehearsalBaseProfileDto
import org.affidtech.musdict.musdictapi.web.dto.RehearsalBaseProfilePatchDto
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class RehearsalBaseService(
	private val locationRepository: LocationRepository,
	private val profileRepository: RehearsalBaseProfileRepository,
	private val mapper: RehearsalBaseProfileMapper
) {
	
	@Transactional(readOnly = true)
	fun get(locationId: UUID): RehearsalBaseProfileDto {
		val entity = profileRepository.findById(locationId).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "RehearsalBase profile not attached: $locationId")
		}
		return mapper.toDto(entity)
	}
	
	@Transactional
	fun attach(locationId: UUID, @Suppress("UNUSED_PARAMETER") dto: RehearsalBaseProfileCreateDto): RehearsalBaseProfileDto {
		if (profileRepository.existsByLocationId(locationId)) {
			throw ResponseStatusException(HttpStatus.CONFLICT, "RehearsalBase profile already exists")
		}
		val location: Location = locationRepository.findById(locationId).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found: $locationId")
		}
		val entity = RehearsalBaseProfile(location = location)
		val saved = profileRepository.save(entity)
		return mapper.toDto(saved)
	}
	
	@Transactional
	fun patch(locationId: UUID, dto: RehearsalBaseProfilePatchDto): RehearsalBaseProfileDto {
		val entity = profileRepository.findById(locationId).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "RehearsalBase profile not attached: $locationId")
		}
		mapper.updateFromPatch(dto, entity)
		val saved = profileRepository.save(entity)
		return mapper.toDto(saved)
	}
	
	@Transactional
	fun detach(locationId: UUID) {
		if (!profileRepository.existsByLocationId(locationId)) {
			throw ResponseStatusException(HttpStatus.NOT_FOUND, "RehearsalBase profile not attached: $locationId")
		}
		profileRepository.deleteByLocationId(locationId)
	}
}
