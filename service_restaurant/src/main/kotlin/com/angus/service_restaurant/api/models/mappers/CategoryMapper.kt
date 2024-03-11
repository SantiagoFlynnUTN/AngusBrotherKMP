package com.angus.service_restaurant.api.models.mappers

import com.angus.service_restaurant.api.models.CategoryDetailsDto
import com.angus.service_restaurant.api.models.CategoryDto
import com.angus.service_restaurant.domain.entity.Category


fun CategoryDto.toEntity() = Category(id = id ?: "", name = name ?: "", image = image ?: "")

fun Category.toCategoryRestaurantsDto() = CategoryDetailsDto(
    id = id,
    name = name,
    image = image,
    restaurants = restaurants.toDto()
)

fun List<Category>.toCategoryRestaurantsDto(): List<CategoryDetailsDto> = map { it.toCategoryRestaurantsDto() }


fun Category.toDto() = CategoryDto(id = id, name = name, image = image)

fun List<Category>.toDto(): List<CategoryDto> = map { it.toDto() }