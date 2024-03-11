package com.angus.service_restaurant.data.collection.relationModels

import kotlinx.serialization.Serializable
import com.angus.service_restaurant.data.collection.CuisineCollection
import com.angus.service_restaurant.data.collection.MealCollection


@Serializable
data class MealCuisines(
    val cuisines: List<CuisineCollection> = emptyList(),
    val meals: List<MealCollection> = emptyList()
)