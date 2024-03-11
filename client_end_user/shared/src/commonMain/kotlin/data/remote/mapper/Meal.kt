package data.remote.mapper

import data.remote.model.MealDto
import domain.entity.Meal
import domain.entity.Price

fun MealDto.toEntity() = Meal(
    id = id ?: "",
    name = name ?: "",
    description = description ?: "",
    price = Price(price ?: 0.0, currency ?: ""),
    restaurantId = restaurantId ?: "",
    cuisines = cuisines?.toEntity() ?: emptyList(),
    imageUrl = image ?: "",
    restaurantName = restaurantName ?: "restaurant Name",
    variations = variations?.toEntity() ?: emptyList(),
)

fun List<MealDto>.toEntity() = map { it.toEntity() }
