package com.angus.api_gateway.endpoints

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.flow.collectLatest
import org.koin.ktor.ext.inject
import com.angus.api_gateway.data.model.MessageDto
import com.angus.api_gateway.data.model.ServerResponse
import com.angus.api_gateway.data.model.TicketDto
import com.angus.api_gateway.data.service.ChatService
import com.angus.api_gateway.endpoints.utils.*
import com.angus.api_gateway.util.Role

fun Route.chatRoute() {
    val chatService: ChatService by inject()
    val webSocketServerHandler: WebSocketServerHandler by inject()
    val chatSocketHandler: ChatSocketHandler by inject()

    route("/chat") {
        authenticateWithRole(Role.END_USER) {
            post("/ticket") {
                val language = extractLocalizationHeader()
                val ticket = call.receive<TicketDto>()
                val result = chatService.createTicket(ticket, language)
                respondWithResult(HttpStatusCode.Created, result)
            }
        }

        authenticateWithRole(Role.SUPPORT) {
            put("/{ticketId}") {
                val ticketId = call.parameters["ticketId"]?.trim().orEmpty()
                val state = call.parameters["state"].toBoolean()
                val language = extractLocalizationHeader()
                val result = chatService.updateTicketState(ticketId, state, language)
                respondWithResult(HttpStatusCode.OK, result)
            }

            webSocket("/tickets/{supportId}") {
                val supportId = call.parameters["supportId"]?.trim().orEmpty()
                val tickets = chatService.receiveTicket(supportId)
                webSocketServerHandler.sessions[supportId] = this
                webSocketServerHandler.sessions[supportId]?.let {
                    webSocketServerHandler.tryToCollect(tickets, it)
                }
            }
        }

        webSocket("/{ticketId}") {
            val ticketId = call.parameters["ticketId"]?.trim().orEmpty()

            if ((chatSocketHandler.connections[ticketId]?.size ?: 0) > 1) return@webSocket

            val chatConnections = chatSocketHandler.connections.computeIfAbsent(ticketId) { LinkedHashSet() }
            chatConnections.add(Connection(this))

            try {
                while (true) {
                    val receiveMessage = receiveDeserialized<MessageDto>()
                    chatService.sendAndReceiveMessage(receiveMessage, ticketId).collectLatest { message ->
                        chatSocketHandler.connections[ticketId]?.forEach {
                            it.session.sendSerialized(ServerResponse.success(message, null))
                        }
                    }
                }
            } catch (e: Throwable) {
                println(e.localizedMessage)
            } finally {
                chatSocketHandler.connections.remove(ticketId)
            }
        }
    }
}
