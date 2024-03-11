package com.angus.api_gateway.endpoints

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.koin.ktor.ext.inject
import com.angus.api_gateway.data.localizedMessages.LocalizedMessagesFactory
import com.angus.api_gateway.data.model.notification.NotificationDto
import com.angus.api_gateway.data.model.notification.NotificationSender
import com.angus.api_gateway.data.model.taxi.TaxiDto
import com.angus.api_gateway.data.model.taxi.TripDto
import com.angus.api_gateway.data.model.taxi.TripStatus
import com.angus.api_gateway.data.model.taxi.TripStatus.*
import com.angus.api_gateway.data.service.IdentityService
import com.angus.api_gateway.data.service.NotificationService
import com.angus.api_gateway.data.service.RestaurantService
import com.angus.api_gateway.data.service.DeliveryService
import com.angus.api_gateway.endpoints.utils.*
import com.angus.api_gateway.util.Claim
import com.angus.api_gateway.util.Role

fun Route.taxiRoutes() {
    val deliveryService: DeliveryService by inject()
    val identityService: IdentityService by inject()
    val restaurantService: RestaurantService by inject()
    val localizedMessagesFactory by inject<LocalizedMessagesFactory>()
    val webSocketServerHandler: WebSocketServerHandler by inject()
    val notificationService: NotificationService by inject()

    route("/taxi") {
        authenticateWithRole(Role.DASHBOARD_ADMIN) {
            get {
                val language = extractLocalizationHeader()
                val page = call.parameters["page"]?.toInt() ?: 1
                val limit = call.parameters["limit"]?.toInt() ?: 20
                val result = deliveryService.getAllTaxi(language, page, limit)
                respondWithResult(HttpStatusCode.OK, result)
            }

            post {
                val taxiDto = call.receive<TaxiDto>()
                val language = extractLocalizationHeader()
                val user = identityService.getUserByUsername(taxiDto.driverUsername, language)
                val result = deliveryService.createTaxi(taxiDto.copy(driverId = user.id), language)
                val successMessage = localizedMessagesFactory.createLocalizedMessages(language).taxiCreatedSuccessfully
                respondWithResult(HttpStatusCode.Created, result, successMessage)
            }

            put("/{taxiId}") {
                val id = call.parameters["taxiId"] ?: ""
                val taxiDto = call.receive<TaxiDto>()
                val language = extractLocalizationHeader()
                val result = deliveryService.editTaxi(id, taxiDto, language)
                val successMessage = localizedMessagesFactory.createLocalizedMessages(language).taxiUpdateSuccessfully
                respondWithResult(HttpStatusCode.OK, result, successMessage)
            }

            delete("/{taxiId}") {
                val language = extractLocalizationHeader()
                val id = call.parameters["taxiId"] ?: ""
                val result = deliveryService.deleteTaxi(id, language)
                val successMessage = localizedMessagesFactory.createLocalizedMessages(language).taxiDeleteSuccessfully
                respondWithResult(HttpStatusCode.OK, result, successMessage)
            }
        }

        get("/{taxiId}") {
            val id = call.parameters["taxiId"] ?: ""
            val language = extractLocalizationHeader()
            val result = deliveryService.getTaxiById(id, language)
            respondWithResult(HttpStatusCode.OK, result)
        }
    }

    route("/taxis") {
        authenticateWithRole(Role.DASHBOARD_ADMIN) {
            get("/search") {
                val language = extractLocalizationHeader()
                val page = call.parameters["page"]?.toInt() ?: 1
                val limit = call.parameters["limit"]?.toInt() ?: 20
                val query = call.request.queryParameters["query"]?.trim().orEmpty()
                val status = call.request.queryParameters["status"]?.trim()?.toBoolean()
                val color = call.request.queryParameters["color"]?.trim()?.toLongOrNull()
                val seats = call.request.queryParameters["seats"]?.trim()?.toIntOrNull()
                val result = deliveryService.findTaxisByQuery(page, limit, status, color, seats, query, language)
                respondWithResult(HttpStatusCode.OK, result)
            }
        }

        authenticateWithRole(Role.TAXI_DRIVER) {
            get("/belongs-to-taxi-driver") {
                val language = extractLocalizationHeader()
                val tokenClaim = call.principal<JWTPrincipal>()
                val driverId = tokenClaim?.get(Claim.USER_ID).toString()
                val taxis = deliveryService.getTaxisByDriverId(driverId = driverId, language)
                respondWithResult(HttpStatusCode.OK, taxis)
            }
        }

        authenticateWithRole(Role.DELIVERY) {
            get("/belongs-to-delivery-driver") {
                val language = extractLocalizationHeader()
                val tokenClaim = call.principal<JWTPrincipal>()
                val driverId = tokenClaim?.get(Claim.USER_ID).toString()
                val taxis = deliveryService.getTaxisByDriverId(driverId = driverId, language)
                respondWithResult(HttpStatusCode.OK, taxis)
            }
        }
    }

    route("/trip") {
        get("/{tripId}") {
            val language = extractLocalizationHeader()
            val tripId = call.parameters["tripId"]?.trim().orEmpty()
            val trip = deliveryService.getTripById(tripId = tripId, language)
            respondWithResult(HttpStatusCode.OK, trip)
        }

        get("/user/{orderId}") {
            val language = extractLocalizationHeader()
            val orderId = call.parameters["orderId"]?.trim().orEmpty()
            val trip = deliveryService.getTripByOrderId(orderId, language)
            respondWithResult(HttpStatusCode.OK, trip)
        }

        authenticateWithRole(Role.END_USER) {
            get("/history") {
                val tokenClaim = call.principal<JWTPrincipal>()
                val userId = tokenClaim?.get(Claim.USER_ID).toString()
                val page = call.parameters["page"]?.trim()?.toInt() ?: 1
                val limit = call.parameters["limit"]?.trim()?.toInt() ?: 10
                val language = extractLocalizationHeader()
                val result = deliveryService.getTripsHistoryForUser(
                    userId = userId,
                    page = page,
                    limit = limit,
                    languageCode = language,
                )
                respondWithResult(HttpStatusCode.OK, result)
            }
        }

        authenticateWithRole(Role.END_USER) {
            post("/taxi") {
                val language = extractLocalizationHeader()
                val tokenClaim = call.principal<JWTPrincipal>()
                val userId = tokenClaim?.get(Claim.USER_ID).toString()
                val successMessage = localizedMessagesFactory.createLocalizedMessages(language).tripCreatedSuccessfully
                val restaurantOrder = call.receive<TripDto>()
                val createdTrip = deliveryService.createTrip(restaurantOrder.copy(clientId = userId), language)
                respondWithResult(HttpStatusCode.Created, createdTrip, successMessage)
            }
        }

        authenticateWithRole(Role.RESTAURANT_OWNER) {
            post("/delivery") {
                val language = extractLocalizationHeader()
                val successMessage = localizedMessagesFactory.createLocalizedMessages(language).tripCreatedSuccessfully
                val trip = call.receive<TripDto>()
                val isRestaurantExisted =
                    restaurantService.isRestaurantExisted(restaurantId = trip.restaurantId, language)
                val isUserExisted = identityService.isUserExistedInDb(userId = trip.clientId, language)

                if (!isUserExisted) {
                    respondWithError(
                        call,
                        statusCode = HttpStatusCode.BadRequest,
                        errorMessage = mapOf(400 to "User with this id Not found"),
                    )
                } else if (!isRestaurantExisted) {
                    respondWithError(
                        call,
                        statusCode = HttpStatusCode.BadRequest,
                        errorMessage = mapOf(400 to "Restaurant with this id Not found"),
                    )
                } else {
                    val createdTrip = deliveryService.createTrip(trip, language)
                    respondWithResult(HttpStatusCode.Created, createdTrip, successMessage)
                }
            }
        }

        authenticateWithRole(Role.TAXI_DRIVER) {
            webSocket("/incoming-taxi-rides") {
                val tokenClaim = call.principal<JWTPrincipal>()
                val driverId = tokenClaim?.get(Claim.USER_ID).toString()
                val language = extractLocalizationHeaderFromWebSocket()
                val taxiTrips = deliveryService.getTaxiTrips(driverId)
                webSocketServerHandler.sessions[driverId] = this
                webSocketServerHandler.sessions[driverId]?.let { session ->
                    webSocketServerHandler.tryToCollectAndMapToTaxiTrip(
                        values = taxiTrips,
                        session = session,
                        language = language,
                    )
                }
            }
        }

        authenticateWithRole(Role.DELIVERY) {
            webSocket("/incoming-delivery-orders") {
                val tokenClaim = call.principal<JWTPrincipal>()
                val deliveryId = tokenClaim?.get(Claim.USER_ID).toString()
                val language = extractLocalizationHeaderFromWebSocket()
                val deliveryTrips = deliveryService.getDeliveryTrips(deliveryId)
                webSocketServerHandler.sessions[deliveryId] = this
                webSocketServerHandler.sessions[deliveryId]?.let { session ->
                    webSocketServerHandler.tryToCollectAndMapToDeliveryTrip(
                        values = deliveryTrips,
                        session = session,
                        language = language,
                    )
                }
            }
        }

        authenticateWithRole(Role.END_USER) {
            webSocket("/track/taxi-ride/{tripId}") {
                val tripId = call.parameters["tripId"]?.trim().orEmpty()
                val language = extractLocalizationHeaderFromWebSocket()
                val ride = deliveryService.trackOrderRequest(tripId)
                webSocketServerHandler.sessions[tripId] = this
                webSocketServerHandler.sessions[tripId]?.let { session ->
                    webSocketServerHandler.tryToTrackTaxiRide(
                        values = ride,
                        session = session,
                        language = language,
                    )
                }
            }

            webSocket("/track/delivery-ride/{tripId}") {
                val tripId = call.parameters["tripId"]?.trim().orEmpty()
                val language = extractLocalizationHeaderFromWebSocket()
                val delivery = deliveryService.trackOrderRequest(tripId)
                webSocketServerHandler.sessions[tripId] = this
                webSocketServerHandler.sessions[tripId]?.let { session ->
                    webSocketServerHandler.tryToCollectAndMapToDeliveryTrip(
                        values = delivery,
                        session = session,
                        language = language,
                    )
                }
            }
        }

        authenticateWithRole(Role.TAXI_DRIVER) {
            put("/update/taxi-ride") {
                val tokenClaim = call.principal<JWTPrincipal>()
                val language = extractLocalizationHeader()
                val successMessage = localizedMessagesFactory.createLocalizedMessages(language).tripUpdated
                val parameters = call.receiveParameters()
                val taxiId = parameters["taxiId"]?.trim().orEmpty()
                val tripId = parameters["tripId"]?.trim().orEmpty()
                val driverId = tokenClaim?.get(Claim.USER_ID).toString()
                val approvedTrip =
                    deliveryService.updateTrip(taxiId = taxiId, tripId = tripId, driverId = driverId, language)
                respondWithResult(HttpStatusCode.OK, approvedTrip, successMessage)

                val tripStatus = TripStatus.getOrderStatus(approvedTrip.tripStatus)
                val notificationBody = when (tripStatus) {
                    APPROVED -> localizedMessagesFactory.createLocalizedMessages(language).rideApproved
                    RECEIVED -> localizedMessagesFactory.createLocalizedMessages(language).taxiArrivedToUserLocation
                    FINISHED -> localizedMessagesFactory.createLocalizedMessages(language).taxiArrivedToDestination
                    else -> ""
                }
                if (tripStatus != TripStatus.PENDING) {
                    val orderNotification = NotificationDto(
                        userId = approvedTrip.clientId,
                        topicId = approvedTrip.id,
                        title = approvedTrip.destinationAddress!!,
                        body = notificationBody,
                        sender = NotificationSender.TAXI.code,
                    )
                    notificationService.sendNotificationToUser(orderNotification, language)
                }
            }
        }

        authenticateWithRole(Role.DELIVERY) {
            put("/update/delivery-order") {
                val tokenClaim = call.principal<JWTPrincipal>()
                val language = extractLocalizationHeader()
                val successMessage = localizedMessagesFactory.createLocalizedMessages(language).tripUpdated
                val parameters = call.receiveParameters()
                val taxiId = parameters["taxiId"]?.trim().orEmpty()
                val tripId = parameters["tripId"]?.trim().orEmpty()
                val deliveryId = tokenClaim?.get(Claim.USER_ID).toString()
                val approvedTrip =
                    deliveryService.updateTrip(taxiId = taxiId, tripId = tripId, driverId = deliveryId, language)
                respondWithResult(HttpStatusCode.OK, approvedTrip, successMessage)

                val tripStatus = TripStatus.getOrderStatus(approvedTrip.tripStatus)
                val notificationBody = when (tripStatus) {
                    APPROVED -> localizedMessagesFactory.createLocalizedMessages(language).orderApprovedFromDelivery
                    RECEIVED -> localizedMessagesFactory.createLocalizedMessages(language).orderArrivedToRestaurant
                    FINISHED -> localizedMessagesFactory.createLocalizedMessages(language).orderArrivedToClient
                    else -> ""
                }
                if (tripStatus != TripStatus.PENDING) {
                    val restaurant = restaurantService.getRestaurantInfo(language, approvedTrip.restaurantId!!)
                    val orderNotification = NotificationDto(
                        userId = approvedTrip.clientId,
                        topicId = approvedTrip.id,
                        title = restaurant.name!!,
                        body = notificationBody,
                        sender = NotificationSender.DELIVERY.code,
                    )
                    notificationService.sendNotificationToUser(orderNotification, language)
                }
            }
        }
    }
}
