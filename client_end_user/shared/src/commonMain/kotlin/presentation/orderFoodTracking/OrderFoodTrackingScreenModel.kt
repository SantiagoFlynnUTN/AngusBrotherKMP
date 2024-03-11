package presentation.orderFoodTracking

import cafe.adriel.voyager.core.model.screenModelScope
import domain.entity.DeliveryRide
import domain.entity.FoodOrder
import domain.entity.Location
import domain.entity.TripStatus
import domain.usecase.ITrackOrdersUseCase
import kotlinx.coroutines.CoroutineScope
import presentation.base.BaseScreenModel
import presentation.base.ErrorState

class OrderFoodTrackingScreenModel(
    private val orderId: String,
    private val tripId: String,
    private val trackOrders: ITrackOrdersUseCase,
) : BaseScreenModel<OrderFoodTrackingUiState, OrderFoodTrackingUiEffect>(OrderFoodTrackingUiState()),
    OrderFoodTrackingInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope
    override fun onBackButtonClicked() {
        sendNewEffect(OrderFoodTrackingUiEffect.NavigateBack)
    }

    init {
        getUserLocation()
        if (orderId.isNotEmpty()) {
            getOrderStatus(orderId)
        } else {
            getTripStatus(tripId)
        }
    }


    private fun trackDeliveryLocation(tripId: String) {
        tryToCollect(
            function = { trackOrders.trackDriverLocation(tripId) },
            onNewValue = ::onTrackDeliveryLocationSuccess,
            onError = ::onTrackingError,
        )
    }

    private fun onTrackDeliveryLocationSuccess(location: Location) {
        updateState { it.copy(deliveryLocation = location.toUiState()) }
    }

    private fun getTripStatus(tripId: String) {
        tryToExecute(
            function = { trackOrders.getTripStatus(tripId) },
            onSuccess = ::onGetTripStatusSuccess,
            onError = ::onGetStatusError,
        )
    }

    private fun onGetTripStatusSuccess(tripStatus: TripStatus) {
        val status = when (tripStatus) {
            TripStatus.APPROVED -> {
                trackDeliveryLocation(tripId)
                OrderFoodTrackingUiState.FoodOrderStatus.ORDER_IN_THE_ROUTE
            }
            TripStatus.RECEIVED -> {
                OrderFoodTrackingUiState.FoodOrderStatus.ORDER_IN_COOKING
            }

            TripStatus.FINISHED -> {
                OrderFoodTrackingUiState.FoodOrderStatus.ORDER_ARRIVED
            }
            else -> {
                OrderFoodTrackingUiState.FoodOrderStatus.ORDER_IN_COOKING
            }
        }
        println("currentOrderStatus: ${status}")
        updateState { it.copy(order = it.order.copy(currentOrderStatus = status)) }
        trackDelivery(tripId)
    }

    private fun getOrderStatus(orderId: String) {
        tryToExecute(
            function = { trackOrders.getOrderStatus(orderId) },
            onSuccess = ::onGetOrderStatusSuccess,
            onError = ::onGetStatusError,
        )
    }

    private fun onGetOrderStatusSuccess(orderStatusInRestaurant: FoodOrder.OrderStatusInRestaurant) {
        val orderStatus =
            if (orderStatusInRestaurant == FoodOrder.OrderStatusInRestaurant.APPROVED) {
                OrderFoodTrackingUiState.FoodOrderStatus.ORDER_PLACED
            } else {
                OrderFoodTrackingUiState.FoodOrderStatus.ORDER_IN_COOKING
            }
        updateState { it.copy(order = it.order.copy(currentOrderStatus = orderStatus)) }
        trackFoodOrderFromRestaurant(orderId)
    }

    private fun onGetStatusError(errorState: ErrorState) {
        println("Error getting order status: $errorState")
    }

    private fun getTripIdAndTrackDelivery(orderId: String) {
        tryToExecute(
            function = { trackOrders.getTripId(orderId) },
            onSuccess = ::onGetTripIdSuccess,
            onError = ::onGetTripIdError,
        )
    }

    private fun onGetTripIdSuccess(tripId: String) {
        trackDelivery(tripId)
    }

    private fun onGetTripIdError(errorState: ErrorState) {
        println("Error getting trip id: $errorState")
    }

    private fun trackDelivery(tripId: String) {
        tryToCollect(
            function = { trackOrders.trackDeliveryRide(tripId) },
            onNewValue = ::onTrackDeliverySuccess,
            onError = ::onTrackingError,
        )
    }

    private fun onTrackDeliverySuccess(deliveryRide: DeliveryRide) {
        val updatedOrderStatus = when (deliveryRide.tripStatus) {
            TripStatus.RECEIVED -> {
                trackDeliveryLocation(deliveryRide.id)
                OrderFoodTrackingUiState.FoodOrderStatus.ORDER_IN_THE_ROUTE
            }

            TripStatus.FINISHED -> {
                OrderFoodTrackingUiState.FoodOrderStatus.ORDER_ARRIVED
            }

            else -> {
                OrderFoodTrackingUiState.FoodOrderStatus.ORDER_IN_COOKING
            }
        }

        updateState { it.copy(order = it.order.copy(currentOrderStatus = updatedOrderStatus)) }
    }

    private fun trackFoodOrderFromRestaurant(orderId: String) {
        tryToCollect(
            function = { trackOrders.trackFoodOrderInRestaurant(orderId) },
            onNewValue = ::onTrackFoodOrderFromRestaurantSuccess,
            onError = ::onTrackingError,
        )
    }

    private fun onTrackFoodOrderFromRestaurantSuccess(foodOrder: FoodOrder) {
        val updatedOrderStatus = when (foodOrder.orderStatus) {
            FoodOrder.OrderStatusInRestaurant.APPROVED -> {
                OrderFoodTrackingUiState.FoodOrderStatus.ORDER_PLACED
            }

            FoodOrder.OrderStatusInRestaurant.IN_COOKING -> {
                OrderFoodTrackingUiState.FoodOrderStatus.ORDER_IN_COOKING
            }

            FoodOrder.OrderStatusInRestaurant.DONE -> {
                getTripIdAndTrackDelivery(foodOrder.id)
                OrderFoodTrackingUiState.FoodOrderStatus.ORDER_IN_COOKING
            }

            else -> {
                OrderFoodTrackingUiState.FoodOrderStatus.ORDER_IN_COOKING
            }
        }

        updateState { it.copy(order = it.order.copy(currentOrderStatus = updatedOrderStatus)) }
    }

    private fun onTrackingError(errorState: ErrorState) {
        println("Error tracking order: $errorState")
    }

    private fun getUserLocation() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            function = trackOrders::getUserOrderLocation,
            onSuccess = ::onGetUserLocationSuccess,
            onError = ::onGetUserLocationError,
        )
    }

    private fun onGetUserLocationSuccess(location: Location) {
        updateState { it.copy(userLocation = location.toUiState(), isLoading = false) }
    }

    private fun onGetUserLocationError(errorState: ErrorState) {
        updateState { it.copy(isLoading = false) }
    }
}