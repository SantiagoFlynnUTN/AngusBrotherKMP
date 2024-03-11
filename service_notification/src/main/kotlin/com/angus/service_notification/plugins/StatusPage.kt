package com.angus.service_notification.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import com.angus.service_notification.domain.entity.InternalServerErrorException
import com.angus.service_notification.domain.entity.NotFoundException
import com.angus.service_notification.domain.entity.ResourceAlreadyExistsException

fun Application.configureStatusPage() {
    install(StatusPages) {
        handleExceptions()
    }
}

fun StatusPagesConfig.handleExceptions() {

    respondWithErrorCodes<MissingRequestParameterException>(HttpStatusCode.BadRequest)

    respondWithErrorCodes<NotFoundException>(HttpStatusCode.NotFound)

    respondWithErrorCodes<InternalServerErrorException>(HttpStatusCode.InternalServerError)

    respondWithErrorCodes<ResourceAlreadyExistsException>(HttpStatusCode.Conflict)
}


private inline fun <reified T : Throwable> StatusPagesConfig.respondWithErrorCodes(
    statusCode: HttpStatusCode,
) {
    exception<T> { call, t ->
        val reasons = t.message?.split(",")?.map { it.toInt() } ?: emptyList()
        call.respond(statusCode, reasons)
    }
}
