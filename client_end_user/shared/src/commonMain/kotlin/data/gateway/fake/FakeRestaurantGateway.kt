package data.gateway.fake

import data.remote.mapper.toEntity
import data.remote.model.MealDto
import data.remote.model.OfferDto
import domain.entity.Cuisine
import domain.entity.Meal
import domain.entity.Offer
import domain.entity.PaginationItems
import domain.entity.Price
import domain.entity.Restaurant
import domain.entity.Taxi
import domain.gateway.IRestaurantGateway

class FakeRestaurantGateway : IRestaurantGateway {
    override suspend fun getRestaurants(page: Int, limit: Int): PaginationItems<Restaurant> {
        TODO("Not yet implemented")
    }

    override suspend fun getCuisines(): List<Cuisine> {
        TODO("Not yet implemented")
    }

    override suspend fun getRestaurantDetails(restaurantId: String): Restaurant {
        TODO("Not yet implemented")
    }

    override suspend fun getMealById(mealId: String): Meal {
        return meals.first().toEntity()
    }

    override suspend fun getNewOffers(): List<Offer> {
        val userGateway = FakeUserGateway() // Initialize outside the loop to avoid repeated initializations
        return offers.map { offer ->
            offer.toEntity().apply {
                restaurants.addAll(userGateway.getFavoriteRestaurants())
            }
        }
    }

    override suspend fun getCuisinesWithMealsInRestaurant(restaurantId: String): List<Cuisine> {
        TODO("Not yet implemented")
    }


    override suspend fun search(query: String): Pair<List<Restaurant>, List<Meal>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMealsInCuisine(cuisineId: String, page: Int, limit: Int): PaginationItems<Meal> {
        return PaginationItems(emptyList(), 0, 0)
    }

    private val mockMeals = emptyList<Meal>() /*listOf(
        Meal("1", "Pasta Carbonara", "Creamy pasta with bacon and parmesan.", "R1", "Italian Bistro", emptyList(), "https://app.pedidosbcn.com/img/productos/00006209.jpg?Tuesday", Price(15.99, "USD")),
        Meal("2", "Margherita Pizza", "Classic pizza with tomatoes, mozzarella, and basil.", "R2", "Napoli Pizza", emptyList(), "https://app.pedidosbcn.com/img/productos/00006209.jpg?Tuesday", Price(12.99, "USD")),
        Meal("3", "Sushi Platter", "Assortment of sushi and sashimi.", "R3", "Sakura Sushi", emptyList(), "https://app.pedidosbcn.com/img/productos/00006209.jpg?Tuesday", Price(24.99, "USD")),
        Meal("4", "Vegan Burger", "Plant-based burger with all the fixings.", "R4", "Green Eats", emptyList(), "https://app.pedidosbcn.com/img/productos/00006209.jpg?Tuesday", Price(11.99, "USD")),
        Meal("5", "Chicken Tikka Masala", "Grilled chicken in a spiced curry sauce.", "R5", "Taj Mahal", emptyList(), "https://app.pedidosbcn.com/img/productos/00006209.jpg?Tuesday", Price(13.99, "USD")),
        Meal("6", "Beef Tacos", "Three tacos with marinated beef and fresh salsa.", "R6", "Mexican Fiesta", emptyList(), "https://app.pedidosbcn.com/img/productos/00006209.jpg?Tuesday", Price(9.99, "USD")),
        Meal("7", "Pad Thai", "Stir-fried rice noodles with shrimp, peanuts, egg, and bean sprouts.", "R7", "Bangkok Street", emptyList(), "https://app.pedidosbcn.com/img/productos/00006209.jpg?Tuesday", Price(14.99, "USD")),
        Meal("8", "Lamb Gyro", "Sliced lamb with tzatziki sauce, onions, and tomatoes.", "R8", "Greek Corner", emptyList(), "https://app.pedidosbcn.com/img/productos/00006209.jpg?Tuesday", Price(8.99, "USD"))
    )*/

    override suspend fun getMealsFromHomePage(page: Int, limit: Int): PaginationItems<Meal> {
        val startIndex = (page - 1) * limit
        val endIndex = kotlin.math.min(startIndex + limit, mockMeals.size)
        val pageItems = mockMeals.subList(startIndex, endIndex)
        return PaginationItems(pageItems, mockMeals.size, page.toLong())
    }

    private val offers = listOf(
        OfferDto(
            "000-0d-d0-d0--00d0d0d",
            "https://app.pedidosbcn.com/img/productos/00006209.jpg?Saturday",

            image = "https://app.pedidosbcn.com/img/productos/00006209.jpg?Saturday"
        ),
        OfferDto(
            "000-0d-d0-d0--00d0d0dacsccs",
            "https://app.pedidosbcn.com/img/productos/00006212.jpg?Saturday",
            image = "https://app.pedidosbcn.com/img/productos/00006212.jpg?Saturday"
        ),
        OfferDto(
            "000-0casscac--c-s-cs",
            "https://app.pedidosbcn.com/img/productos/00006218.jpg?Saturday",
            image = "https://app.pedidosbcn.com/img/productos/00006218.jpg?Saturday"
        ),
        OfferDto(
            "000-0wdwdwdww-d-w-d-wd-dw",
            "https://app.pedidosbcn.com/img/productos/00006313.jpg?Saturday",
            image = "https://app.pedidosbcn.com/img/productos/00006313.jpg?Saturday"
        )
    )

