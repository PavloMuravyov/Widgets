package widgets.widgetsModule.widgets.celsius.domain.repository

import widgets.domain.SystemLocale
import widgets.domain.dbus.Location.LocationData
import widgets.widgetsModule.widgets.celsius.domain.api.HereGeocodingApi
import widgets.widgetsModule.widgets.celsius.domain.model.LocalizedLocation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

class LocalizedLocationRepositoryImpl (
    private val hereGeocodingApi: HereGeocodingApi
) : LocalizedLocationRepository {


    private val cache = mutableMapOf<String, LocalizedLocation>()


    override suspend fun getLocation(location: LocationData): LocalizedLocation {

        val key = location.cacheKey
        cache[key]?.let { return it }
        val result = fetchLocalizedLocationApi(location)
        cache[key] = result
        return result

    }

    private suspend fun fetchLocalizedLocationApi(location: LocationData) : LocalizedLocation{

        val response = hereGeocodingApi.getLocalisedLocation(location.latitude, location.longitude, SystemLocale.locale.language)

        val item = response.items.firstOrNull()

        return LocalizedLocation(
            name = item?.address?.city
                ?: item?.address?.county,
            country = item?.address?.countryName
        )
    }
}