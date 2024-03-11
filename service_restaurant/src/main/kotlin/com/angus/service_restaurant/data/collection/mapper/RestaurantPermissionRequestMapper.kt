package com.angus.service_restaurant.data.collection.mapper

import com.angus.service_restaurant.data.collection.RestaurantPermissionRequestCollection
import com.angus.service_restaurant.domain.entity.RestaurantPermissionRequest

fun RestaurantPermissionRequestCollection.toEntity() = RestaurantPermissionRequest(
    id = id.toString(),
    restaurantName = restaurantName,
    ownerEmail = ownerEmail,
    cause = cause,
)

fun List<RestaurantPermissionRequestCollection>.toEntity(): List<RestaurantPermissionRequest> = map { it.toEntity() }
