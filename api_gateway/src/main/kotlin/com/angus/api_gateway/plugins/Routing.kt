package com.angus.api_gateway.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import com.angus.api_gateway.data.model.authenticate.TokenConfiguration
import com.angus.api_gateway.endpoints.cuisineRoute
import com.angus.api_gateway.endpoints.permissionRoutes
import com.angus.api_gateway.endpoints.restaurantRoutes
import com.angus.api_gateway.endpoints.taxiRoutes
import com.angus.api_gateway.endpoints.userRoutes
import com.angus.api_gateway.endpoints.*

fun Application.configureRouting(tokenConfiguration: TokenConfiguration) {
    routing {
        authenticationRoutes(tokenConfiguration)
        userRoutes()
        permissionRoutes()
        restaurantRoutes()
        cuisineRoute()
        taxiRoutes()
        mealRoute()
        orderRoutes()
        notificationRoute()
        locationRoute()
        cartRoutes()
        chatRoute()
        offerRoute()
    }
}
