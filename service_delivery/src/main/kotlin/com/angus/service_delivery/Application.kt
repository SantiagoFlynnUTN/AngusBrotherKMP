package com.angus.service_taxi

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import com.angus.service_taxi.plugins.*

fun main() {
    embeddedServer(Netty, port = 8086, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureStatusExceptions()
    configureDependencyInjection()
    configureSerialization()
    configureMonitoring()
    configureWebSocket()
    configureRouting()
}
