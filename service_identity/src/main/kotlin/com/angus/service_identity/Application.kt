package com.angus.service_identity

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.angus.service_identity.plugins.*

fun main() {
    embeddedServer(Netty, port = 8082, host = "192.168.0.215", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureDependencyInjection()
    configureSerialization()
    configureMonitoring()
    configureRouting()
    configureStatusPages()
}
