package widgets.widgetsModule.widgets.celsius.domain.model

import kotlinx.serialization.Serializable


/**
 * DTO models for working with the WeatherAPI service response.
 *
 * Contains serialized data structures that describe:
 * - current weather ([CurrentDto]),
 * - geolocation ([LocationDto]),
 * - forecast for several days ([ForecastDto]) with detail by hour ([HourDto]) and day ([ForecastDayDto]),
 * - astronomical data ([AstroDto]) and weather conditions ([ConditionDto]).
 *
 * Used to deserialize the `forecast.json` endpoint response into [ForecastResponseDto].
 */
@Serializable
data class ForecastResponseDto(
    val location: LocationDto,
    val current: CurrentDto,
    val forecast: ForecastDto
)
@Serializable
data class LocationDto(
    val name: String,
    val region: String?,
    val country: String,
    val lat: Double,
    val lon: Double,
    val tz_id: String,
    val localtime_epoch: Long,
    val localtime: String
)
@Serializable
data class CurrentDto(
    val temp_c: Double,
    val condition: ConditionDto,
    val wind_kph: Double,
    val wind_degree: Int,
    val wind_dir: String,
    val last_updated: String,
    val is_day: Int,
    val sunrise: String? = null,
    val sunset: String? = null,

    )
@Serializable
data class ConditionDto(
    val text: String,
    val icon: String,
    val code: Int
)
@Serializable
data class ForecastDto(
    val forecastday: List<ForecastDayDto>
)
@Serializable
data class ForecastDayDto(
    val date: String,
    val day: DayDto,
    val hour: List<HourDto>,
    val astro: AstroDto
)
@Serializable
data class DayDto(
    val maxtemp_c: Double,
    val mintemp_c: Double,
    val condition: ConditionDto
)
@Serializable
data class HourDto(
    val time_epoch: Long,
    val time: String,
    val temp_c: Double,
    val condition: ConditionDto,
    val chance_of_rain: Int,
    val chance_of_snow: Int,
    val wind_kph: Double,
    val wind_degree: Int,
)
@Serializable
data class AstroDto(
    val sunrise: String,
    val sunset: String,
    val moon_illumination: Int,
)