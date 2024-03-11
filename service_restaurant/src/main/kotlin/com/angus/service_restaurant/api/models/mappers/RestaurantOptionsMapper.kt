package com.angus.service_restaurant.api.models.mappers

import com.angus.service_restaurant.api.models.RestaurantOptionsDto
import com.angus.service_restaurant.domain.entity.RestaurantOptions

fun RestaurantOptionsDto.toEntity() = RestaurantOptions(
    page = page ?: 10,
    limit = limit ?: 10,
    query = query,
    rating = rating,
    priceLevel = priceLevel
)