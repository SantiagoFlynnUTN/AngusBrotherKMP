package com.angus.api_gateway.data.model.taxi

import kotlinx.serialization.Serializable
import com.angus.api_gateway.data.model.LocationDto

@Serializable
data class RideTrackingResponse(
    val id: String,
    val taxiPlateNumber: String,
    val taxiDriverName: String,
    val driverImage: String? = null,
    val carType: String? = null,
    val taxiColor: Long,
    val startPoint: LocationDto,
    val destination: LocationDto,
    val startPointAddress: String,
    val destinationAddress: String,
    val rate: Double? = null,
    val tripStatus: Int
)