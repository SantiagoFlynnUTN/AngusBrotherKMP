package com.angus.service_identity.endpoints.model.mapper

import com.angus.service_identity.domain.entity.User
import com.angus.service_identity.domain.entity.UserManagement
import com.angus.service_identity.endpoints.model.UserManagementDto

fun UserManagement.toDto() = UserManagementDto(
    id = id,
    fullName = fullName,
    username = username,
    email = email,
    country = country,
    phone = phone,
    permission = permission
)

fun List<UserManagement>.toDto() = map { it.toDto() }

fun UserManagement.toUserDetails(currency: String, walletBalance: Double) = User(
    id = id,
    fullName = fullName,
    username = username,
    email = email,
    country = country,
    phone = phone,
    permission = permission,
    currency = currency,
    walletBalance = walletBalance
)