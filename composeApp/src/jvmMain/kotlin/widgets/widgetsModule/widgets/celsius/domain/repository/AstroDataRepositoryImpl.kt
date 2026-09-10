package widgets.widgetsModule.widgets.celsius.domain.repository

import widgets.domain.dbus.Location.LocationData
import widgets.widgetsModule.widgets.celsius.domain.model.AstroData
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import org.shredzone.commons.suncalc.SunTimes
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes


class AstroDataRepositoryImpl : AstroDataRepository {

    override fun observeAstroData(locationData: LocationData): Flow<AstroData> = flow {
        while (currentCoroutineContext().isActive) {
            locationData.timeZoneId?.let { timeZone ->
                val astroData = computeAstroData(locationData, timeZone)
                emit(astroData.data)
                delay(astroData.delayUntilNextEvent)
            } ?: delay(10.minutes)
        }
    }

    private fun computeAstroData(
        locationData: LocationData,
        timeZone: String,
    ): AstroDataResult {
        val zoneId = ZoneId.of(timeZone)
        val zonedNow = ZonedDateTime.now(zoneId)
        val midnight = zonedNow.toLocalDate().plusDays(1).atStartOfDay(zoneId)
        val sunTimes = computeSunTimes(locationData, timeZone, zonedNow)
        val allEvents = listOfNotNull(
            sunTimes.previous.rise, sunTimes.previous.set,
            sunTimes.today.rise, sunTimes.today.set,
            sunTimes.next.rise, sunTimes.next.set,
        ).sorted()

        val lastEvent = allEvents.lastOrNull { it.isBefore(zonedNow) }
        val nextEvent = allEvents.firstOrNull { it.isAfter(zonedNow) }

        val isDay = calculateIsDay(sunTimes.today, lastEvent)

        return AstroDataResult(
            data = AstroData(
                isDay = isDay,
                length = calculateLengthMinutes(sunTimes, zonedNow, midnight, isDay),
                sunrise = if (!isDay) sunTimes.next.rise else sunTimes.today.rise,
                sunset = sunTimes.today.set,
            ),
            delayUntilNextEvent = calculateDelay(sunTimes.today, zonedNow, midnight, nextEvent),
        )
    }

    private fun computeSunTimes(
        locationData: LocationData,
        timeZone: String,
        zonedNow: ZonedDateTime,
    ): TriDaySunTimes {
        val today = zonedNow.toLocalDate()

        fun compute(date: LocalDate) = SunTimes.compute()
            .timezone(timeZone)
            .on(date)
            .at(locationData.latitude, locationData.longitude)
            .execute()
        return TriDaySunTimes(
            previous = compute(today.minusDays(1)),
            today = compute(today),
            next = compute(today.plusDays(1)),
        )
    }

    private fun calculateIsDay(today: SunTimes, lastEvent: ZonedDateTime?): Boolean = when {
        today.isAlwaysUp -> true
        today.isAlwaysDown -> false
        else -> lastEvent == today.rise
    }

    private fun calculateLengthMinutes(
        sunTimes: TriDaySunTimes,
        zonedNow: ZonedDateTime,
        midnight: ZonedDateTime,
        isDay: Boolean,
    ): Int {
        val today = sunTimes.today
        return when {
            today.isAlwaysUp -> 24 * 60
            today.isAlwaysDown -> 0
            isDay -> {
                if (today.rise != null && today.set != null)
                    ChronoUnit.MINUTES.between(today.rise, today.set).toInt()
                else 1
            }
            else -> {

                val isBeforeMidnight = zonedNow < midnight
                val nightStart = if (isBeforeMidnight) today.set else sunTimes.previous.set
                val nightEnd = if (isBeforeMidnight) sunTimes.next.rise else today.rise
                if (nightStart != null && nightEnd != null) {

                    ChronoUnit.MINUTES.between(nightStart, nightEnd).toInt()
                } else {
                    1
                }
            }
        }
    }

