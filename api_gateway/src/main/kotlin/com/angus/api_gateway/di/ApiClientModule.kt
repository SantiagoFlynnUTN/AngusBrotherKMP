package com.angus.api_gateway.di

import com.angus.api_gateway.util.APIs
import com.angus.api_gateway.util.EnvConfig
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
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import kotlin.time.Duration.Companion.seconds

@Module
class ApiClientModule {

    val apiHosts = mapOf(
        APIs.RESTAURANT_API to (System.getenv("RESTAURANT_API") ?: EnvConfig.RESTAURANT_API),
        APIs.DELIVERY_API to (System.getenv("DELIVERY_API") ?: EnvConfig.TAXI_API),
        APIs.IDENTITY_API to (System.getenv("IDENTITY_API") ?: EnvConfig.IDENTITY_API),
        APIs.NOTIFICATION_API to (System.getenv("NOTIFICATION_API") ?: EnvConfig.NOTIFICATION_API),
        APIs.LOCATION_API to (System.getenv("LOCATION_API") ?: EnvConfig.LOCATION_API),
        APIs.CHAT_API to (System.getenv("CHAT_API") ?: EnvConfig.CHAT_API)
    )

    @Single
    fun provideHttpClientAttribute(): Attributes {
        return Attributes(true)
    }

    fun createHttpClient(apiHostKey: APIs): HttpClient {
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
                val host = apiHosts[apiHostKey]
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

    @Single
    @Named("identity_client")
    fun provideIdentityHttpClient(): HttpClient {
        return createHttpClient(APIs.IDENTITY_API)
    }

    @Single
    @Named("chat_client")
    fun provideChatHttpClient(): HttpClient {
        return createHttpClient(APIs.CHAT_API)
    }

    @Single
    @Named("location_client")
    fun provideLocationHttpClient(): HttpClient {
        return createHttpClient(APIs.LOCATION_API)
    }

    @Single
    @Named("notification_client")
    fun provideNotificationHttpClient(): HttpClient {
        return createHttpClient(APIs.NOTIFICATION_API)
    }

    @Single
    @Named("restaurant_client")
    fun provideRestaurantHttpClient(): HttpClient {
        return createHttpClient(APIs.RESTAURANT_API)
    }

    @Single
    @Named("delivery_client")
    fun provideDeliveryHttpClient(): HttpClient {
        return createHttpClient(APIs.DELIVERY_API)
    }
}
