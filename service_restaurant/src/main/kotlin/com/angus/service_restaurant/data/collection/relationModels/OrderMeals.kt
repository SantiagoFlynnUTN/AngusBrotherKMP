package com.angus.service_restaurant.data.collection.relationModels

import kotlinx.serialization.Serializable
import com.angus.service_restaurant.data.collection.OrderCollection
import com.angus.service_restaurant.data.collection.RestaurantCollection

@Serializable
data class OrderMeals(
    val restaurants: RestaurantCollection,
    val orders: OrderCollection,
)
