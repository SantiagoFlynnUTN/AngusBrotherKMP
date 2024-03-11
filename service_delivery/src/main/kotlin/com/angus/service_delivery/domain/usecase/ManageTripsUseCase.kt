package com.angus.service_taxi.domain.usecase

import com.angus.service_taxi.domain.entity.Trip
import com.angus.service_taxi.domain.exceptions.ResourceNotFoundException
import com.angus.service_taxi.domain.gateway.ITaxiGateway

interface IManageTripsUseCase {
    suspend fun getTrips(page: Int, limit: Int): List<Trip> // admin
    suspend fun getTripById(tripId: String): Trip // admin
    suspend fun getTripByOrderId(orderId: String): Trip // admin
}

class ManageTripsUseCase(private val taxiGateway: ITaxiGateway) : IManageTripsUseCase {
    override suspend fun getTrips(page: Int, limit: Int): List<Trip> {
        return taxiGateway.getAllTrips(page, limit)
    }

    override suspend fun getTripById(tripId: String): Trip {
        return taxiGateway.getTripById(tripId) ?: throw ResourceNotFoundException()
    }

    override suspend fun getTripByOrderId(orderId: String): Trip {
        return taxiGateway.getTripByOrderId(orderId) ?: throw ResourceNotFoundException()
    }
}