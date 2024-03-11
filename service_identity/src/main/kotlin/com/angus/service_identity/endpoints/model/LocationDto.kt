package com.angus.service_identity.endpoints.model

import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(val latitude: Double, val longitude: Double)