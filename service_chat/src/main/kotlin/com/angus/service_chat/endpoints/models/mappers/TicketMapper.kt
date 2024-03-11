package com.angus.service_chat.endpoints.models.mappers

import com.angus.service_chat.domain.entity.Ticket
import com.angus.service_chat.endpoints.models.TicketDto

fun Ticket.toDto(): TicketDto {
    return TicketDto(
        id = id,
        userId = userId,
        supportId = supportId,
        time = time,
        messages = messages.map { it.toDto() },
        isOpen = isOpen
    )
}


fun TicketDto.toEntity(): Ticket {
    return Ticket(
        id = id ?: "",
        userId = userId,
        supportId = supportId ?: "",
        time = time ?: 0,
        messages = messages?.map { it.toEntity() } ?: emptyList(),
        isOpen = isOpen ?: true
    )
}