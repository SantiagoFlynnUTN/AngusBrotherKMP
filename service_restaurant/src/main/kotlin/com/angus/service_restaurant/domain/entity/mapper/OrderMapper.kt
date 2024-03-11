package com.angus.service_restaurant.domain.entity.mapper

import com.angus.service_restaurant.domain.entity.Cart
import com.angus.service_restaurant.domain.entity.Order
import com.angus.service_restaurant.domain.utils.currentDateTime


fun Cart.toOrder() = Order(
    id = "",
    userId = userId,
    restaurantId = restaurantId ?: "",
    restaurantName = restaurantName ?: "",
    restaurantImage = restaurantImage ?: "",
    meals = meals ?: emptyList(),
    createdAt = currentDateTime(),
    status =  Order.Status.PENDING,
    totalPrice = totalPrice,
    currency = currency ?: ""
)