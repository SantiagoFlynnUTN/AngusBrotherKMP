package com.angus.service_identity.domain.entity

import com.angus.service_identity.domain.util.Role

data class User(
    val id: String,
    val fullName: String,
    val username: String,
    val email: String,
    val phone: String,
    val walletBalance: Double,
    val currency: String,
    val addresses: List<Address> = emptyList(),
    val country: String,
    val permission: Int = Role.END_USER
)