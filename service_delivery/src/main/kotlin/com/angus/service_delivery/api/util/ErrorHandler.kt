package com.angus.service_taxi.api.util

import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import com.angus.service_taxi.domain.exceptions.*

fun StatusPagesConfig.configureStatusPages() {
    exception<MissingParameterException> { call, _ ->
        call.respond(HttpStatusCode.BadRequest, listOf(INVALID_REQUEST_PARAMETER))
    }

    exception<AlreadyExistException> { call, _ ->
        call.respond(HttpStatusCode.NotFound, listOf(ALREADY_EXIST))
    }

    exception<ResourceNotFoundException> { call, _ ->
        call.respond(HttpStatusCode.NotFound, listOf(NOT_FOUND))
    }

    exception<CantBeNullException> { call, _ ->
        call.respond(HttpStatusCode.BadRequest, listOf(REQUIRED_QUERY))
    }

    exception<MultiErrorException> { call, exception ->
        call.respond(HttpStatusCode.BadRequest, exception.errorCodes)
    }
}