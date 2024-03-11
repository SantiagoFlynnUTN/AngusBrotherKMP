package com.angus.service_restaurant.api.models.mappers

import com.angus.service_restaurant.api.models.RestaurantDetailsDto
import com.angus.service_restaurant.api.models.RestaurantDto
import com.angus.service_restaurant.domain.entity.Restaurant

fun RestaurantDto.toEntity() = Restaurant(
    id = id ?: "",
    ownerId = ownerId,
    ownerUserName = ownerUserName ?: "",
    name = name ?: "",
    restaurantImage = restaurantImage ?: "",
    description = description,
    priceLevel = priceLevel,
    rate = rate,
    phone = phone ?: "",
    openingTime = openingTime ?: "",
    closingTime = closingTime ?: "",
    address = address ?: "",
    currency = "",
    location = location.toEntity(),
)

fun Restaurant.toDto() = RestaurantDto(
    id = id,
    ownerId = ownerId,
    ownerUserName = ownerUserName,
    name = name,
    restaurantImage = restaurantImage,
    description = description,
    priceLevel = priceLevel,
    rate = rate,
    phone = phone,
    openingTime = openingTime,
    closingTime = closingTime,
    address = address,
    location = location.toDto(),
)

fun Restaurant.toDetailsDto() = RestaurantDetailsDto(
    id = id,
    name = name,
    restaurantImage = restaurantImage,
    ownerId = ownerId,
    ownerUserName = ownerUserName,
    description = description,
    priceLevel = priceLevel,
    rate = rate,
    phone = phone,
    openingTime = openingTime,
    closingTime = closingTime,
    address = address,
    location = location.toDto()
)

fun List<Restaurant>.toDto(): List<RestaurantDto> = map { it.toDto() }