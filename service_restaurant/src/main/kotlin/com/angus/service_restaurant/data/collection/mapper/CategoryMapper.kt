package com.angus.service_restaurant.data.collection.mapper

import com.angus.service_restaurant.data.collection.CategoryCollection
import com.angus.service_restaurant.data.collection.relationModels.CategoryDetails
import com.angus.service_restaurant.domain.entity.Category

fun CategoryCollection.toEntity() = Category(
    id = id.toString(),
    name = name,
    image = image
)

fun CategoryDetails.categoryDetailsToEntity() = Category(
    id = id.toString(),
    name = name,
    restaurants = restaurants.toEntity(),
    image = image
)

fun List<CategoryCollection>.toEntity(): List<Category> = map { it.toEntity() }

fun List<CategoryDetails>.categoryDetailsToEntity(): List<Category> = map { it.categoryDetailsToEntity() }

fun Category.toCollection() = CategoryCollection(name = name, image = image)

