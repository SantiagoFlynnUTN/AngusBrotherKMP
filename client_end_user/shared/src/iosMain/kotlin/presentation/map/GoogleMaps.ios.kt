package presentation.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import presentation.map.CameraLocationBounds
import presentation.map.CameraPosition
import presentation.map.LatLong
import presentation.map.Location
import presentation.map.Marker
import presentation.map.LatLongZoom

@Composable
actual fun GoogleMaps(
    modifier: Modifier,
    isMapOptionSwitchesVisible: Boolean,
    isTrackingEnabled: Boolean,
    userLocation: LatLong?,
    markers: List<Marker>?,
    shouldCalcClusterItems: Boolean,
    onDidCalculateClusterItemList: () -> Unit,
    shouldSetInitialCameraPosition: CameraPosition?,
    shouldCenterCameraOnLatLong: LatLong?,
    onDidCenterCameraOnLatLong: () -> Unit,
    cameraLocationBounds: CameraLocationBounds?,
    polyLine: List<LatLong>?,
    onMapClick: ((LatLong) -> Unit)?,
    onMapLongClick: ((LatLong) -> Unit)?,
    onMarkerInfoClick: ((Marker) -> Unit)?,
    seenRadiusMiles: Double,
    cachedMarkersLastUpdatedLocation: Location?,
    onToggleIsTrackingEnabledClick: (() -> Unit)?,
    onFindMeButtonClick: (() -> Unit)?,
    isMarkersLastUpdatedLocationVisible: Boolean,
    shouldShowInfoMarker: Marker?,
    onDidShowInfoMarker: () -> Unit,
    shouldZoomToLatLongZoom: LatLongZoom?,
    onDidZoomToLatLongZoom: () -> Unit,
    shouldAllowCacheReset: Boolean,
    onDidAllowCacheReset: () -> Unit
) {
}