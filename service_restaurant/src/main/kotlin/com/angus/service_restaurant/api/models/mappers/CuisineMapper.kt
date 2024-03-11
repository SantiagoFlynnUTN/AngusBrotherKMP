package com.angus.service_restaurant.api.models.mappers

import com.angus.service_restaurant.api.models.CuisineDetailsDto
import com.angus.service_restaurant.api.models.CuisineDto
import com.angus.service_restaurant.domain.entity.Cuisine

fun Cuisine.toDto(): CuisineDto = CuisineDto(id, name,image)

fun List<Cuisine>.toDto(): List<CuisineDto> = this.map { it.toDto() }

fun CuisineDto.toEntity(): Cuisine = Cuisine(id ?: "", name ?: "", image ?: "")

fun Cuisine.toCuisineDetailsDto() = CuisineDetailsDto(id, name, image = image, meals = meals.toMealDto())

fun List<Cuisine>.toCuisineDetailsDto() = map { it.toCuisineDetailsDto() }