package org.affidtech.musdict.musdictapi.web.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.util.*

enum class LocationType {
	CONCERT_VENUE,
	REHEARSAL_BASE,
	STUDIO,
	RENTAL
}

data class LocationReadSummary(
	@field:NotNull
	val id: UUID,
	
	val types: List<LocationType> = emptyList(),
	
	
	@field:NotBlank
	@field:Size(min = 1, max = 200)
	val name: String,
	
	
	@field:Size(max = 1000)
	val cover: String? = null,
	
	
	@field:Valid
	val address: AddressReadSummary,
)


data class LocationReadDetail(
	@field:NotNull
	val id: UUID,
	
	val types: List<LocationType> = emptyList(),
	
	@field:NotBlank
	@field:Size(min = 1, max = 200)
	val name: String,
	
	
	@field:Size(max = 1000)
	val cover: String? = null,
	
	
	@field:Valid
	val address: AddressReadDetail,
	
	
	@field:Size(max = 5000)
	val description: String? = null,
	
	
	@field:Size(max = 2000)
	val contacts: String? = null,
)


// Writes: type profiles are separate resources; clients DO NOT send 'types'.


data class LocationCreate(
	@field:NotBlank
	@field:Size(min = 1, max = 200)
	val name: String,
	
	
	@field:Size(max = 1000)
	val cover: String? = null,
	
	
	/** Exactly one of [addressId] or [addressCreate] must be provided. */
	val addressId: UUID? = null,
	
	
	@field:Valid
	val addressCreate: AddressCreate? = null,
	
	
	@field:Size(max = 5000)
	val description: String? = null,
	
	
	@field:Size(max = 2000)
	val contacts: String? = null
) {
	@AssertTrue(message = "Exactly one of addressId or addressCreate must be provided")
	fun isExactlyOneAddressSourceProvided(): Boolean =
		(addressId != null) xor (addressCreate != null)
}

data class LocationUpdate(
	@field:Size(min = 1, max = 200)
	val name: String? = null,
	
	
	@field:Size(max = 1000)
	val cover: String? = null,
	
	
	/** If present, at most one of [addressId] or [addressCreate] may be provided. */
	val addressId: UUID? = null,
	
	
	@field:Valid
	val addressCreate: AddressCreate? = null,
	
	
	@field:Size(max = 5000)
	val description: String? = null,
	
	
	@field:Size(max = 2000)
	val contacts: String? = null
) {
	@AssertTrue(message = "At most one of addressId or addressCreate may be provided")
	fun isAtMostOneAddressSourceProvided(): Boolean =
		!(addressId != null && addressCreate != null)
}