    private fun calculateDelay(
        today: SunTimes,
        zonedNow: ZonedDateTime,
        midnight: ZonedDateTime,
        nextEvent: ZonedDateTime?,
    ): Duration = when {
        today.isAlwaysUp || today.isAlwaysDown ->
            maxOf(ChronoUnit.MILLIS.between(zonedNow, midnight), 0L).milliseconds
        nextEvent != null ->
            maxOf(ChronoUnit.MILLIS.between(zonedNow, nextEvent), 0L).milliseconds
        else -> 1.hours
    }
}
private data class TriDaySunTimes(
    val previous: SunTimes,
    val today: SunTimes,
    val next: SunTimes,
)
private data class AstroDataResult(
    val data: AstroData,
    val delayUntilNextEvent: Duration,
)


/*


class AstroDataRepositoryImpl : AstroDataRepository {
    private val _astroData = MutableStateFlow<AstroData?>(null)
    override val astroData: StateFlow<AstroData?> = _astroData




    override suspend fun getAstroData(locationData: LocationData) {
        while (currentCoroutineContext().isActive) {
            locationData.timeZoneId?.let { timeZone ->
                updateAstroData(locationData, timeZone)
            } ?: delay(600000.milliseconds)
        }
    }

    private suspend fun updateAstroData(locationData: LocationData, timeZone: String) {
        val zoneId = ZoneId.of(timeZone)
        val zonedNow = ZonedDateTime.now(zoneId)
        val midnight = zonedNow.toLocalDate().plusDays(1).atStartOfDay(zoneId)

        val sunTimes = computeSunTimes(locationData, timeZone, zonedNow)

        val allEvents = listOfNotNull(
            sunTimes.previous.rise, sunTimes.previous.set,
            sunTimes.today.rise, sunTimes.today.set,
            sunTimes.next.rise, sunTimes.next.set,
        ).sorted()

        val lastEvent = allEvents.lastOrNull { it.isBefore(zonedNow) }
        val nextEvent = allEvents.firstOrNull { it.isAfter(zonedNow) }

        _astroData.value = AstroData(
            isDay = calculateIsDay(sunTimes.today, lastEvent),
            length = calculateLengthMinutes(sunTimes, zonedNow, midnight),
            sunrise = sunTimes.today.rise,
            sunset = sunTimes.today.set,
        )

        delay(calculateDelay(sunTimes.today, zonedNow, midnight, nextEvent))
    }

    private fun computeSunTimes(
        locationData: LocationData,
        timeZone: String,
        zonedNow: ZonedDateTime,
    ): TriDaySunTimes {
        val today = zonedNow.toLocalDate()

        fun compute(date: LocalDate) = SunTimes.compute()
            .timezone(timeZone)
            .on(date)
            .at(locationData.latitude, locationData.longitude)
            .execute()
        return TriDaySunTimes(
            previous = compute(today.minusDays(1)),
            today = compute(today),
            next = compute(today.plusDays(1)),
        )
    }

    private fun calculateIsDay(today: SunTimes, lastEvent: ZonedDateTime?): Boolean = when {
        today.isAlwaysUp -> true
        today.isAlwaysDown -> false
        else -> lastEvent == today.rise
    }

    private fun calculateLengthMinutes(
        sunTimes: TriDaySunTimes,
        zonedNow: ZonedDateTime,
        midnight: ZonedDateTime,
    ): Int {
        val today = sunTimes.today
        return when {
            today.isAlwaysUp -> 24 * 60
            today.isAlwaysDown -> 0
            today.rise != null && today.set != null ->
                ChronoUnit.MINUTES.between(today.rise, today.set).toInt()
            else -> {
                val isBeforeMidnight = zonedNow < midnight
                val nightStart = if (isBeforeMidnight) today.set else sunTimes.previous.set
                val nightEnd = if (isBeforeMidnight) sunTimes.next.rise else today.rise
                if (nightStart != null && nightEnd != null) {
                    ChronoUnit.MINUTES.between(nightStart, nightEnd).toInt()
                } else {
                    1
                }
            }
        }
    }

    private fun calculateDelay(
        today: SunTimes,
        zonedNow: ZonedDateTime,
        midnight: ZonedDateTime,
        nextEvent: ZonedDateTime?,
    ): Duration = when {
        today.isAlwaysUp || today.isAlwaysDown ->
            ChronoUnit.MILLIS.between(zonedNow, midnight).milliseconds
        nextEvent != null ->
            ChronoUnit.MILLIS.between(zonedNow, nextEvent).milliseconds
        else -> 1.hours
    }
}

private data class TriDaySunTimes(
    val previous: SunTimes,
    val today: SunTimes,
    val next: SunTimes,
)

*/




