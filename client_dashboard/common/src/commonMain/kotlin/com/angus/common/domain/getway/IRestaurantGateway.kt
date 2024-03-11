package com.angus.common.domain.getway

import com.angus.common.domain.entity.Cuisine
import com.angus.common.domain.entity.DataWrapper
import com.angus.common.domain.entity.Offer
import com.angus.common.domain.entity.RestaurantInformation
import com.angus.common.domain.entity.Restaurant

interface IRestaurantGateway {

    suspend fun createRestaurant(restaurant: RestaurantInformation): Restaurant

    suspend fun deleteRestaurant(id: String): Boolean

    suspend fun getCuisines(): List<Cuisine>
    suspend fun getOffers(): List<Offer>

    suspend fun createCuisine(cuisineName: String,image:ByteArray): Cuisine
    suspend fun deleteCuisine(cuisineId: String)
    suspend fun createOffer(offerName: String, image: ByteArray): Offer

    suspend fun getRestaurants(
        pageNumber: Int,
        numberOfRestaurantsInPage: Int,
        restaurantName: String,
        rating: Double,
        priceLevel: Int,
    ): DataWrapper<Restaurant>

    suspend fun getRestaurantById(id: String): Restaurant

    suspend fun updateRestaurant(
        restaurantId: String,
        ownerId: String,
        restaurant: RestaurantInformation
    ): Restaurant

}