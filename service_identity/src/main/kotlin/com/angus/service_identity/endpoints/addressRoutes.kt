package com.angus.service_identity.endpoints

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import com.angus.service_identity.endpoints.model.mapper.toDto
import com.angus.service_identity.data.collection.mappers.toEntity
import com.angus.service_identity.domain.util.MissingParameterException
import com.angus.service_identity.domain.usecases.IUserAddressManagementUseCase
import com.angus.service_identity.domain.util.INVALID_REQUEST_PARAMETER
import com.angus.service_identity.endpoints.model.AddressDto
import com.angus.service_identity.endpoints.model.LocationDto
import com.angus.service_identity.endpoints.model.mapper.toEntity

fun Route.addressRoutes() {

    val manageUserAddress: IUserAddressManagementUseCase by inject()

    route("address") {

        get("/{addressId}") {
            val id = call.parameters["addressId"] ?: throw MissingParameterException(INVALID_REQUEST_PARAMETER)
            call.respond(HttpStatusCode.OK, manageUserAddress.getAddress(id).toDto())
        }

        put("/{addressId}") {
            val id = call.parameters["addressId"] ?: throw MissingParameterException(INVALID_REQUEST_PARAMETER)
            val location = call.receive<LocationDto>()
            call.respond(HttpStatusCode.OK, manageUserAddress.updateAddress(id, location.toEntity()).toDto())
        }

        delete("/{addressId}") {
            val id = call.parameters["addressId"] ?: throw MissingParameterException(INVALID_REQUEST_PARAMETER)
            call.respond(HttpStatusCode.OK, manageUserAddress.deleteAddress(id))
        }

    }

    route("/user/{userId}/address") {
        get {
            val id = call.parameters["userId"] ?: throw MissingParameterException(INVALID_REQUEST_PARAMETER)
            val userAddresses = manageUserAddress.getUserAddresses(id)
            call.respond(HttpStatusCode.OK, userAddresses.map { it.toDto() })
        }

        post("/location") {
            val location = call.receive<LocationDto>()
            val userId = call.parameters["userId"] ?: throw MissingParameterException(INVALID_REQUEST_PARAMETER)
            call.respond(HttpStatusCode.Created, manageUserAddress.addLocation(userId, location.toEntity()).toDto())
        }

        post {
            val address = call.receive<AddressDto>()
            val userId = call.parameters["userId"] ?: throw MissingParameterException(INVALID_REQUEST_PARAMETER)
            call.respond(HttpStatusCode.Created, manageUserAddress.addAddress(userId, address.toEntity()).toDto())
        }
    }
}