package org.affidtech.musdict.musdictapi.service

import org.affidtech.musdict.musdictapi.domain.ConcertVenueProfile
import org.affidtech.musdict.musdictapi.domain.Location
import org.affidtech.musdict.musdictapi.repository.ConcertVenueProfileRepository
import org.affidtech.musdict.musdictapi.repository.LocationRepository
import org.affidtech.musdict.musdictapi.web.ConcertVenueProfileMapper
import org.affidtech.musdict.musdictapi.web.dto.ConcertVenueProfileCreateDto
import org.affidtech.musdict.musdictapi.web.dto.ConcertVenueProfileDto
import org.affidtech.musdict.musdictapi.web.dto.ConcertVenueProfilePatchDto
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class ConcertVenueService(
	private val locationRepository: LocationRepository,
	private val profileRepository: ConcertVenueProfileRepository,
	private val mapper: ConcertVenueProfileMapper
) {
	
	@Transactional(readOnly = true)
	fun get(locationId: UUID): ConcertVenueProfileDto {
		val entity = profileRepository.findById(locationId).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "ConcertVenue profile not attached: $locationId")
		}
		return mapper.toDto(entity)
	}
	
	@Transactional
	fun attach(locationId: UUID, dto: ConcertVenueProfileCreateDto): ConcertVenueProfileDto {
		if (profileRepository.existsByLocationId(locationId)) {
			throw ResponseStatusException(HttpStatus.CONFLICT, "ConcertVenue profile already exists")
		}
		val location: Location = locationRepository.findById(locationId).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found: $locationId")
		}
		val entity = ConcertVenueProfile(
			location = location,
			capacity = dto.capacity,
			terms = dto.terms
		)
		val saved = profileRepository.save(entity)
		return mapper.toDto(saved)
	}
	
	@Transactional
	fun patch(locationId: UUID, dto: ConcertVenueProfilePatchDto): ConcertVenueProfileDto {
		val entity = profileRepository.findById(locationId).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "ConcertVenue profile not attached: $locationId")
		}
		mapper.updateFromPatch(dto, entity)
		val saved = profileRepository.save(entity)
		return mapper.toDto(saved)
	}
	
	@Transactional
	fun detach(locationId: UUID) {
		if (!profileRepository.existsByLocationId(locationId)) {
			throw ResponseStatusException(HttpStatus.NOT_FOUND, "ConcertVenue profile not attached: $locationId")
		}
		profileRepository.deleteByLocationId(locationId)
	}
}
