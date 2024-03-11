package com.angus.common.domain.getway

import com.angus.common.domain.entity.LocationInfo

interface ILocationGateway {
    suspend fun getCurrentLocation(): LocationInfo

    suspend fun getIpAddress(): String
}