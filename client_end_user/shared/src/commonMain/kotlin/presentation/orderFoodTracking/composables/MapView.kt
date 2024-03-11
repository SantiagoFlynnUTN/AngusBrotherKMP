package presentation.orderFoodTracking.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import presentation.map.LatLongZoom
import presentation.map.Location
import presentation.map.Marker
import presentation.map.composable.MapContent
import presentation.orderFoodTracking.LocationUiState
import presentation.orderFoodTracking.OrderFoodTrackingUiState

@Composable
fun MapView(
    deliveryLocation: LocationUiState,
    currentLocation: LocationUiState,
    modifier: Modifier = Modifier,
    orderState: OrderFoodTrackingUiState.FoodOrderStatus,
) {
    Box(Modifier.fillMaxSize()) {
        val markersEx = listOf<Marker>(Marker())
        var shouldZoomCameraToLatLongZoom by remember {
            mutableStateOf<LatLongZoom?>(null) // used to zoom to a specific location
        }
        /*var userLocation: Location by remember {
            mutableStateOf(appSettings.lastKnownUserLocation)
        }*/

        MapContent(
            Modifier.fillMaxSize(),
            initialUserLocation = Location(-34.65444, -59.43444),
            markers = markersEx,
            shouldZoomToLatLongZoom = shouldZoomCameraToLatLongZoom,
            onDidZoomToLatLongZoom = {
                shouldZoomCameraToLatLongZoom = null // reset
            },
            shouldCalcClusterItems = false,
            userLocation = Location(-34.65444, -59.43444),
        )
    }
}
