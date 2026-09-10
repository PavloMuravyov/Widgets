package widgets.widgetsModule.widgets.celsius.domain.repository

import widgets.domain.dbus.Location.LocationData
import widgets.widgetsModule.widgets.celsius.domain.api.WeatherApi
import widgets.widgetsModule.widgets.celsius.domain.model.Weather
import widgets.widgetsModule.widgets.celsius.domain.model.toDomainModel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.milliseconds


class WeatherRepositoryImpl(
    private val api: WeatherApi,
    private val localizedLocationRepository: LocalizedLocationRepository
) : WeatherRepository {



    override fun weatherFlow(
        location: LocationData,
        intervalMillis: Long
    ): Flow<Weather> = flow {

        while (currentCoroutineContext().isActive) {
            try {
                val localizedLocation = localizedLocationRepository.getLocation(location)

                val response = api.getForecast(location)

                emit(response.toDomainModel(localizedLocation))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                e.printStackTrace()
            }

            delay(intervalMillis.milliseconds)
        }
    }
}
