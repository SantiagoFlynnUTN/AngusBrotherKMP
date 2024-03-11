package com.angus.service_restaurant.api.models

import kotlinx.serialization.Serializable

@Serializable
data class RestaurantDetailsDto(
    val id: String,
    val ownerId:String,
    val ownerUserName: String,
    val name: String,
    val restaurantImage: String?,
    val description: String?,
    val priceLevel: String?,
    val rate: Double?,
    val phone: String,
    val openingTime: String,
    val closingTime: String,
    val address: String,
    val location: LocationDto,
)