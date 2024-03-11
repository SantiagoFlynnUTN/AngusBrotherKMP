package com.angus.service_restaurant.api.models

import kotlinx.serialization.Serializable

@Serializable
data class VariationsDto(
    val variations: List<String>? = emptyList(),
)