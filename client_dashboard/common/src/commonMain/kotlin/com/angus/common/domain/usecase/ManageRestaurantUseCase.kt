package com.angus.common.domain.usecase

import com.angus.common.domain.entity.DataWrapper
import com.angus.common.domain.entity.RestaurantInformation
import com.angus.common.domain.entity.Restaurant
import com.angus.common.domain.getway.IRestaurantGateway


interface IManageRestaurantUseCase {

    suspend fun createRestaurant(restaurant: RestaurantInformation): Restaurant

    suspend fun deleteRestaurant(id: String): Boolean

    suspend fun updateRestaurant(
        restaurantId: String,
        ownerId: String,
        restaurant: RestaurantInformation
    ): Restaurant

}


class ManageRestaurantUseCase(private val restaurantGateway: IRestaurantGateway) :
    IManageRestaurantUseCase {

    override suspend fun createRestaurant(restaurant: RestaurantInformation): Restaurant {
        println("createRestaurant PASE")
        return restaurantGateway.createRestaurant(restaurant)
    }

    override suspend fun deleteRestaurant(id: String): Boolean {
        return restaurantGateway.deleteRestaurant(id)
    }

    override suspend fun updateRestaurant(
        restaurantId: String,
        ownerId: String,
        restaurant: RestaurantInformation
    ): Restaurant {
        return restaurantGateway.updateRestaurant(restaurantId, ownerId, restaurant)
    }

}