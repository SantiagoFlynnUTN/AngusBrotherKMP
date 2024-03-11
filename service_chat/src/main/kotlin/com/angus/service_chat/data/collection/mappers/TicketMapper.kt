package com.angus.service_chat.data.collection.mappers

import com.angus.service_chat.data.collection.TicketCollection
import com.angus.service_chat.domain.entity.Ticket


fun Ticket.toCollection(): TicketCollection {
    return TicketCollection(
        ticketId = id,
        userId = userId,
        supportId = supportId,
        time = time,
        isOpen = isOpen
    )
}

fun TicketCollection.toEntity(): Ticket {
    return Ticket(
        id = ticketId,
        userId = userId,
        supportId = supportId,
        time = time,
        messages = messages.toEntity(),
        isOpen = isOpen
    )
}