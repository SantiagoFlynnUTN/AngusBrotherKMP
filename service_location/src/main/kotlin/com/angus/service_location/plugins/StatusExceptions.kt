package com.angus.service_location.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import com.angus.service_location.util.configureStatusPages

fun Application.configureStatusExceptions() {
    install(StatusPages) {
        configureStatusPages()
    }
}

