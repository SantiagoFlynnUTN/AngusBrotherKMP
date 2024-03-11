package com.angus.service_restaurant.domain.usecase.validation

import com.angus.service_restaurant.domain.entity.Category
import com.angus.service_restaurant.domain.utils.IValidation
import com.angus.service_restaurant.domain.utils.exceptions.INVALID_ID
import com.angus.service_restaurant.domain.utils.exceptions.INVALID_NAME
import com.angus.service_restaurant.domain.utils.exceptions.MultiErrorException

interface ICategoryValidationUseCase {
    fun validationCategory(category: Category)

}

class CategoryValidationUseCase(
    private val basicValidation: IValidation
) : ICategoryValidationUseCase {


    override fun validationCategory(category: Category) {
        val validationErrors = mutableListOf<Int>()

        if (!basicValidation.isValidId(category.id)) {
            validationErrors.add(INVALID_ID)
        }
        if (!basicValidation.isValidName(category.name)) {
            validationErrors.add(INVALID_NAME)
        }
        if (validationErrors.isNotEmpty()) {
            throw MultiErrorException(validationErrors)
        }
    }
}

