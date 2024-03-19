package com.angus.service_chat

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.angus.service_chat.plugins.*
import com.angus.service_chat.plugins.configureSerialization

fun main() {
    embeddedServer(Netty, port = 8081, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureDependencyInjection()
    configureSockets()
    configureStatusExceptions()
    configureRouting()
    configureSerialization()
    configureMonitoring()
}
