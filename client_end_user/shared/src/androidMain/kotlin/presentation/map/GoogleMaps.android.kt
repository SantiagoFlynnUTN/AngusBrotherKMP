package presentation.map
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NoLiveLiterals
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

private const val kMaxRestrictedClusterRadiusPhase = 3

// Android Google Maps implementation
@NoLiveLiterals
@Composable
actual fun GoogleMaps(
    modifier: Modifier,
    isMapOptionSwitchesVisible: Boolean,
    isTrackingEnabled: Boolean,
    userLocation: LatLong?,
    markers: List<Marker>?,
    shouldCalcClusterItems: Boolean,
    onDidCalculateClusterItemList: () -> Unit, // Best for setting initial camera position bc zoom level is forced
    shouldSetInitialCameraPosition: CameraPosition?, // Best for tracking user location
    shouldCenterCameraOnLatLong: LatLong?, // Best for showing a bunch of markers
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
    onDidAllowCacheReset: () -> Unit,
) {
    val cameraPositionState = rememberCameraPositionState {
        position = fromLatLngZoom(LatLng(37.4220, -122.0841), 10f)
    }
    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                myLocationButtonEnabled = false, // !isTrackingEnabled,
                compassEnabled = false,
                mapToolbarEnabled = false,
                zoomControlsEnabled = false, // the +/- buttons (obscures the FAB)
                zoomGesturesEnabled = true,
                scrollGesturesEnabled = true,
                rotationGesturesEnabled = false,
            ),
        )
    }
    // TODO check for darktheme
    val mapStyle = mapStyleDark()

    var properties by remember {
        mutableStateOf(
            MapProperties(
                isMyLocationEnabled = true, // always show the dot
                minZoomPreference = 1f,
                maxZoomPreference = 25f,
                mapStyleOptions = MapStyleOptions(
                    mapStyle,
                ),
            ),
        )
    }

    // TODO check for dark theme
    LaunchedEffect(true) {
        properties = MapProperties(
            isMyLocationEnabled = true, // always show the dot
            minZoomPreference = 1f,
            maxZoomPreference = 25f,
            mapStyleOptions = MapStyleOptions(
                mapStyle,
            ),
        )
    }

    // Usually used to setup the initial camera position (doesn't support tracking due to forcing zoom level)
    LaunchedEffect(shouldSetInitialCameraPosition) {
        shouldSetInitialCameraPosition?.let { cameraPosition ->
            // println("💿 GoogleMaps-Android 👾: LaunchedEffect(shouldSetInitialCameraPosition), " +
            //        "shouldSetInitialCameraPosition = ${shouldSetInitialCameraPosition.target}, " +
            //        "zoom= ${shouldSetInitialCameraPosition.zoom}")
            cameraPositionState.move(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        cameraPosition.target.latitude,
                        cameraPosition.target.longitude,
                    ),
                    cameraPosition.zoom,
                    // cameraPositionState.position.zoom // allows users to zoom in and out while maintaining the same center, why does this work?
                ),
            )
        }

        //    // Follow the camera position - LEAVE FOR REFERENCE
        //    snapshotFlow { cameraPositionState.position }
        //        .collect { position ->
        //            // println { "position = ${position.target.latitude}, ${position.target.longitude}" }
        //        }
    }

    // Set Camera to Bounds
    // Note: Zoom level is reset
    LaunchedEffect(cameraLocationBounds) {
        cameraLocationBounds?.let { cameraPositionBounds ->
            // Build the bounding box
            val latLngBounds = LatLngBounds.builder().apply {
                cameraPositionBounds.coordinates.forEach { latLong ->
                    include(LatLng(latLong.latitude, latLong.longitude))
                }
            }.build()

            cameraPositionState.move(
                CameraUpdateFactory.newLatLngBounds(latLngBounds, cameraPositionBounds.padding),
            )
        }
    }

    // Set Camera to `shouldCenterCameraOnLatLong` position (doesn't change zoom level)
    // Note: ONLY allowed to change the camera position ONCE per change in `shouldCenterCameraOnLatLong`.
    //       This is to prevent google maps from locking to position:
    //       ie: By only allowing the camera to change once, the user can pan around the map without
    //       the camera jumping back to the `shouldCenterCameraOnLatLong` position.
    var previousCameraLocationLatLong by remember { mutableStateOf<LatLong?>(null) }
    LaunchedEffect(shouldCenterCameraOnLatLong) {
        if (previousCameraLocationLatLong == shouldCenterCameraOnLatLong) {
            return@LaunchedEffect
        }

        previousCameraLocationLatLong = shouldCenterCameraOnLatLong
        shouldCenterCameraOnLatLong?.let { cameraLocationLatLong ->
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLng(
                    LatLng(
                        cameraLocationLatLong.latitude,
                        cameraLocationLatLong.longitude,
                    ),
                ),
            )
            onDidCenterCameraOnLatLong()
        }
    }

    // Zoom to `shouldZoomToLatLng` position
    var previousCameraLocationLatLongZoom by remember { mutableStateOf<LatLongZoom?>(null) }
    LaunchedEffect(shouldZoomToLatLongZoom) {
        if (previousCameraLocationLatLongZoom == shouldZoomToLatLongZoom) {
            return@LaunchedEffect
        }

        previousCameraLocationLatLongZoom = shouldZoomToLatLongZoom
        shouldZoomToLatLongZoom?.let { cameraLocationLatLongZoom ->
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        cameraLocationLatLongZoom.latLong.latitude,
                        cameraLocationLatLongZoom.latLong.longitude,
                    ),
                    cameraLocationLatLongZoom.zoom,
                ),
            )
            onDidZoomToLatLongZoom()
        }
    }

    // Set Camera to User Location (ie: Tracking) (Allows user to control zoom level)
    LaunchedEffect(userLocation) {
        userLocation?.let { myLocation ->
            if (isTrackingEnabled) {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLng(
                        LatLng(
                            myLocation.latitude,
                            myLocation.longitude,
                        ),
                    ),
                )
            }
        }
    }

    // Information marker - visible after user clicks "find marker" button in details panel
    var infoMarker by remember { mutableStateOf<Marker?>(null) }
    var infoMarkerState = rememberMarkerState()

    Box(modifier.fillMaxSize()) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            uiSettings = uiSettings,
            properties = properties,
            onMapClick = {},
            googleMapOptionsFactory = {
                GoogleMapOptions().apply {
                    this.backgroundColor(0x000000)
                }
            },
        ) {
            println("################################ Render Polylines")

            // Render the polyline
            /*polyLine?.let { polyLine ->
                Polyline(
                    points = List(polyLine.size) {
                        val latLong = polyLine[it]
                        LatLng(latLong.latitude, latLong.longitude)
                    },
                    color = Color(0XFF1572D5),
                    width = 16f,
                )
                Polyline(
                    points = List(polyLine.size) {
                        val latLong = polyLine[it]
                        LatLng(latLong.latitude, latLong.longitude)
                    },
                    color = Color(0XFF00AFFE),
                    width = 8f,
                )
            }*/
        }
    }
}

