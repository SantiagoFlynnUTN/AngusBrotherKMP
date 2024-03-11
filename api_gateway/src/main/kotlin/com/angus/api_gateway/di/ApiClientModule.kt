package com.angus.api_gateway.di

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import com.angus.api_gateway.util.APIs
import com.angus.api_gateway.util.EnvConfig
import kotlin.time.Duration.Companion.seconds

@Module
class ApiClientModule {

    val apiHosts = mapOf(
        APIs.RESTAURANT_API.value to (System.getenv("RESTAURANT_API") ?: EnvConfig.RESTAURANT_API),
        APIs.TAXI_API.value to (System.getenv("TAXI_API") ?: EnvConfig.TAXI_API),
        APIs.IDENTITY_API.value to (System.getenv("IDENTITY_API") ?: EnvConfig.IDENTITY_API),
        APIs.NOTIFICATION_API.value to (System.getenv("NOTIFICATION_API") ?: EnvConfig.NOTIFICATION_API),
        APIs.LOCATION_API.value to (System.getenv("LOCATION_API") ?: EnvConfig.LOCATION_API),
        APIs.CHAT_API.value to (System.getenv("CHAT_API") ?: EnvConfig.CHAT_API)
    )

    @Single
    fun provideHttpClientAttribute(): Attributes {
        return Attributes(true)
    }

    @Single
    fun provideHttpClient(
        clientAttributes: Attributes
    ): HttpClient {
        return HttpClient(CIO) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }

            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(Json)
                pingInterval = 20.seconds.inWholeMilliseconds
            }

            defaultRequest {
                header("Content-Type", "application/json")
                val host = apiHosts[clientAttributes[AttributeKey("API")]]
                url("http://$host")
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }
}