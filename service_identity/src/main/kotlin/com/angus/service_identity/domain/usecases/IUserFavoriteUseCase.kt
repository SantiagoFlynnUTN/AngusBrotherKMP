package com.angus.service_identity.domain.usecases

import org.koin.core.annotation.Single
import com.angus.service_identity.domain.gateway.IDataBaseGateway
import com.angus.service_identity.domain.util.ALREADY_ID_FAVORITE
import com.angus.service_identity.domain.util.ERROR_IN_DB
import com.angus.service_identity.domain.util.NOT_FOUND
import com.angus.service_identity.domain.util.ResourceNotFoundException

interface IUserFavoriteUseCase {
    suspend fun getFavoriteRestaurants(userId: String): List<String>

    suspend fun addRestaurantsToFavorite(userId: String, restaurantId: String): Boolean

    suspend fun removeRestaurantsFromFavorite(userId: String, restaurantId: String): Boolean
}

@Single
class UserFavoriteUseCase(private val dataBaseGateway: IDataBaseGateway) : IUserFavoriteUseCase {
    override suspend fun getFavoriteRestaurants(userId: String): List<String> {
        return dataBaseGateway.getFavoriteRestaurants(userId)
    }

    override suspend fun addRestaurantsToFavorite(userId: String, restaurantId: String): Boolean {
        return if (dataBaseGateway.addToFavorite(userId, restaurantId)) {
            true
        } else {
            throw ResourceNotFoundException(ALREADY_ID_FAVORITE)
        }
    }

    override suspend fun removeRestaurantsFromFavorite(userId: String, restaurantId: String): Boolean {
        return if (dataBaseGateway.deleteFromFavorite(userId, restaurantId)) {
            true
        } else {
            throw ResourceNotFoundException(NOT_FOUND)
        }
    }


}