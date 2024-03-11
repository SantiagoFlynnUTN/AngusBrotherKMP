package com.angus.service_identity.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import com.angus.service_identity.endpoints.addressRoutes
import com.angus.service_identity.endpoints.favoriteRoutes
import com.angus.service_identity.endpoints.userManagementRoutes
import com.angus.service_identity.endpoints.userRoutes

fun Application.configureRouting() {
    routing {
        userRoutes()
        addressRoutes()
        userManagementRoutes()
        favoriteRoutes()
    }
}
