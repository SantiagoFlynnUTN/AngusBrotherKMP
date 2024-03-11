package presentation.orderHistory

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import presentation.base.ErrorState
import presentation.orderFoodTracking.LocationUiState

data class OrderScreenUiState(
    val selectedType: OrderSelectType = OrderSelectType.DELIVERED,
    val ordersHistory: Flow<PagingData<OrderHistoryUiState>> = emptyFlow(),
    val tripsHistory: Flow<PagingData<TripHistoryUiState>> = emptyFlow(),

    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val error: ErrorState? = null,
) {
    enum class OrderSelectType {
        DELIVERED,
        CANCELED,
    }
}

data class OrderHistoryUiState(
    val meals: String = "",
    val restaurantName: String = "",
    val restaurantImageUrl: String = "",
    val totalPrice: Double = 0.0,
    val currency: String = "",
    val createdAt: String = "",
)

data class TripHistoryUiState(
    val taxiPlateNumber: String = "",
    val driverName: String = "",
    val startPoint: LocationUiState = LocationUiState(),
    val destination: LocationUiState = LocationUiState(),
    val price: Double = 0.0,
    val endDate: String = "",
)
