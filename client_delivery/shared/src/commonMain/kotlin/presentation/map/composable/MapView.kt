package presentation.map.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.mohamedrejeb.calf.ui.web.WebView
import com.mohamedrejeb.calf.ui.web.rememberWebViewStateWithHTMLData
import presentation.map.LocationUiState
import presentation.map.OrderState

@Composable
fun MapView(
    deliveryLocation: LocationUiState,
    restaurantLocation: LocationUiState,
    destination: LocationUiState,
    modifier: Modifier = Modifier,
    orderState: OrderState,
) {

    val stateWebView = rememberWebViewStateWithHTMLData(
        data = map,
        mimeType = "text/html",
        encoding = "UTF-8"
    )

    val jsCode = """
                GetMap();
                clearDirections();
            """.trimIndent()

    val icon1 =
        "http://0.0.0.0:8080/Cheese_0.9092009843595866.png"
    val icon2 =
        "http://0.0.0.0:8080/Cheese_Pizzaa0.027885622802020227.png"

    stateWebView.settings.javaScriptEnabled = true
    WebView(
        state = stateWebView,
        modifier = modifier.fillMaxSize(),
        onCreated = { stateWebView.evaluateJavascript(jsCode, null) }
    )

    LaunchedEffect(orderState) {
        if (orderState == OrderState.LOADING) {
            stateWebView.evaluateJavascript("clearDirections()", null)
        }
        if (orderState == OrderState.NEW_ORDER) {
            stateWebView.evaluateJavascript("clearDirections()", null)
            stateWebView.evaluateJavascript(
                "getDirections(${restaurantLocation.lat},${restaurantLocation.lng},${destination.lat},${destination.lng}, '$icon1' , '$icon2')",
                null
            )
        }
        if (orderState == OrderState.ACCEPTED ){
            stateWebView.evaluateJavascript("clearDirections()", null)
            stateWebView.evaluateJavascript(
                "getDirectionsAfterAccept(${restaurantLocation.lat},${restaurantLocation.lng},${destination.lat},${destination.lng} , ${deliveryLocation.lat},${deliveryLocation.lng} , '$icon1', '$icon2', '$icon1')",
                null
            )
        }

        if (orderState == OrderState.RECEIVED){
            stateWebView.evaluateJavascript("clearDirections()", null)
            stateWebView.evaluateJavascript(
                "getDirections(${deliveryLocation.lat},${deliveryLocation.lng} , ${destination.lat},${destination.lng} , '$icon1', '$icon2')",
                null
            )
        }
    }

    LaunchedEffect(deliveryLocation){
        if (orderState == OrderState.RECEIVED){
            stateWebView.evaluateJavascript("clearDirections()", null)
            stateWebView.evaluateJavascript(
                "getDirections(${deliveryLocation.lat},${deliveryLocation.lng} , ${destination.lat},${destination.lng} , '$icon1', '$icon2')",
                null
            )
        }
    }

}

private val map = """<!doctype HTML><head>
    <link rel="stylesheet" href="style.css" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,700" rel="stylesheet">
    <meta name="viewport" content="width=device-width,user-scalable=no,initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <style>
            html, body {
                height: 100%;
                margin: 0;
                padding: 0;
            }

            .content {
                width: 100%;
                height: 100%;
            }

            #myMap {
                position: relative;
                width: 100%;
                height: 100%;
            }
        </style>
    <meta charset="utf-8"/>
</head>
<body>
<div class="content">
    <script type='text/javascript' src='https://www.bing.com/api/maps/mapcontrol?callback=GetMap&branch=experimental&key=AgNnGNredNlcyG1M8AET0L9F43oDcjEUyZnPhX2mOf5nHNnQcFkz9IFHN_c7z6fO' async defer></script>
    <div id="myMap" style="position:relative;width:100%;height:100%;"></div>
</div>
<script>
var map, directionsManager;

function GetMap() {
    map = new Microsoft.Maps.Map('#myMap', {
        credentials: 'Access_token',
        mapTypeId: Microsoft.Maps.MapTypeId.grayscale,
        minZoom: 1,
        maxZoom: 10
    });
}

function getDirections(startLat, startLng, endLat, endLng , icon1 , icon2) {
    Microsoft.Maps.loadModule('Microsoft.Maps.Directions', function () {
        directionsManager = new Microsoft.Maps.Directions.DirectionsManager(map);

        const requestOptions = {
            routeMode: Microsoft.Maps.Directions.RouteMode.driving,
            routeDraggable: false,
            autoUpdateMapView: false,
        };

        const renderOptions = {
            drivingPolylineOptions: {
                strokeColor: 'red',
                strokeThickness: 8,
            },
            autoUpdateMapView: true
        };

        const startWaypoint = createWaypoint(startLat, startLng, icon1);
        const endWaypoint = createWaypoint(endLat, endLng, icon2);

        directionsManager.addWaypoint(startWaypoint);
        directionsManager.addWaypoint(endWaypoint);
        directionsManager.setRequestOptions(requestOptions);
        directionsManager.setRenderOptions(renderOptions);
        directionsManager.calculateDirections();
    });
}

function getDirectionsAfterAccept(startLat, startLng, endLat, endLng , currentLat, currentLng , icon1,icon2) {
    Microsoft.Maps.loadModule('Microsoft.Maps.Directions', function () {
        directionsManager = new Microsoft.Maps.Directions.DirectionsManager(map);

        const requestOptions = {
            routeMode: Microsoft.Maps.Directions.RouteMode.driving,
            routeDraggable: false,
            autoUpdateMapView: false,
        };

        const renderOptions = {
            drivingPolylineOptions: {
                strokeColor: 'red',
                strokeThickness: 8,
            },
            autoUpdateMapView: true
        };

        const currentWayPoint = createWaypoint(currentLat, currentLng, icon2);
        const startWaypoint = createWaypoint(startLat, startLng, icon1);
        const endWaypoint = createWaypoint(endLat, endLng, icon2);

        directionsManager.addWaypoint(currentWayPoint , 0);
        directionsManager.addWaypoint(startWaypoint);
        directionsManager.addWaypoint(endWaypoint);
        directionsManager.setRequestOptions(requestOptions);
        directionsManager.setRenderOptions(renderOptions);
        directionsManager.calculateDirections();
    });
}


function createWaypoint(lat, lng, icon) {
    const location = new Microsoft.Maps.Location(lat, lng);
    const pushpin = new Microsoft.Maps.Pushpin(location, {
        icon: icon,
        anchor: new Microsoft.Maps.Point(12, 39),
    });

    map.entities.push(pushpin);

    return new Microsoft.Maps.Directions.Waypoint({
        location: location,
        pushpin: pushpin,
    });
}

function clearDirections() {
    directionsManager.clearAll();
    directionsManager.clearDisplay();
    map.entities.clear();
}

function clearMap() {
    map.entities.clear();
}

</script>
<script src="script.js" async></script>
</body>""".trimIndent()
