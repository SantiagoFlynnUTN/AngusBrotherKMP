package com.angus.service_restaurant.data.collection.relationModels

import kotlinx.serialization.Serializable
import com.angus.service_restaurant.data.collection.CuisineCollection

@Serializable
data class RestaurantCuisine(
    val cuisines: MutableList<CuisineCollection> = mutableListOf(),
)