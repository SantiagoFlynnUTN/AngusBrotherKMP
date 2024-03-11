package com.angus.service_identity.data.collection.mappers

import com.angus.service_identity.data.collection.DetailedUser
import com.angus.service_identity.domain.entity.User


fun DetailedUser.toEntity() = User(
    id = id.toString(),
    fullName = fullName,
    username = username,
    email = email,
    phone = phone,
    walletBalance = wallet.walletBalance,
    currency = wallet.currency,
    addresses = addresses.toEntity(),
    country = country,
    permission = permission,
)