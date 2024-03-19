package com.angus.service_restaurant.data.collection.relationModels

import com.angus.service_restaurant.data.collection.CartCollection
import com.angus.service_restaurant.data.collection.RestaurantCollection
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class OrderWithRestaurant(
    @Contextual
    val id: ObjectId,
    @Contextual
    val userId: ObjectId,
    val restaurant: RestaurantCollection,
    val meals: List<CartCollection.MealCollection>,
    val totalPrice: Double,
    val createdAt: LocalDateTime,
    val orderStatus: Int
)