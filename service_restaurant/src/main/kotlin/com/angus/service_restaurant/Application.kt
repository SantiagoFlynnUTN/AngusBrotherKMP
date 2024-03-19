package com.angus.service_restaurant

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.angus.service_restaurant.plugins.*
import com.angus.service_restaurant.plugins.configureDependencyInjection

fun main() {
    embeddedServer(Netty, port = 8085, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    configureDependencyInjection()
    configureStatusExceptions()
    configureSerialization()
    configureMonitoring()
    configureSockets()
    configureRouting()
}
