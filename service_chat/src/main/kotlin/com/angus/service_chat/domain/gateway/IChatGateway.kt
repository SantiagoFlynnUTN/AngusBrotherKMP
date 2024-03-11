package com.angus.service_chat.domain.gateway

import com.angus.service_chat.domain.entity.Message
import com.angus.service_chat.domain.entity.Ticket

interface IChatGateway {
    suspend fun createTicket(ticket: Ticket): Ticket
    suspend fun updateTicket(ticketId: String, supportId: String): Ticket
    suspend fun saveMessage(ticketId: String, message: Message): Message
    suspend fun updateTicketState(ticketId: String, state: Boolean): Boolean
}