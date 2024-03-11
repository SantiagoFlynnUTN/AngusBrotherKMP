package com.angus.service_restaurant.domain.usecase.validation

import com.angus.service_restaurant.domain.utils.IValidation
import com.angus.service_restaurant.domain.utils.exceptions.INVALID_ID
import com.angus.service_restaurant.domain.utils.exceptions.INVALID_QUANTITY
import com.angus.service_restaurant.domain.utils.exceptions.MultiErrorException

interface ICartValidationUseCase {
    fun validateUpdateCart(userId: String, restaurantId: String, mealId: String, quantity: Int)

}

class CartValidationUseCase(private val basicValidation: IValidation) : ICartValidationUseCase {
    override fun validateUpdateCart(userId: String, restaurantId: String, mealId: String, quantity: Int) {
        val validationErrors = mutableListOf<Int>()

        if (!basicValidation.isValidId(userId) ||
            !basicValidation.isValidId(restaurantId) ||
            !basicValidation.isValidId(mealId)
        ) {
            validationErrors.add(INVALID_ID)
        }

        if (quantity < 0) {
            validationErrors.add(INVALID_QUANTITY)
        }

        if (validationErrors.isNotEmpty()) {
            throw MultiErrorException(validationErrors)
        }
    }

}