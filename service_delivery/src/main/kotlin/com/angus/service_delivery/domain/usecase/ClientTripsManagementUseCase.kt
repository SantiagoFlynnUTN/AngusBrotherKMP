package com.angus.service_taxi.domain.usecase

import com.angus.service_taxi.domain.entity.Trip
import com.angus.service_taxi.domain.exceptions.*
import com.angus.service_taxi.domain.gateway.ITaxiGateway
import com.angus.service_taxi.domain.usecase.utils.IValidations

interface IClientTripsManagementUseCase {
    suspend fun getTripsByClientId(clientId: String, page: Int, limit: Int): List<Trip> // user
    suspend fun rateTrip(tripId: String, rate: Double): Trip // user
    suspend fun createTrip(trip: Trip): Trip // user
    suspend fun getNumberOfTripsByClientId(id: String): Long
}

class ClientTripsManagementUseCase(
    private val taxiGateway: ITaxiGateway,
    private val validations: IValidations
) : IClientTripsManagementUseCase {

    override suspend fun getTripsByClientId(clientId: String, page: Int, limit: Int): List<Trip> {
        return taxiGateway.getClientTripsHistory(clientId, page, limit)
    }

    override suspend fun rateTrip(tripId: String, rate: Double): Trip {
        taxiGateway.getTripById(tripId) ?: throw ResourceNotFoundException()
        if (!validations.isValidRate(rate)) throw MultiErrorException(listOf(INVALID_RATE))
        return taxiGateway.rateTrip(tripId, rate) ?: throw ResourceNotFoundException()
    }

    override suspend fun createTrip(trip: Trip): Trip {
        validationTrip(trip)
        return taxiGateway.addTrip(trip)?.also {
            it.taxiId?.let { id ->
                val count = taxiGateway.getTaxiById(id)?.tripsCount ?: 0
                taxiGateway.updateTaxiTripsCount(id, count + 1)
            }
        } ?: throw ResourceNotFoundException()
    }

    override suspend fun getNumberOfTripsByClientId(id: String): Long {
        return taxiGateway.getNumberOfTripsByClientId(id)
    }

    private fun validationTrip(trip: Trip) {
        val validationErrors = mutableListOf<Int>()

        if (!validations.isValidId(trip.id)) {
            validationErrors.add(INVALID_ID)
        }
        if (!validations.isValidId(trip.clientId)) {
            validationErrors.add(INVALID_ID)
        }
        trip.taxiId?.let {
            if (!validations.isValidId(it)) {
                validationErrors.add(INVALID_ID)
            }
        }
        trip.driverId?.let {
            if (!validations.isValidId(it)) {
                validationErrors.add(INVALID_ID)
            }
        }
        if (!validations.isValidLocation(trip.startPoint.latitude, trip.startPoint.longitude)) {
            validationErrors.add(INVALID_LOCATION)
        }
        trip.destination?.let {
            if (!validations.isValidLocation(it.latitude, it.longitude)) {
                validationErrors.add(INVALID_LOCATION)
            }
        }
        trip.rate?.let {
            if (!validations.isValidRate(it)) {
                validationErrors.add(INVALID_RATE)
            }
        }
        if (!validations.isValidPrice(trip.price)) {
            validationErrors.add(INVALID_PRICE)
        }
        if (trip.startDate != null && trip.endDate != null) {
            if (trip.startDate >= trip.endDate) {
                validationErrors.add(INVALID_DATE)
            }
        }
        if (trip.isATaxiTrip == null) {
            validationErrors.add(INVALID_REQUEST_PARAMETER)
        }
        trip.isATaxiTrip?.let { isATaxiTrip ->
            if (!isATaxiTrip && trip.restaurantId.isNullOrEmpty()) {
                validationErrors.add(INVALID_REQUEST_PARAMETER)
            }
            if (isATaxiTrip && !trip.restaurantId.isNullOrEmpty()) {
                validationErrors.add(INVALID_REQUEST_PARAMETER)
            }
        }
        if (validationErrors.isNotEmpty()) {
            throw MultiErrorException(validationErrors)
        }
    }
}