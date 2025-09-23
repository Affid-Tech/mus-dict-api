package org.affidtech.musdict.musdictapi.service

import org.affidtech.musdict.musdictapi.domain.Location
import org.affidtech.musdict.musdictapi.domain.StudioProfile
import org.affidtech.musdict.musdictapi.repository.LocationRepository
import org.affidtech.musdict.musdictapi.repository.StudioProfileRepository
import org.affidtech.musdict.musdictapi.web.StudioProfileMapper
import org.affidtech.musdict.musdictapi.web.dto.StudioProfileCreateDto
import org.affidtech.musdict.musdictapi.web.dto.StudioProfileDto
import org.affidtech.musdict.musdictapi.web.dto.StudioProfilePatchDto
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class StudioService(
	private val locationRepository: LocationRepository,
	private val profileRepository: StudioProfileRepository,
	private val mapper: StudioProfileMapper
) {
	@Transactional(readOnly = true)
	fun get(locationId: UUID): StudioProfileDto {
		val entity = profileRepository.findById(locationId).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "Studio profile not attached: $locationId")
		}
		return mapper.toDto(entity)
	}
	
	@Transactional
	fun attach(locationId: UUID, @Suppress("UNUSED_PARAMETER") dto: StudioProfileCreateDto): StudioProfileDto {
		if (profileRepository.existsByLocationId(locationId)) {
			throw ResponseStatusException(HttpStatus.CONFLICT, "Studio profile already exists")
		}
		val location: Location = locationRepository.findById(locationId).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found: $locationId")
		}
		val entity = StudioProfile(location = location)
		val saved = profileRepository.save(entity)
		return mapper.toDto(saved)
	}
	
	@Transactional
	fun patch(locationId: UUID, dto: StudioProfilePatchDto): StudioProfileDto {
		val entity = profileRepository.findById(locationId).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "Studio profile not attached: $locationId")
		}
		mapper.updateFromPatch(dto, entity)
		val saved = profileRepository.save(entity)
		return mapper.toDto(saved)
	}
	
	@Transactional
	fun detach(locationId: UUID) {
		if (!profileRepository.existsByLocationId(locationId)) {
			throw ResponseStatusException(HttpStatus.NOT_FOUND, "Studio profile not attached: $locationId")
		}
		profileRepository.deleteByLocationId(locationId)
	}
}
