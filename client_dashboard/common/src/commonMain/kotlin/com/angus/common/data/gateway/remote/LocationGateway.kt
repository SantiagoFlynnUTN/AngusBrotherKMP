package com.angus.common.data.gateway.remote

import io.ktor.client.*
import io.ktor.client.request.*
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject
import com.angus.common.data.gateway.remote.mapper.toEntity
import com.angus.common.data.gateway.remote.model.LocationInfoDto
import com.angus.common.domain.entity.LocationInfo
import com.angus.common.domain.getway.ILocationGateway

class LocationGateway : BaseGateway(), ILocationGateway {
    private val client: HttpClient by inject(qualifier = named("locationClient"), clazz = HttpClient::class.java)

    override suspend fun getCurrentLocation(): LocationInfo {
        val ipAddress = getIpAddress()
        return tryToExecute<LocationInfoDto>(client) {
            get(urlString = "http://ip-api.com/json/$ipAddress")
        }.toEntity()
    }

    override suspend fun getIpAddress(): String {
        return tryToExecute<String>(client) {
            get(urlString = "https://api.ipify.org")
        }
    }
}