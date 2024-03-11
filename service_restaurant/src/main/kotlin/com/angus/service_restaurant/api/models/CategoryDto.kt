package com.angus.service_restaurant.api.models

import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    val id: String? = null,
    val name: String? = null,
    val image: String? = null,
)