package com.angus.service_notification.domain.entity

data class User(
    val id: String,
    val deviceTokens: List<String>,
)

