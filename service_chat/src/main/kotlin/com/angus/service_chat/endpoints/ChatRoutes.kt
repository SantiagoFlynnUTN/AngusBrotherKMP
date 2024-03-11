package com.angus.service_chat.endpoints

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.flow.collectLatest
import org.koin.ktor.ext.inject
import com.angus.service_chat.domain.usecase.IManageChat
import com.angus.service_chat.domain.usecase.IManageTicket
import com.angus.service_chat.endpoints.models.MessageDto
import com.angus.service_chat.endpoints.models.SupportAgent
import com.angus.service_chat.endpoints.models.TicketDto
import com.angus.service_chat.endpoints.models.mappers.toDto
import com.angus.service_chat.endpoints.models.mappers.toEntity


fun Route.chatRoutes() {

    val manageTicket: IManageTicket by inject()
    val manageChat: IManageChat by inject()

    route("/chat") {

        post("/ticket") {
            val chat = call.receive<TicketDto>()
            val result = manageTicket.createTicket(chat.toEntity())
            call.respond(HttpStatusCode.Created, result.toDto())
        }

        put("/{ticketId}") {
            val ticketId = call.parameters["ticketId"]?.trim().orEmpty()
            val state = call.parameters["state"].toBoolean()
            val result = manageTicket.updateTicketState(ticketId, state)
            call.respond(HttpStatusCode.OK, result)
        }

        webSocket("/tickets/{supportId}") {
            val supportId = call.parameters["supportId"]?.trim().orEmpty()
            manageTicket.supports[supportId] = SupportAgent(session = this)
            while (true) {
                manageTicket.notifySupportAgentOfNewTickets(supportId).collectLatest { ticket ->
                    sendSerialized(ticket.toDto())
                }
            }
        }

        webSocket("/{ticketId}") {
            val ticketId = call.parameters["ticketId"]?.trim().orEmpty()
            while (true) {
                val message = receiveDeserialized<MessageDto>()
                val savedMessage = manageChat.saveMessage(ticketId, message.toEntity())
                sendSerialized(savedMessage.toDto())
            }
        }
    }
}