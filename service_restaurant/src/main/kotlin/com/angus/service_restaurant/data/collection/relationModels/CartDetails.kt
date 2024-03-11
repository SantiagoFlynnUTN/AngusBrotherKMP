package com.angus.service_restaurant.data.collection.relationModels

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import com.angus.service_restaurant.data.collection.CartCollection
import com.angus.service_restaurant.data.collection.RestaurantCollection

@Serializable
data class CartDetails(
    @Contextual
    val id: ObjectId,
    @Contextual
    val userId: ObjectId,
    @Contextual
    val restaurant: RestaurantCollection? = null,
    val meals: List<CartCollection.MealCollection> = mutableListOf(),
)