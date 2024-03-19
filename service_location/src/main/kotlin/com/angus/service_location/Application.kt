package com.angus.service_location

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.angus.service_location.plugins.*

fun main() {
    embeddedServer(Netty, port = 8083, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureStatusExceptions()
    configureSerialization()
    configureMonitoring()
    configureSockets()
    configureRouting()
    configureDependencyInjection()
}
