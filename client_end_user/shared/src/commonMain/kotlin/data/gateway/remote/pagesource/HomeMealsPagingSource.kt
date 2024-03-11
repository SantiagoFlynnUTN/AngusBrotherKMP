package data.gateway.remote.pagesource

import data.gateway.fake.FakeRestaurantGateway
import domain.entity.Meal
import domain.entity.PaginationItems
import domain.gateway.IRestaurantGateway

class HomeMealsPagingSource(
    private val remoteGateway: IRestaurantGateway,
) : BasePagingSource<Meal>() {

    override suspend fun fetchData(page: Int, limit: Int): PaginationItems<Meal> {
        return FakeRestaurantGateway().getMealsFromHomePage(page, limit)
    }
}
