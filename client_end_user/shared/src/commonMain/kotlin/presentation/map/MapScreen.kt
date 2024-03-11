package presentation.map

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.resources.ExperimentalResourceApi
import presentation.base.BaseScreen
import presentation.map.composable.MapContent

class MapScreen :
    BaseScreen<MapScreenModel, MapUiState, MapUiEffect, MapInteractionListener>() {

    @Composable
    override fun Content() {
        initScreen(getScreenModel())
    }

    override fun onEffect(effect: MapUiEffect, navigator: Navigator) {
        when (effect) {
            else -> {}
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun onRender(state: MapUiState, listener: MapInteractionListener) {
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

    @OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class)
    @Composable
    private fun content(
        state: MapUiState,
        listener: MapInteractionListener,
        modifier: Modifier = Modifier,
    ) {
    }
}
