package com.angus.common.domain.entity

data class Restaurant(
    val id: String,
    val name: String,
    val ownerId: String,
    val ownerUsername: String,
    val phone: String,
    val rate: Double,
    val priceLevel: String,
    val openingTime: String,
    val closingTime: String,
    val location: Location
)

data class Location(
    val latitude: Double,
    val longitude: Double
)
