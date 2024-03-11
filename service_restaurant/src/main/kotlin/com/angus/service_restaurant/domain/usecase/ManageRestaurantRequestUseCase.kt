package com.angus.service_restaurant.domain.usecase

import com.angus.service_restaurant.domain.entity.RestaurantPermissionRequest
import com.angus.service_restaurant.domain.gateway.IRestaurantGateway
import com.angus.service_restaurant.domain.utils.IValidation
import com.angus.service_restaurant.domain.utils.exceptions.INVALID_EMAIL
import com.angus.service_restaurant.domain.utils.exceptions.INVALID_NAME
import com.angus.service_restaurant.domain.utils.exceptions.MultiErrorException
import com.angus.service_restaurant.domain.utils.nullIfFalse

interface IManageRestaurantRequestUseCase {
    suspend fun createRestaurantRequest(form: RestaurantPermissionRequest): RestaurantPermissionRequest

    suspend fun getRestaurantRequests(): List<RestaurantPermissionRequest>
}

class ManageRestaurantRequestUseCase(
    private val restaurantGateway: IRestaurantGateway,
    private val validations: IValidation
) : IManageRestaurantRequestUseCase {
    override suspend fun createRestaurantRequest(form: RestaurantPermissionRequest): RestaurantPermissionRequest {
        validations.isValidName(form.restaurantName).nullIfFalse() ?: throw MultiErrorException(listOf(INVALID_NAME))
        validations.isValidEmail(form.ownerEmail).nullIfFalse() ?: throw MultiErrorException(listOf(INVALID_EMAIL))
        return restaurantGateway.createRestaurantPermissionRequest(form.restaurantName, form.ownerEmail, form.cause)
    }

    override suspend fun getRestaurantRequests(): List<RestaurantPermissionRequest> {
        return restaurantGateway.getRestaurantPermissionRequests()
    }
}



