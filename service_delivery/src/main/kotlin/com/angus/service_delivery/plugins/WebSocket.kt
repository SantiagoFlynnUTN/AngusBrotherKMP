package com.angus.service_taxi.plugins

import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.websocket.*
import kotlinx.serialization.json.Json
import java.time.Duration

fun Application.configureWebSocket() {
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
        pingPeriod = Duration.ofSeconds(10000)
        timeout = Duration.ofSeconds(10000)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
}