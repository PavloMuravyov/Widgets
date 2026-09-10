package widgets.widgetsModule.widgets.celsius.domain.api

import widgets.domain.SystemLocale
import widgets.domain.dbus.Location.LocationData
import widgets.widgetsModule.widgets.celsius.domain.model.ForecastResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import widgets.config.BuildConfig
import java.util.Locale


/**
 * Class for interaction with the public API of the WeatherAPI service (https://www.weatherapi.com/).
 *
 * Uses [HttpClient] to send HTTP requests and receive weather data
 * in the form of a [ForecastResponseDto] object.
 * API key is taken from [BuildConfig.WEATHER_API_KEY].
 *
 * @property httpClient An instance of [HttpClient] used to make HTTP requests.
 * */
class WeatherApi(
    private val httpClient: HttpClient) {

    /** API key required to access WeatherAPI. Read from [BuildConfig].* */
    private val apiKey = BuildConfig.WEATHER_API_KEY

    /**
     * Gets the weather forecast for the specified geolocation.
     *
     * Makes a request to the `forecast.json` endpoint with the following parameters:
     * - `key` — API key;
     * - `q` — coordinates in `"latitude,longitude"` format;
     * - `days` — number of forecast days (default `7`);
     * - `aqi=no` — do not include air quality data;
     * - `alerts=no` — do not include weather alerts.
     *
     * @param location A [Location] object that contains the latitude and longitude of the location.
     * @param days The number of forecast days (1–10). Default is `7`.
     * @return A [ForecastResponseDto] object that contains the forecast data.
     *
     */
    suspend fun getForecast(location: LocationData, days: Int = 7) : ForecastResponseDto {

        val response: ForecastResponseDto = httpClient.get("https://api.weatherapi.com/v1/forecast.json") {
            parameter("key", apiKey)
            parameter("q", "${location.latitude},${location.longitude}")
            parameter("days", days)
            parameter("aqi", "no")
            parameter("alerts", "no")
            parameter("lang", "${SystemLocale.locale.language}")
        }.body()
        return response
    }
}