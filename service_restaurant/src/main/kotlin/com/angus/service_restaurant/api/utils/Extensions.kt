package com.angus.service_restaurant.api.utils

import io.ktor.http.*
import java.time.LocalTime

fun Parameters.extractString(key: String): String? {
    return this[key]?.trim()?.takeIf { it.isNotEmpty() }
}

fun Parameters.extractInt(key: String): Int? {
    return this[key]?.toIntOrNull()
}

fun isRestaurantOpen(openTime: String, closeTime: String, currentTime: LocalTime = LocalTime.now()): Boolean {
    return currentTime.isAfter(LocalTime.parse(openTime)) && currentTime.isBefore(LocalTime.parse(closeTime))
}


fun String?.toListOfStringOrNull(): List<String>? {
    return takeIf { !it.isNullOrBlank() }?.run {
        val integerStrings = this.replace("[", "").replace("]", "").split(",")
        integerStrings.map(String::trim)
    }
}