package com.angus.common.data.gateway.remote.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class RestaurantResponse(
    @SerializedName("items")
    val restaurants: List<RestaurantDto>,
    @SerializedName("total")
    val total: Int,
)

@Serializable
data class RestaurantDto(
    @SerialName("id")
    val id: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("ownerId")
    val ownerId: String? = null,
    @SerialName("ownerUserName")
    val ownerUserName: String? = null,
    @SerialName("phone")
    val phone: String? = null,
    @SerialName("rate")
    val rate: Double? = null,
    @SerialName("price_level")
    val priceLevel: String? = null,
    @SerialName("openingTime")
    val openingTime: String? = null,
    @SerialName("closingTime")
    val closingTime: String? = null,
    @SerialName("address")
    val address: String? = null,
    @SerialName("location")
    val location: LocationDto? = null,
)

@Serializable
data class LocationDto(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
)


