package com.angus.api_gateway.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PaginationResponse<T>(
    val items: List<T>,
    val page: Int,
    val total: Long
)