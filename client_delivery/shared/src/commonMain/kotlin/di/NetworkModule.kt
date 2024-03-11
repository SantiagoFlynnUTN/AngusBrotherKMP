package di

import data.gateway.remote.authorizationIntercept
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.header
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import util.getEngine

val networkModule = module {

    single {
        val client = HttpClient(getEngine()) {
            expectSuccess = true
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }

            defaultRequest {
                header("Content-Type", "application/json")
                header("Accept-Language", "en")
                url("http://192.168.0.215:8080/")
                //url("http://192.168.1.100:8080/")

            }
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(Json)
                val urlBuilder = URLBuilder(
                    protocol = URLProtocol.WS,
                    host = "192.168.0.215:8080/",
//    Local         host = "192.168.1.100",
//    Local         port = 8080
                )
                Url(urlBuilder)
                pingInterval = 10000
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
        authorizationIntercept(client)
        client
    }
}