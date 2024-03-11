package com.angus.service_restaurant.api.models.mappers


import com.angus.service_restaurant.api.models.OrderDto
import com.angus.service_restaurant.domain.entity.Order
import com.angus.service_restaurant.domain.utils.toMillis

fun Order.toDto() = OrderDto(
    id = id,
    userId = userId,
    restaurantId = restaurantId,
    restaurantName = restaurantName,
    restaurantImage = restaurantImage,
    meals = meals.toDto(),
    totalPrice = totalPrice,
    currency = currency,
    createdAt = createdAt.toMillis(),
    orderStatus = status?.statusCode ?: Order.Status.PENDING.statusCode
)






