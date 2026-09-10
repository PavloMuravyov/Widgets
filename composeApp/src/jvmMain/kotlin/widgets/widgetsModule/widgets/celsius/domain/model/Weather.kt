package widgets.widgetsModule.widgets.celsius.domain.model

data class Weather(
    val currentWeather: CurrentWeather,
    val forecastDays: List<ForecastDay>,
    val hourlyForecast: List<HourlyForecast> // нове поле
)

data class CurrentWeather(
    val locationName: String,
    val country: String,
    val fullCountyName: String,
    val temperature: Double,
    val conditionText: String,
    val conditionIconUrl: String,
    val conditionCode: Int = 1000,
    val windSpeed: Double,
    val windDirection: String,
    val windDegree: Int,
    val isDay: Boolean,
    val sunrise: String?,
    val sunset: String?,
    val moonIllumination: Int,

    )

data class ForecastDay(
    val date: String,
    val maxTemp: Double,
    val minTemp: Double,
    val conditionText: String,
    val conditionIconUrl: String,
    val conditionCode: Int = 1000,
    val sunrise: String?,
    val sunset: String?,

    )

data class HourlyForecast(
    val time: String,
    val temperature: Double,
    val conditionText: String,
    val conditionIconUrl: String,
    val conditionCode: Int = 1000,
    val chanceOfRain: Int,
    val chanceOfSnow: Int,
    val windSpeed: Double,
    val windDegree: Int,
)


