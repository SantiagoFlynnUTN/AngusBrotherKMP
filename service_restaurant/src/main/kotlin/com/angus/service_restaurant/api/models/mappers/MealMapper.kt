package com.angus.service_restaurant.api.models.mappers

import com.angus.service_restaurant.api.models.MealDetailsDto
import com.angus.service_restaurant.api.models.MealDto
import com.angus.service_restaurant.api.models.MealWithCuisineDto
import com.angus.service_restaurant.api.models.VariationsDto
import com.angus.service_restaurant.domain.entity.Cuisine
import com.angus.service_restaurant.domain.entity.Meal
import com.angus.service_restaurant.domain.entity.MealDetails
import com.angus.service_restaurant.domain.entity.Variations
import com.angus.service_restaurant.domain.utils.Validation.Companion.NULL_DOUBLE

fun MealDetails.toDto() = MealDetailsDto(
    id = id,
    restaurantId = restaurantId,
    name = name,
    description = description,
    price = price,
    currency = currency,
    cuisines = cuisines.toDto(),
    image = image,
    variations = variations?.toVariationsDto()
)

fun MealWithCuisineDto.toEntity() = MealDetails(
    id = id ?: "",
    restaurantName = restaurantName ?: "",
    restaurantId = restaurantId ?: "",
    name = name ?: "",
    description = description ?: "",
    price = price ?: NULL_DOUBLE,
    currency = currency ?: "",
    cuisines = cuisines?.map { Cuisine(id = it, name = "", image = image ?: "") } ?: emptyList(),
    image = image ?: "",
    variations = variations?.map { Variations(variations = it.variations ?: emptyList()) } ?: emptyList(),

)

fun Meal.toDto() = MealWithCuisineDto(
    id = id,
    restaurantId = restaurantId,
    restaurantName = restaurantName,
    name = name,
    description = description,
    price = price,
    currency = currency,
    image = image,
    variations = variations?.toVariationsDto()
)

fun Meal.toMealDto() = MealDto(
    id = id,
    restaurantId = restaurantId,
    restaurantName = restaurantName,
    name = name,
    description = description,
    price = price,
    currency = currency,
    image = image,
    variations = variations?.toVariationsDto()
)

fun List<Meal>.toDto() = map { it.toDto() }

fun List<Meal>.toMealDto() = map { it.toMealDto() }

fun Variations.toVariationsDto() = VariationsDto(variations = variations)

fun List<Variations>.toVariationsDto() = map { it.toVariationsDto() }

