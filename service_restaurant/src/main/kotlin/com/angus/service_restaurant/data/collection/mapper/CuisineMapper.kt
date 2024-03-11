package com.angus.service_restaurant.data.collection.mapper

import com.angus.service_restaurant.data.collection.CuisineCollection
import com.angus.service_restaurant.data.collection.relationModels.CuisinesMealDetails
import com.angus.service_restaurant.domain.entity.Cuisine

fun Cuisine.toCollection(): CuisineCollection = CuisineCollection(name = name, image = image)

fun CuisineCollection.toEntity(): Cuisine = Cuisine(id = id.toString(), name = name, image = image)

fun List<CuisineCollection>.toEntity(): List<Cuisine> = this.map { it.toEntity() }


fun CuisinesMealDetails.toCuisineMealsEntity() =
    Cuisine(id = id.toString(), name = name, image = image, meals = meals.toMealEntity())

fun List<CuisinesMealDetails>.toCuisineMealsEntity() = map { it.toCuisineMealsEntity() }