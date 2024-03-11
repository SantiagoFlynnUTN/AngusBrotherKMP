package presentation.map.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import presentation.map.CameraLocationBounds
import presentation.map.CameraPosition
import presentation.map.GoogleMaps
import presentation.map.LatLong
import presentation.map.LatLongZoom
import presentation.map.Location
import presentation.map.Marker
import presentation.map.isLocationValid

@Composable
fun MapContent(
    modifier: Modifier = Modifier,
    initialUserLocation: Location = Location(-34.65554, -59.43554), // only sets the initial position, not tracked. Use `userLocation` for tracking.
    userLocation: Location =  Location(-34.65554, -59.43554),
    markers: List<Marker>,
    mapBounds: List<LatLong>? = null,
    shouldCalcClusterItems: Boolean,
    onDidCalcClusterItems: () -> Unit = {},
    isTrackingEnabled: Boolean = true,
    shouldCenterCameraOnLocation: Location? = null,
    onDidCenterCameraOnLocation: () -> Unit = {},
    seenRadiusMiles: Double = .5,
    cachedMarkersLastUpdatedLocation: Location? = null,
    onToggleIsTrackingEnabled: (() -> Unit)? = null,
    onFindMeButtonClicked: (() -> Unit)? = null,
    isMarkersLastUpdatedLocationVisible: Boolean = false,
    isMapOptionSwitchesVisible: Boolean = true,
    onMarkerInfoClick: ((Marker) -> Unit)? = null,
    shouldShowInfoMarker: Marker? = null,
    onDidShowInfoMarker: () -> Unit = {},
    shouldZoomToLatLongZoom: LatLongZoom?,
    onDidZoomToLatLongZoom: () -> Unit,
    shouldAllowCacheReset: Boolean = false,
    onDidAllowCacheReset: () -> Unit = {},
) {
    var isFirstUpdate by remember { mutableStateOf(true) } // force map to update at least once
    var didSetInitialCameraPosition by remember { mutableStateOf(false) } // Move to initial location on first update

    // Guard against initial location being (0.0, 0.0) (prevents initial map drawing in the middle of the atlantic ocean near africa)
    if (!initialUserLocation.isLocationValid()) return

    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Log.d("💿 MapContent: isFinishedLoadingMarkerData=$isFinishedLoadingMarkerData, isFirstUpdate=$isFirstUpdate, shouldRedrawMapMarkers=$shouldRedrawMarkers")
        println("################################ Initializing GoogleMaps")
        GoogleMaps(
            polyLine = listOf(LatLong(-34.65444, -59.43444), LatLong(-34.65554, -59.43554)),
            modifier = modifier,
            isMapOptionSwitchesVisible = isMapOptionSwitchesVisible,
            isTrackingEnabled = isTrackingEnabled,
            userLocation = LatLong(
                // passed to map to track location
                userLocation.latitude,
                userLocation.longitude,
            ),
            markers = markers.ifEmpty { null },
            shouldCalcClusterItems = shouldCalcClusterItems,
            onDidCalculateClusterItemList = onDidCalcClusterItems,
            shouldSetInitialCameraPosition =
                CameraPosition(
                    target = LatLong(
                        initialUserLocation.latitude,
                        initialUserLocation.longitude,
                    ),
                    zoom = 15f, // note: forced zoom level
                ),
            shouldCenterCameraOnLatLong = remember(shouldCenterCameraOnLocation) {
                // 37.422160,
                // -122.084270  // googleplex
                if (!isFirstUpdate) {
                    println("💿 MapContent.shouldCenterCameraOnLatLong: ➤➤➤ Centering camera position")
                    shouldCenterCameraOnLocation?.let {
                        LatLong(
                            shouldCenterCameraOnLocation.latitude,
                            shouldCenterCameraOnLocation.longitude,
                        )
                    } ?: run {
                        null
                    }
                } else {
                    null
                }
            },
            onDidCenterCameraOnLatLong = onDidCenterCameraOnLocation,
            cameraLocationBounds = remember { // Center around bound of markers // note: does not allow user to move map
                mapBounds?.let {
                    CameraLocationBounds(
                        coordinates = mapBounds,
                        padding = 80, // in pixels
                    )
                } ?: run {
                    null // won't center around bounds
                }
            },
            onMarkerInfoClick = onMarkerInfoClick,
            seenRadiusMiles = seenRadiusMiles,
            cachedMarkersLastUpdatedLocation = cachedMarkersLastUpdatedLocation,
            onToggleIsTrackingEnabledClick = onToggleIsTrackingEnabled,
            onFindMeButtonClick = onFindMeButtonClicked,
            isMarkersLastUpdatedLocationVisible = isMarkersLastUpdatedLocationVisible,
            shouldShowInfoMarker = shouldShowInfoMarker,
            onDidShowInfoMarker = onDidShowInfoMarker,
            shouldZoomToLatLongZoom = shouldZoomToLatLongZoom,
            onDidZoomToLatLongZoom = onDidZoomToLatLongZoom,
            shouldAllowCacheReset = shouldAllowCacheReset,
            onDidAllowCacheReset = onDidAllowCacheReset,
        )

        // Indicate first update has occurred
        isFirstUpdate = false
    }
}
