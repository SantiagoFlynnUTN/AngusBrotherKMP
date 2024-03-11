package com.angus.service_restaurant.domain.entity

data class Cuisine(
    val id: String,
    val name: String,
    val image: String,
    val meals: List<Meal> = emptyList()
)