@NoLiveLiterals
fun mapStyleLight(): String {
    return """
        [
          {
            "featureType": "administrative.land_parcel",
            "elementType": "labels",
            "stylers": [
              {
                "visibility": "off"
              }
            ]
          },
          {
            "featureType": "poi",
            "elementType": "labels.text",
            "stylers": [
              {
                "visibility": "off"
              }
            ]
          },
          {
            "featureType": "poi.business",
            "stylers": [
              {
                "visibility": "off"
              }
            ]
          },
          {
            "featureType": "poi.park",
            "elementType": "labels.text",
            "stylers": [
              {
                "visibility": "off"
              }
            ]
          },
          {
            "featureType": "road.local",
            "elementType": "labels",
            "stylers": [
              {
                "visibility": "off"
              }
            ]
          }
        ]
    """.trimIndent()
}

// https://mapstyle.withgoogle.com/
@NoLiveLiterals
fun mapStyleDark(): String {
    return """
[
  {
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#242f3e"
      }
    ]
  },
  {
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#746855"
      }
    ]
  },
  {
    "elementType": "labels.text.stroke",
    "stylers": [
      {
        "color": "#242f3e"
      }
    ]
  },
  {
    "featureType": "administrative.land_parcel",
    "elementType": "labels",
    "stylers": [
      {
        "visibility": "off"
      }
    ]
  },
  {
    "featureType": "administrative.locality",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#d59563"
      }
    ]
  },
  {
    "featureType": "poi",
    "elementType": "labels.text",
    "stylers": [
      {
        "visibility": "off"
      }
    ]
  },
  {
    "featureType": "poi",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#d59563"
      }
    ]
  },
  {
    "featureType": "poi.business",
    "stylers": [
      {
        "visibility": "off"
      }
    ]
  },
  {
    "featureType": "poi.park",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#263c3f"
      }
    ]
  },
  {
    "featureType": "poi.park",
    "elementType": "labels.text",
    "stylers": [
      {
        "visibility": "off"
      }
    ]
  },
  {
    "featureType": "poi.park",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#6b9a76"
      }
    ]
  },
  {
    "featureType": "road",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#38414e"
      }
    ]
  },
  {
    "featureType": "road",
    "elementType": "geometry.stroke",
    "stylers": [
      {
        "color": "#212a37"
      }
    ]
  },
  {
    "featureType": "road",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#9ca5b3"
      }
    ]
  },
  {
    "featureType": "road.highway",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#746855"
      }
    ]
  },
  {
    "featureType": "road.highway",
    "elementType": "geometry.stroke",
    "stylers": [
      {
        "color": "#1f2835"
      }
    ]
  },
  {
    "featureType": "road.highway",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#f3d19c"
      }
    ]
  },
  {
    "featureType": "road.highway.controlled_access",
    "stylers": [
      {
        "visibility": "simplified"
      }
    ]
  },
  {
    "featureType": "road.highway.controlled_access",
    "elementType": "geometry",
    "stylers": [
      {
        "visibility": "simplified"
      }
    ]
  },
  {
    "featureType": "road.highway.controlled_access",
    "elementType": "labels",
    "stylers": [
      {
        "visibility": "on"
      }
    ]
  },
  {
    "featureType": "road.local",
    "elementType": "labels",
    "stylers": [
      {
        "visibility": "off"
      }
    ]
  },
  {
    "featureType": "transit",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#2f3948"
      }
    ]
  },
  {
    "featureType": "transit.station",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#d59563"
      }
    ]
  },
  {
    "featureType": "water",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#17263c"
      }
    ]
  },
  {
    "featureType": "water",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#515c6d"
      }
    ]
  },
  {
    "featureType": "water",
    "elementType": "labels.text.stroke",
    "stylers": [
      {
        "color": "#17263c"
      }
    ]
  }
]
    """.trimIndent()
}
