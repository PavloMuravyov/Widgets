package widgets.widgetsModule.widgets.celsius.domain.api

import widgets.widgetsModule.widgets.celsius.domain.model.HereResponseDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import widgets.config.BuildConfig

class HereGeocodingApi (
    private val httpClient: HttpClient
) {
    private val apiKey = BuildConfig.HERE_GEOCODING_API_KEY

    suspend fun getLocalisedLocation(latitude: Double, longitude: Double, lang: String) : HereResponseDTO{

        val response = httpClient.get(
            "https://revgeocode.search.hereapi.com/v1/revgeocode"
        ) {
            parameter("at", "$latitude,$longitude")
            parameter("lang", lang)
            parameter("apiKey", apiKey)
        }.body<HereResponseDTO>()

        return response
    }
}