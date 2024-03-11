package com.angus.service_identity.endpoints.model

import kotlinx.serialization.Serializable

@Serializable
data class AddressDto(
    val id: String? = null,
    val location: LocationDto? = null,
    val address: String
)