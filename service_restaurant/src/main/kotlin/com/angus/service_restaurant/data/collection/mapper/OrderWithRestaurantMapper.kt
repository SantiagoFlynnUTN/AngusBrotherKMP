package com.angus.service_restaurant.data.collection.mapper

import com.angus.service_restaurant.data.collection.CartCollection
import com.angus.service_restaurant.data.collection.relationModels.OrderWithRestaurant
import com.angus.service_restaurant.domain.entity.Order
import com.angus.service_restaurant.domain.entity.OrderedMeal

fun OrderWithRestaurant.toHistoryEntity() = Order(
    id = id.toString(),
    userId = userId.toString(),
    restaurantId = restaurant.id.toString(),
    restaurantName = restaurant.name,
    restaurantImage = restaurant.restaurantImage,
    totalPrice = totalPrice,
    status = Order.Status.getOrderStatus(orderStatus),
    createdAt = createdAt,
    currency = restaurant.currency,
    meals = meals.map { it.toMealHistoryEntity() },
)

fun List<OrderWithRestaurant>.toHistoryEntity() = map { it.toHistoryEntity() }

fun CartCollection.MealCollection.toMealHistoryEntity() = OrderedMeal(
    meadId = mealId.toString(),
    quantity = quantity,
    image = image,
    name = name,
    price = price,
)

fun OrderWithRestaurant.toOrderEntity(): Order {
    return Order(
        id = id.toString(),
        userId = userId.toString(),
        restaurantId = restaurant.id.toString(),
        restaurantName = restaurant.name,
        restaurantImage = restaurant.restaurantImage,
        meals = meals.map { it.toMealEntity() },
        totalPrice = totalPrice,
        currency = restaurant.currency,
        createdAt = createdAt,
        status = Order.Status.getOrderStatus(orderStatus),
    )
}
