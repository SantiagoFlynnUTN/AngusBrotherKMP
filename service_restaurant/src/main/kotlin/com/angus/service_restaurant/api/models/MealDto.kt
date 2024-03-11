package com.angus.service_restaurant.api.models

import kotlinx.serialization.Serializable

@Serializable
data class MealDto(
    val id: String? = null,
    val restaurantId: String? = null,
    val restaurantName: String? = null,
    val name: String? = null,
    val description: String? = null,
    val price: Double? = null,
    val currency: String? = null,
    val image: String? = null,
    val variations: List<VariationsDto>? = null
)
