package com.angus.api_gateway.data.model.identity

import kotlinx.serialization.Serializable
import com.angus.api_gateway.data.model.LocationDto

@Serializable
data class AddressDto(
    val id: String? = null,
    val location: LocationDto? = null,
    val address: String
)