    private fun getTaxiOnTheWay(): List<Taxi> {
        return listOf(
            Taxi(
                id = "khhfhdfhd",
                color = "White",
                plate = "1234BC",
                timeToArriveInMints = 30,
            )
        )
    }


    private val meals = listOf(
        MealDto(
            id = "000-0d-d0-d0--00d0d0d",
            name = "Burger",
            price = 10.0,
            currency = "$",
            description = "nice meal",
            image = "https://s3-alpha-sig.figma.com/img/74ff/c283/fb93589c0fb95bffae890164ec2aba74?Expires=1695600000&Signature=cSB9yZulMLy-7VnhobwW3PavUWML0c5jJopRFJuz1jC2fWvHa~32qyGcqlHAMIPJTDIk~GOkzIv3UnVGKWJ4Zf75xvqJEF7jx6XTWeO5oECoRidQzbF7oY73ubLtarmWRqlqiUz1-~PrXkMq1r38ba~XvOd80~08EJ5MjGVcwsGnClS06Kstl0lQa9KqiLkMW02GLYlTKIF89ANlAjMcKkcCsVsnYcqemMRqa96JjCuMM5g~Fhpfd0LkY9akixUM1P9ixDoZ7AYNWfjnWSgHAqq4Cr~ZRAP4vuCxzekVbcG8I3xT8I5JsUXbsLG0EO3UnqNE2feBRRgfx1sOG13qwg__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4"
        ),
        MealDto(
            id = "000-0d-d0-d0--00d0d0dacsccs",
            name = "Burger",
            price = 10.0,
            description = "nice meal",
            currency = "$",
            image = "https://s3-alpha-sig.figma.com/img/44f7/7fd1/8c37e4958c7caca679d133c2374c85a6?Expires=1695600000&Signature=bQJPGKU2FfZPuIyU2gEXeJV9Ei9XsRe6ytOGcUIz6mbVzv-g1SJ0hCNg1dXHeKaXEvqEAmXHG-KGQTmiGqldgPCfGWw9a0baZWOfSqrcN-2qPxjkBXZiilrDvhn4UyzF5tDsMwArarP~DpNQ0XcZseHKDGBOZFihi-Dbv8DHhS3qPi4uvi5mrGHltMM9KHkZkLLU5NYQOPUUOXo~A2tg1wk1NI7Zd7h2Jh0v3Rn72o~G1e9dpj8Mqxr-4SZYqY8pBvQTdSXHEIx2uqFuBAobbw4Bi2Fzgdf894XMjjebqcm8b9KSh1RNiIN7y4xD0kb1JCcpV~UFc0HwkyNu9PummQ__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4"
        ),
        MealDto(
            id = "000-0casscac--c-s-cs",
            name = "Burger",
            price = 10.0,
            description = "nice meal",
            currency = "$",
            image = "https://s3-alpha-sig.figma.com/img/74ff/c283/fb93589c0fb95bffae890164ec2aba74?Expires=1695600000&Signature=cSB9yZulMLy-7VnhobwW3PavUWML0c5jJopRFJuz1jC2fWvHa~32qyGcqlHAMIPJTDIk~GOkzIv3UnVGKWJ4Zf75xvqJEF7jx6XTWeO5oECoRidQzbF7oY73ubLtarmWRqlqiUz1-~PrXkMq1r38ba~XvOd80~08EJ5MjGVcwsGnClS06Kstl0lQa9KqiLkMW02GLYlTKIF89ANlAjMcKkcCsVsnYcqemMRqa96JjCuMM5g~Fhpfd0LkY9akixUM1P9ixDoZ7AYNWfjnWSgHAqq4Cr~ZRAP4vuCxzekVbcG8I3xT8I5JsUXbsLG0EO3UnqNE2feBRRgfx1sOG13qwg__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4"
        ),
        MealDto(
            id = "000-0wdwdwdww-d-w-d-wd-dw",
            name = "Burger",
            price = 10.0,
            description = "nice meal",
            currency = "$",
            image = "https://s3-alpha-sig.figma.com/img/44f7/7fd1/8c37e4958c7caca679d133c2374c85a6?Expires=1695600000&Signature=bQJPGKU2FfZPuIyU2gEXeJV9Ei9XsRe6ytOGcUIz6mbVzv-g1SJ0hCNg1dXHeKaXEvqEAmXHG-KGQTmiGqldgPCfGWw9a0baZWOfSqrcN-2qPxjkBXZiilrDvhn4UyzF5tDsMwArarP~DpNQ0XcZseHKDGBOZFihi-Dbv8DHhS3qPi4uvi5mrGHltMM9KHkZkLLU5NYQOPUUOXo~A2tg1wk1NI7Zd7h2Jh0v3Rn72o~G1e9dpj8Mqxr-4SZYqY8pBvQTdSXHEIx2uqFuBAobbw4Bi2Fzgdf894XMjjebqcm8b9KSh1RNiIN7y4xD0kb1JCcpV~UFc0HwkyNu9PummQ__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4"
        )
    )
}
