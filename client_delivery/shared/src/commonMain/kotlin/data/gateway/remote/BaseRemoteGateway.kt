package data.gateway.remote

import data.remote.model.BaseResponse
import domain.utils.InvalidPasswordException
import domain.utils.NoInternetException
import domain.utils.SocketException
import domain.utils.UnAuthorizedException
import domain.utils.UnknownErrorException
import domain.utils.UserNotFoundException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.ws
import io.ktor.client.plugins.websocket.wss
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class BaseRemoteGateway(val client: HttpClient) {

    protected suspend inline fun <reified T> tryToExecute(
        method: HttpClient.() -> HttpResponse,
    ): T {
        try {
            return client.method().body()
        } catch (e: ClientRequestException) {
            val errorMessages = e.response.body<BaseResponse<String>>().status.errorMessages
            errorMessages?.let { throwMatchingException(it) }
            throw UnknownErrorException()
        } catch (e: UnresolvedAddressException) {
            throw NoInternetException()
        } catch (e: Exception) {
            println("exception: $e")
            throw UnknownErrorException()
        }
    }

    suspend inline fun <reified T> HttpClient.tryToExecuteWebSocket(path: String): Flow<T> {
        return flow {
            ws(path = path) {
                while (true) {
                    try {
                        emit(receiveDeserialized<T>())
                    } catch (e: Exception) {
                        throw SocketException()
                    }
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    fun throwMatchingException(errorMessages: Map<String, String>) {
        when {
            errorMessages.containsErrors(WRONG_PASSWORD) ->
                throw InvalidPasswordException(errorMessages.getOrEmpty(WRONG_PASSWORD))

            errorMessages.containsErrors(USER_NOT_EXIST) ->
                throw UserNotFoundException(errorMessages.getOrEmpty(USER_NOT_EXIST))

            errorMessages.containsErrors(INVALID_PERMISSION) ->
                throw UnAuthorizedException(errorMessages.getOrEmpty(INVALID_PERMISSION))
        }
    }

    private fun Map<String, String>.containsErrors(vararg errorCodes: String): Boolean =
        keys.containsAll(errorCodes.toList())

    private fun Map<String, String>.getOrEmpty(key: String): String = get(key) ?: ""

    companion object {
        const val WRONG_PASSWORD = "1013"
        const val USER_NOT_EXIST = "1043"
        const val INVALID_PERMISSION = "1014"
    }
}