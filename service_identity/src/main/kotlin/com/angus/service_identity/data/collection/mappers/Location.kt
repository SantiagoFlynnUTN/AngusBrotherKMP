package com.angus.service_identity.data.collection.mappers

import com.angus.service_identity.data.collection.LocationCollection
import com.angus.service_identity.domain.entity.Location
import com.angus.service_identity.endpoints.model.LocationDto

fun LocationCollection.toEntity() = Location(
    latitude = latitude,
    longitude = longitude,
)

fun LocationDto.toEntity() = Location(
    latitude = latitude,
    longitude = longitude,
)

fun Location.toCollection() = LocationCollection(
    latitude = latitude,
    longitude = longitude,
)
