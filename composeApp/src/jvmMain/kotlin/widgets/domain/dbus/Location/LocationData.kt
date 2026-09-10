package widgets.domain.dbus.Location

import kotlinx.serialization.Serializable

@Serializable
data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val timeZoneId : String? = null
) {

    val cacheKey: String
        get() = "%.1f,%.1f".format(latitude, longitude)
}
