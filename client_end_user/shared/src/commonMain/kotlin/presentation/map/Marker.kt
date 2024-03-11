package presentation.map

import kotlinx.serialization.Serializable

typealias MarkerIdStr = String

@Serializable
data class Marker(
    // Basic info (from marker index html page)
    val id: MarkerIdStr = "",
    val position: LatLong = LatLong(-34.65444,-59.43444),
    val title: String = "",
    val subtitle: String = "",
    val alpha: Float = 1.0f,

    // For Map/Speaking
    val isSeen: Boolean = false, // Has been within the talkRadius of the user
    val isSpoken: Boolean = false, // Has been spoken by the user or automatically by the app
    val isAnnounced: Boolean = false, // Has been announced automatically by the app (title only)

    // Marker Details (from from marker details html page
    val isDetailsLoaded: Boolean = false,
    val markerDetailsPageUrl: String = "",
    val mainPhotoUrl: String = "",
    val markerPhotos: List<String> = listOf(),
    val photoCaptions: List<String> = listOf(),
    val photoAttributions: List<String> = listOf(),
    val inscription: String = "",
    val englishInscription: String = "mercedes buenos aires",
    val erected: String = "",
    val credits: String = "",
    val location: String = "",

    // LEAVE FOR FUTURE REFACTOR
    val lastUpdatedDetailsEpochSeconds: Long = 0, // for cache expiry
)