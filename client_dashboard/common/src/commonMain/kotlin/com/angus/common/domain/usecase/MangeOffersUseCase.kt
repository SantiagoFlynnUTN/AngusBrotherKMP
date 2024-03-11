package com.angus.common.domain.usecase

import com.angus.common.domain.entity.Offer
import com.angus.common.domain.getway.IRestaurantGateway

interface IMangeOffersUseCase {

    suspend fun createOffer(offerName: String,image:ByteArray): Offer

}

class MangeOffersUseCase(
    private val restaurantGateway: IRestaurantGateway
) : IMangeOffersUseCase {
    override suspend fun createOffer(offerName: String, image: ByteArray): Offer {
        return restaurantGateway.createOffer(offerName,image)
    }
}