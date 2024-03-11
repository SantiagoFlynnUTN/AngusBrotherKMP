package com.angus.service_restaurant.data.collection.relationModels

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import com.angus.service_restaurant.data.collection.RestaurantCollection
import com.angus.service_restaurant.data.collection.VariationCollection
import com.angus.service_restaurant.domain.entity.Meal

@Serializable
data class MealWithRestaurant(
    @Contextual
    val id: ObjectId,
    val restaurant: RestaurantCollection,
    val name: String,
    val description: String,
    val price: Double,
    val currency: String,
    val image: String,
    val variations: List<VariationCollection>
)