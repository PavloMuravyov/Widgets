package widgets.widgetsModule.widgets.celsius

import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.lifecycle.viewModelScope
import widgets.domain.AppSettings.AppSettingsRepository
import widgets.domain.DensityProvider.DensityProvider
import widgets.domain.TimeService.TimeService
import widgets.domain.dbus.Location.LocationRepository
import widgets.domain.dbus.Network.InternetConnectivityMonitor
import widgets.widgetsModule.data.models.WidgetsTypes
import widgets.widgetsModule.managers.GridPositioningManager.GridPositioningManager
import widgets.widgetsModule.managers.SystemDataManager.SystemDataManager
import widgets.widgetsModule.widgets.WidgetBaseViewModel
import widgets.widgetsModule.widgets.celsius.domain.model.AstroData
import widgets.widgetsModule.widgets.celsius.domain.model.CurrentWeather
import widgets.widgetsModule.widgets.celsius.domain.model.ForecastDay
import widgets.widgetsModule.widgets.celsius.domain.model.HourlyForecast
import widgets.widgetsModule.widgets.celsius.domain.model.Weather
import widgets.widgetsModule.widgets.celsius.domain.repository.AstroDataRepository
import widgets.widgetsModule.widgets.celsius.domain.repository.AstroDataRepositoryImpl
import widgets.widgetsModule.widgets.celsius.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class CelsiusViewModel(
    private val weatherRepository: WeatherRepository,
    private val astroDataRepository: AstroDataRepository,
    systemDataManager: SystemDataManager,
    appSettingsRepository: AppSettingsRepository,
    gridPositioningManager: GridPositioningManager,
    densityProvider: DensityProvider,

    ) : WidgetBaseViewModel(
    WidgetsTypes.Celsius,
    appSettingsRepository,
    gridPositioningManager,
    densityProvider,
    systemDataManager
) {



    val astroData: StateFlow<AstroData?> = systemDataManager.locationState
        .filterNotNull()
        .flatMapLatest { locationData ->
            astroDataRepository.observeAstroData(locationData)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)


   private val weatherState: StateFlow<Weather?> = combine(
        systemDataManager.connectivityState,
        systemDataManager.locationState
    ) { connected, location ->
        connected to location
    }
        .flatMapLatest { (connected, location) ->
            if (connected && location != null) {
                weatherRepository.weatherFlow(location)
            } else {
                emptyFlow()
            }
        }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )


    val moonIllumination: StateFlow<Int?> = weatherState
        .map { it?.currentWeather?.moonIllumination }
        .distinctUntilChanged()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val locationInfo: StateFlow<LocationInfo?> = weatherState
        .map { it?.currentWeather?.toLocationInfo() }
        .distinctUntilChanged()  // ← перекомпозиція тільки якщо змінилось
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val currentCondition: StateFlow<CurrentCondition?> = weatherState
        .map { it?.currentWeather?.toCondition() }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val temperature: StateFlow<TemperatureData?> = weatherState
        .map { it?.currentWeather?.toTemperature() }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val windData: StateFlow<WindData?> = weatherState
        .map { it?.currentWeather?.toWindData() }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val forecastDays: StateFlow<List<ForecastDay>> = weatherState
        .map { it?.forecastDays ?: emptyList() }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    val forecastToday: StateFlow<ForecastDay?> = forecastDays
        .map {
            if(it.isNotEmpty()){
                it[0]
            } else null
        }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val hourlyForecast: StateFlow<List<HourlyForecast>> = weatherState
        .map { it?.hourlyForecast ?: emptyList() }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())



    data class LocationInfo(val city: String, val country: String, val countryCode: String)
    data class CurrentCondition(val text: String, val iconUrl: String, val isDay: Boolean, val code: Int)
    data class TemperatureData(val current: Double)

    data class WindData(val windSpeed: Int, val windDirection: String, val windDegrees: Int)
    fun CurrentWeather.toLocationInfo() = LocationInfo(locationName, fullCountyName, country)
    fun CurrentWeather.toCondition() = CurrentCondition(conditionText, conditionIconUrl, isDay, conditionCode)
    fun CurrentWeather.toTemperature() = TemperatureData(temperature)
    fun CurrentWeather.toWindData() = WindData(windSpeed.toInt(), windDirection, windDegree)



/*
    val dayNightDuration: StateFlow<DayNightDuration?> = combine(
        weatherState,
        forecastDays
    ) { weather, forecast ->
        val currentWeather = weather?.currentWeather ?: return@combine null
        if (forecast.size < 2) return@combine null

        val today = forecast[0]
        val tomorrow = forecast[1]

        val sunriseToday = today.sunrise?.parseTime() ?: return@combine null
        val sunsetToday = today.sunset?.parseTime() ?: return@combine null
        val sunriseTomorrow = tomorrow.sunrise?.parseTime() ?: return@combine null

        val now = LocalTime.now()

        if (currentWeather.isDay) {
            val duration = Duration.between(sunriseToday, sunsetToday).toMinutes().toInt()
            DayNightDuration(isDay = true, durationMinutes = duration)
        } else {
            val duration = if (now.isAfter(sunsetToday)) {
                val toMidnight = Duration.between(now, LocalTime.MIDNIGHT).toMinutes()
                val fromMidnight = Duration.between(LocalTime.MIDNIGHT, sunriseTomorrow).toMinutes()
                (toMidnight + fromMidnight).toInt()
            } else {

                Duration.between(now, sunriseToday).toMinutes().toInt()
            }
            DayNightDuration(isDay = false, durationMinutes = duration)
        }
    }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)


    private fun String.parseTime(): LocalTime? = try {
        LocalTime.parse(this.trim(), DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH))
    } catch (e: Exception) { null }

*/

    override fun onCleared() {


    }

}

/*

data class DayNightDuration(
    val isDay: Boolean,
    val durationMinutes: Int
)*/
