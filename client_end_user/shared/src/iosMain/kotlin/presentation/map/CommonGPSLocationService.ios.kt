package presentation.map

// Define a common class for location service
actual class CommonGPSLocationService actual constructor() {
    actual suspend fun getCurrentGPSLocationOneTime(): Location {
        TODO("Not yet implemented")
    }

    actual suspend fun onUpdatedGPSLocation(
        errorCallback: (String) -> Unit,
        locationCallback: (Location?) -> Unit
    ) {
    }

    actual suspend fun currentHeading(callback: (Heading?) -> Unit) {
    }

    actual fun getLatestGPSLocation(): Location? {
        TODO("Not yet implemented")
    }

    actual fun allowBackgroundLocationUpdates() {
    }

    actual fun preventBackgroundLocationUpdates() {
    }

}