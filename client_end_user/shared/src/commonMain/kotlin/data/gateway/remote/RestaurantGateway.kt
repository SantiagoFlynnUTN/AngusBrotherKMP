package data.gateway.remote

import data.gateway.fake.FakeUserGateway
import data.remote.mapper.toCuisineEntity
import data.remote.mapper.toEntity
import data.remote.model.CuisineDto
import data.remote.model.MealDto
import data.remote.model.MealRestaurantDto
import data.remote.model.OfferDto
import data.remote.model.PaginationResponse
import data.remote.model.RestaurantDto
import data.remote.model.ServerResponse
import domain.entity.Cuisine
import domain.entity.Meal
import domain.entity.Offer
import domain.entity.PaginationItems
import domain.entity.Restaurant
import domain.gateway.IRestaurantGateway
import domain.utils.NotFoundException
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class RestaurantGateway(client: HttpClient) : BaseGateway(client = client), IRestaurantGateway {
    override suspend fun getRestaurants(page: Int, limit: Int): PaginationItems<Restaurant> {
        val result = tryToExecute<ServerResponse<PaginationResponse<RestaurantDto>>> {
            get("/restaurants") {
                parameter("page", page)
                parameter("limit", limit)
            }
        }.value
        return paginateData(
            result = result?.items?.map { it.toEntity() }
                ?: throw NotFoundException(""),
            page = result.page,
            total = result.total,
        )
    }

    override suspend fun getCuisines(): List<Cuisine> {
        return tryToExecute<ServerResponse<List<CuisineDto>>> {
            get("/cuisines")
        }.value?.toCuisineEntity() ?: throw NotFoundException("")
    }

    override suspend fun getRestaurantDetails(restaurantId: String): Restaurant {
        return tryToExecute<ServerResponse<RestaurantDto>> {
            get("/restaurant/$restaurantId")
        }.value?.toEntity() ?: throw NotFoundException("")
    }

    override suspend fun getMealById(mealId: String): Meal {
        // return FakeRestaurantGateway().getMealById(mealId)
        return tryToExecute<ServerResponse<MealDto>> {
            get("/meal/$mealId")
        }.value?.toEntity() ?: throw NotFoundException("")
    }

    override suspend fun getNewOffers(): List<Offer> {
        val userGateway = FakeUserGateway()
        return tryToExecute<ServerResponse<List<OfferDto>>> {
            get("/offers/restaurants")
        }.value?.toEntity()?.apply {
            forEach { it.restaurants.addAll(userGateway.getFavoriteRestaurants()) }
        } ?: throw NotFoundException("")
    }

    override suspend fun getCuisinesWithMealsInRestaurant(restaurantId: String): List<Cuisine> {
        return tryToExecute<ServerResponse<List<CuisineDto>>> {
            get("/restaurant/$restaurantId/cuisineMeals")
        }.value?.toCuisineEntity() ?: throw NotFoundException("")
    }

    override suspend fun search(query: String): Pair<List<Restaurant>, List<Meal>> {
        val result = tryToExecute<ServerResponse<MealRestaurantDto>> {
            get("/restaurants/search?query=$query")
        }.value ?: throw NotFoundException("")
        return Pair(result.restaurants.toEntity(), result.meals.toEntity())
    }

    override suspend fun getMealsInCuisine(
        cuisineId: String,
        page: Int,
        limit: Int,
    ): PaginationItems<Meal> {
        val result = tryToExecute<ServerResponse<PaginationResponse<MealDto>>> {
            get("/cuisine/$cuisineId/meals") {
                parameter("page", page)
                parameter("limit", limit)
            }
        }.value
        return paginateData(
            result = result?.items?.map { it.toEntity() }
                ?: throw NotFoundException(""),
            page = result.page,
            total = result.total,
        )
    }

    override suspend fun getMealsFromHomePage(page: Int, limit: Int): PaginationItems<Meal> {
        TODO("Not yet implemented")
    }
}
