package com.angus.service_restaurant.api.models.mappers

import com.angus.service_restaurant.api.models.RestaurantPermissionRequestDto
import com.angus.service_restaurant.domain.entity.RestaurantPermissionRequest

fun RestaurantPermissionRequestDto.toEntity(): RestaurantPermissionRequest {
    return RestaurantPermissionRequest(
        id = this.id ?: "",
        restaurantName = this.restaurantName ?: "",
        ownerEmail = this.ownerEmail ?: "",
        cause = this.cause ?: "",
    )
}

fun RestaurantPermissionRequest.toDto(): RestaurantPermissionRequestDto {
    return RestaurantPermissionRequestDto(
        id = this.id,
        restaurantName = this.restaurantName,
        ownerEmail = this.ownerEmail,
        cause = this.cause,
    )
}

fun List<RestaurantPermissionRequest>.toDto(): List<RestaurantPermissionRequestDto> = map { it.toDto() }
