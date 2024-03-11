package com.angus.service_identity.domain.entity

data class UserManagement(
    val id: String,
    val fullName: String,
    val username: String,
    val email: String,
    val country: String,
    val phone: String,
    val permission: Int
)
