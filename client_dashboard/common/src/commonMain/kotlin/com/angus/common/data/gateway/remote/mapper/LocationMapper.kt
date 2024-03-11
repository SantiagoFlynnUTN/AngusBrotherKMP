package com.angus.common.data.gateway.remote.mapper

import com.angus.common.data.gateway.remote.model.LocationInfoDto
import com.angus.common.domain.entity.LocationInfo

fun LocationInfoDto.toEntity() = LocationInfo(
    lat ?: 0.0,
    lon ?: 0.0,
    countryCode ?: "IQ"
)