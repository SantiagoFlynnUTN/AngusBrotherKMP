package com.angus.service_restaurant.data.collection.mapper

import com.angus.service_restaurant.data.collection.LocationCollection
import com.angus.service_restaurant.domain.entity.Location

fun LocationCollection.toEntity(): Location {
    return Location(
        latitude = latitude,
        longitude = longitude,
    )
}

fun Location.toCollection() = LocationCollection(
    latitude = latitude,
    longitude = longitude,
)
