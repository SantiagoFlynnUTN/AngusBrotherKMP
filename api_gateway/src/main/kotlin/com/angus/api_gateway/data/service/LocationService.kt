package com.angus.api_gateway.data.service

import io.ktor.client.*
import io.ktor.util.*
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single
import com.angus.api_gateway.data.model.LocationDto
import com.angus.api_gateway.data.utils.tryToExecuteWebSocket
import com.angus.api_gateway.data.utils.tryToSendWebSocketData
import com.angus.api_gateway.util.APIs
import org.koin.core.annotation.Named

@Single
class LocationService(
    @Named("location_client") private val client: HttpClient,
    private val attributes: Attributes,
) {
    suspend fun sendLocation(location: LocationDto, tripId: String) {
        client.tryToSendWebSocketData(
            data = location,
            api = APIs.LOCATION_API,
            attributes = attributes,
            path = "/location/sender/$tripId"
        )
    }

    suspend fun receiveLocation(tripId: String): Flow<LocationDto> {
        return client.tryToExecuteWebSocket<LocationDto>(
            api = APIs.LOCATION_API,
            attributes = attributes,
            path = "/location/receiver/$tripId",
        )
    }
}
