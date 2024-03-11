import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun openNavigationAction(lat: Double, lng: Double, markerTitle: String) {
    CoroutineScope(Dispatchers.Main).launch {
        _androidIntentFlow.emit(
//            Intent(Intent.ACTION_VIEW, Uri.parse("geo:$lat,$lng")).also { // just shows location
//            Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=$lat,$lng")).also {
//                it.setPackage("com.google.android.apps.maps")
//            }
            Intent("Navigation").also {
                it.putExtra("lat", lat)
                it.putExtra("lng", lng)
                it.putExtra("label", "📌���")
                it.putExtra("markerTitle", markerTitle)
            }
        )
    }
}


