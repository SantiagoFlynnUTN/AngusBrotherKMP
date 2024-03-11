package com.angus.service_identity.data.collection.mappers

import com.angus.service_identity.data.collection.UserCollection
import com.angus.service_identity.domain.entity.User
import com.angus.service_identity.domain.entity.UserInfo

fun UserInfo.toCollection(hash: String, salt: String, country: String) = UserCollection(
    username = username,
    fullName = fullName,
    phone = phone,
    email = email,
    country = country,
    hashedPassword = hash,
    salt = salt
)