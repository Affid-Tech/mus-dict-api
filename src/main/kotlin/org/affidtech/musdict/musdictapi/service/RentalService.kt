package org.affidtech.musdict.musdictapi.service

import org.affidtech.musdict.musdictapi.domain.Location
import org.affidtech.musdict.musdictapi.domain.RentalProfile
import org.affidtech.musdict.musdictapi.repository.LocationRepository
import org.affidtech.musdict.musdictapi.repository.RentalProfileRepository
import org.affidtech.musdict.musdictapi.web.RentalProfileMapper
import org.affidtech.musdict.musdictapi.web.dto.RentalProfileCreateDto
import org.affidtech.musdict.musdictapi.web.dto.RentalProfileDto
import org.affidtech.musdict.musdictapi.web.dto.RentalProfilePatchDto
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class RentalService(
	private val locationRepository: LocationRepository,
	private val profileRepository: RentalProfileRepository,
	private val mapper: RentalProfileMapper
) {
	@Transactional(readOnly = true)
	fun get(locationId: UUID): RentalProfileDto {
		val entity = profileRepository.findById(locationId).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "Rental profile not attached: $locationId")
		}
		return mapper.toDto(entity)
	}
	
	@Transactional
	fun attach(locationId: UUID, @Suppress("UNUSED_PARAMETER") dto: RentalProfileCreateDto): RentalProfileDto {
		if (profileRepository.existsByLocationId(locationId)) {
			throw ResponseStatusException(HttpStatus.CONFLICT, "Rental profile already exists")
		}
		val location: Location = locationRepository.findById(locationId).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found: $locationId")
		}
		val entity = RentalProfile(location = location)
		val saved = profileRepository.save(entity)
		return mapper.toDto(saved)
	}
	
	@Transactional
	fun patch(locationId: UUID, dto: RentalProfilePatchDto): RentalProfileDto {
		val entity = profileRepository.findById(locationId).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "Rental profile not attached: $locationId")
		}
		mapper.updateFromPatch(dto, entity)
		val saved = profileRepository.save(entity)
		return mapper.toDto(saved)
	}
	
	@Transactional
	fun detach(locationId: UUID) {
		if (!profileRepository.existsByLocationId(locationId)) {
			throw ResponseStatusException(HttpStatus.NOT_FOUND, "Rental profile not attached: $locationId")
		}
		profileRepository.deleteByLocationId(locationId)
	}
}