/*

class AstroDataRepositoryImpl : AstroDataRepository {


    private val _astroData = MutableStateFlow<AstroData?>(null)
    override val astroData: StateFlow<AstroData?> = _astroData


    override suspend fun getAstroData(locationData: LocationData) {
        while (currentCoroutineContext().isActive) {
            locationData.timeZoneId?.let { timeZone ->
                val zoneId = ZoneId.of(timeZone)
                val zonedNow = ZonedDateTime.now(zoneId)
                val today = zonedNow.toLocalDate()

                val sunTimesToday = SunTimes.compute()
                    .timezone(timeZone)
                    .on(today)
                    .at(locationData.latitude, locationData.longitude)
                    .execute()

                val nextDayTimes = SunTimes.compute()
                    .timezone(timeZone)
                    .on(today.plusDays(1))
                    .at(locationData.latitude, locationData.longitude)
                    .execute()

                val previousDayTimes = SunTimes.compute()
                    .timezone(timeZone)
                    .on(today.minusDays(1))
                    .at(locationData.latitude, locationData.longitude)
                    .execute()

                val allEvents = listOfNotNull(
                    previousDayTimes.rise,
                    previousDayTimes.set,
                    sunTimesToday.rise,
                    sunTimesToday.set,
                    nextDayTimes.rise,
                    nextDayTimes.set,
                ).sorted()


                val lastEvent = allEvents.lastOrNull { it.isBefore(zonedNow) }
                val nextEvent = allEvents.firstOrNull { it.isAfter(zonedNow) }

                val todaySunrise = sunTimesToday.rise
                val todaySunset = sunTimesToday.set

                val midnight = zonedNow.toLocalDate().plusDays(1).atStartOfDay(zoneId)

                val lengthMinutes = when {
                    sunTimesToday.isAlwaysUp -> 24 * 60

                    sunTimesToday.isAlwaysDown -> 0

                    todaySunrise != null && todaySunset != null ->
                        ChronoUnit.MINUTES.between(todaySunrise, todaySunset).toInt()

                    else -> {
                        val isBeforeMidnight = zonedNow < midnight

                        val nightStart = if (isBeforeMidnight) todaySunset else previousDayTimes.set
                        val nightEnd = if (isBeforeMidnight) nextDayTimes.rise else todaySunrise
                        if (nightStart != null && nightEnd != null) {
                            ChronoUnit.MINUTES.between(nightStart, nightEnd).toInt()
                        } else {
                            0
                        }
                    }
                }

                val isDay = when {
                    sunTimesToday.isAlwaysUp -> true
                    sunTimesToday.isAlwaysDown -> false
                    else -> lastEvent == sunTimesToday.rise
                }


                _astroData.value = AstroData(
                    isDay = isDay,
                    length = lengthMinutes,
                    sunrise = todaySunrise,
                    sunset = todaySunset,
                )

                val delayMillis = when {
                    sunTimesToday.isAlwaysUp || sunTimesToday.isAlwaysDown -> {

                        ChronoUnit.MILLIS.between(zonedNow, midnight)
                    }
                    nextEvent != null -> ChronoUnit.MILLIS.between(zonedNow, nextEvent)
                    else -> 60 * 60 * 1000L
                }

                delay(delayMillis.milliseconds)
            } ?: delay((60 * 1000L).milliseconds)
        }
    }
}*/
