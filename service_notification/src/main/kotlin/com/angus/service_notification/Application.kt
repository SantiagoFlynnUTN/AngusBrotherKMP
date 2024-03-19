package com.angus.service_notification


import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.angus.service_notification.plugins.*

fun main() {
    embeddedServer(Netty, port = 8084, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    configureFirebaseApp()
    configureDependencyInjection()
    configureSerialization()
    configureMonitoring()
    configureRouting()
    configureStatusPage()
}
