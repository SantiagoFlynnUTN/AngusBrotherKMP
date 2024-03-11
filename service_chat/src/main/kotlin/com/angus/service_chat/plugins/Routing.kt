package com.angus.service_chat.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import com.angus.service_chat.endpoints.chatRoutes

fun Application.configureRouting(
) {
    routing {
        chatRoutes()
    }
}
