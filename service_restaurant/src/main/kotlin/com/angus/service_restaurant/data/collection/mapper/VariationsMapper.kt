package com.angus.service_restaurant.data.collection.mapper

import com.angus.service_restaurant.data.collection.VariationCollection
import com.angus.service_restaurant.domain.entity.Variations

fun List<Variations>.toCollection() = map { it.toCollection() }

fun List<VariationCollection>.toEntity() = map { it.toEntity() }

fun Variations.toCollection() =
    VariationCollection(
        variations = variations
    )

fun VariationCollection.toEntity() =
    Variations(
        variations = variations,
    )