package com.angus.api_gateway.data.model.restaurant

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CuisineDto(
    @SerialName("id") val id: String? = null,
    @SerialName("name") val name: String,
    @SerialName("image") val image: String? = null
)