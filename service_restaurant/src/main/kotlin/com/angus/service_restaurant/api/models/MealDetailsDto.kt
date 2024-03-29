package com.angus.service_restaurant.api.models

import kotlinx.serialization.Serializable

@Serializable
data class MealDetailsDto(
    val id: String? = null,
    val restaurantId: String? = null,
    val name: String? = null,
    val description: String? = null,
    val price: Double? = null,
    val currency: String? = null,
    val cuisines: List<CuisineDto>? = null,
    val image : String? = null,
    val variations: List<VariationsDto>? = null
)
