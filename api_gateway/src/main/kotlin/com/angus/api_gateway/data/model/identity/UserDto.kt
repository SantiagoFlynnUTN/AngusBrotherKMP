package com.angus.api_gateway.data.model.identity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserDto(
    @SerialName("id") val id: String,
    @SerialName("fullName") val fullName: String,
    @SerialName("username") val username: String,
    @SerialName("country") val country: String,
    @SerialName("phone") val phone: String,
    @SerialName("email") val email: String,
    @SerialName("permission") val permission: Int,
)