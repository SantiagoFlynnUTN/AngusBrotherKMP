package com.angus.service_identity.endpoints.model.mapper

import com.angus.service_identity.domain.entity.Location
import com.angus.service_identity.endpoints.model.LocationDto

fun Location.toDto() = LocationDto(
    latitude = latitude,
    longitude = longitude,
)