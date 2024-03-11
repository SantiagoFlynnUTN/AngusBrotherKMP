package com.angus.service_identity.endpoints.model.mapper

import com.angus.service_identity.domain.entity.UserOptions
import com.angus.service_identity.endpoints.model.UserOptionsDto

fun UserOptionsDto.toEntity() = UserOptions(
    page = page ?: 1,
    limit = limit ?: 10,
    query = query,
    permissions = permissions,
    country = country
)