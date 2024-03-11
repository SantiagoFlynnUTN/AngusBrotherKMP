package com.angus.service_location.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import com.angus.service_location.endpoints.locationRoutes

fun Application.configureRouting(
) {
    routing {
        locationRoutes()
    }
}
