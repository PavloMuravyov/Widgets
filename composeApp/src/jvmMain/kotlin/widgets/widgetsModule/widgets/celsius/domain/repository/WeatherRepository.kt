package widgets.widgetsModule.widgets.celsius.domain.repository

import widgets.domain.dbus.Location.LocationData
import widgets.widgetsModule.widgets.celsius.domain.model.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


interface WeatherRepository{

    fun weatherFlow(location: LocationData, intervalMillis: Long = 900_000) : Flow<Weather>

}