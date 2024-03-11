package com.angus.common.data.gateway.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OfferDto(
    @SerialName("id")
    val id: String="",
    @SerialName("name")
    val name: String="",
    @SerialName("image")
    val image: String=""
)
