package com.angus.service_restaurant.data.collection.mapper

import org.bson.types.ObjectId
import com.angus.service_restaurant.data.collection.RestaurantCollection
import com.angus.service_restaurant.domain.entity.Restaurant
import java.util.*


fun RestaurantCollection.toEntity() = Restaurant(
    id = id.toString(),
    ownerUserName = ownerUserName?:"",
    ownerId = ownerId.toString(),
    name = name,
    restaurantImage = restaurantImage,
    description = description,
    priceLevel = priceLevel,
    rate = rate,
    phone = phone,
    openingTime = openingTime,
    closingTime = closingTime,
    address = address,
    currency = currency,
    location = location.toEntity()
)

fun List<RestaurantCollection>.toEntity(): List<Restaurant> = map { it.toEntity() }


fun Restaurant.toCollection() = RestaurantCollection(
    ownerId = ObjectId(ownerId),
    ownerUserName = ownerUserName,
    name = name,
    restaurantImage = restaurantImage,
    description = description,
    priceLevel = priceLevel,
    rate = rate,
    phone = phone,
    openingTime = openingTime,
    closingTime = closingTime,
    address = address,
    currency = currency,
    location = location.toCollection(),
)

