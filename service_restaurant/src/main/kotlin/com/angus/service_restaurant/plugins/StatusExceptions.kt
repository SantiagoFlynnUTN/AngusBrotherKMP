package com.angus.service_restaurant.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import com.angus.service_restaurant.api.utils.configureStatusPages

fun Application.configureStatusExceptions() {
    install(StatusPages) {
        configureStatusPages()
    }
}


