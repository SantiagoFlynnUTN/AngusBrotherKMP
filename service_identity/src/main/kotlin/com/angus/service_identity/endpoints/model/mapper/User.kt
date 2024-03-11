package com.angus.service_identity.endpoints.model.mapper

import com.angus.service_identity.domain.entity.Address
import com.angus.service_identity.domain.entity.User
import com.angus.service_identity.domain.entity.UserInfo
import com.angus.service_identity.endpoints.model.UserDto
import com.angus.service_identity.endpoints.model.UserRegistrationDto

fun User.toDto() = UserDto(
    id = id,
    fullName = fullName,
    username = username,
    email = email,
    phone = phone,
    walletBalance = walletBalance,
    currency = currency,
    country = country,
    addresses = addresses.toDto(),
    permission = permission
)

fun UserRegistrationDto.toEntity() = UserInfo(
    id = "",
    fullName = fullName,
    username = username,
    email = email,
    phone = phone,
    addresses = listOf(Address(id = "", address = address)),
)