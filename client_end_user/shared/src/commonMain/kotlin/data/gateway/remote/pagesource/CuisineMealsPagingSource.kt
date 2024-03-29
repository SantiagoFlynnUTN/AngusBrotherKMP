package data.gateway.remote.pagesource

import domain.entity.Meal
import domain.entity.PaginationItems
import domain.gateway.IRestaurantGateway
import kotlin.properties.Delegates

class CuisineMealsPagingSource(
    private val remoteGateway: IRestaurantGateway,
) : BasePagingSource<Meal>() {
    private var cuisineId by Delegates.notNull<String>()

    fun initCuisine(id: String) {
        cuisineId = id
    }

    override suspend fun fetchData(page: Int, limit: Int): PaginationItems<Meal> {
        return remoteGateway.getMealsInCuisine(cuisineId, page, limit)
    }
}
