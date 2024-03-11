package com.angus.api_gateway.data.utils

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.angus.api_gateway.util.APIs
import com.angus.api_gateway.util.EnvConfig

val apiHosts = mapOf(
    APIs.RESTAURANT_API.value to (System.getenv("RESTAURANT_API") ?: EnvConfig.RESTAURANT_API),
    APIs.TAXI_API.value to (System.getenv("TAXI_API") ?: EnvConfig.TAXI_API),
    APIs.IDENTITY_API.value to (System.getenv("IDENTITY_API") ?: EnvConfig.IDENTITY_API),
    APIs.NOTIFICATION_API.value to (System.getenv("NOTIFICATION_API") ?: EnvConfig.NOTIFICATION_API),
    APIs.LOCATION_API.value to (System.getenv("LOCATION_API") ?: EnvConfig.LOCATION_API),
    APIs.CHAT_API.value to (System.getenv("CHAT_API") ?: EnvConfig.CHAT_API)
)

suspend inline fun <reified T> HttpClient.tryToExecute(
    api: APIs,
    attributes: Attributes,
    setErrorMessage: (errorCodes: List<Int>) -> Map<Int, String> = { emptyMap() },
    method: HttpClient.() -> HttpResponse
): T {
    attributes.put(AttributeKey("API"), api.value)
    val response = this.method()
    if (response.status.isSuccess()) {
        return response.body<T>()
    } else {
        val errorResponse = response.body<List<Int>>()
        val errorMessage = setErrorMessage(errorResponse)
        throw LocalizedMessageException(errorMessage)
    }
}

suspend inline fun <reified T> HttpClient.tryToExecuteWebSocket(
    api: APIs,
    path: String,
    attributes: Attributes
): Flow<T> {
    attributes.put(AttributeKey("API"), api.value)
    val host = apiHosts[api.value]
    return flow {
        webSocket(urlString = "ws://$host$path") {
            while (true) {
                try {
                    emit(receiveDeserialized<T>())
                } catch (e: Exception) {
                    throw Exception(e.message.toString())
                }
            }
        }
    }.flowOn(Dispatchers.IO)
}

suspend inline fun <reified T> HttpClient.tryToSendWebSocketData(
    data: T,
    api: APIs,
    path: String,
    attributes: Attributes
) {
    attributes.put(AttributeKey("API"), api.value)
    val host = apiHosts[api.value]
    webSocket(urlString = "ws://$host$path") {
        try {
            sendSerialized(data)
        } catch (e: Exception) {
            throw Exception(e.message.toString())
        }
    }
}


suspend inline fun <reified T> HttpClient.tryToSendAndReceiveWebSocketData(
    data: T,
    api: APIs,
    path: String,
    attributes: Attributes
): Flow<T> {
    attributes.put(AttributeKey("API"), api.value)
    val host = apiHosts[api.value]
    return flow {
        webSocket(urlString = "ws://$host$path") {
            sendSerialized(data)
            emit(receiveDeserialized<T>())
        }
    }
}