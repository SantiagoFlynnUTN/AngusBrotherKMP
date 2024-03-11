package com.angus.service_restaurant.api.models

import kotlinx.serialization.Serializable

@Serializable
data class BasePaginationResponseDto<T>(
    val items: List<T>,
    val page: Int,
    val total: Long
)