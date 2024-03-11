package com.angus.service_restaurant.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import com.angus.service_restaurant.api.endpoints.*

fun Application.configureRouting(
) {
    routing {
        restaurantRoutes()
        categoryRoutes()
        mealRoutes()
        cuisineRoutes()
        orderRoutes()
        cartRoutes()
        restaurantPermissionRequestRoutes()
        cartRoutes()
    }
}
