package widgets.widgetsModule.widgets.celsius.domain.model

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


fun ForecastResponseDto.toDomainModel(localizedLocation: LocalizedLocation): Weather {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val currentTime = ZonedDateTime.now(ZoneId.of(location.tz_id)).toLocalDateTime()


    val allHours = forecast.forecastday
        .take(2) // сьогодні + завтра
        .flatMap { it.hour }


    val next12HoursForecast = allHours
        .map { it to LocalDateTime.parse(it.time, formatter) }
        .filter { (_, time) ->
            time.isAfter(currentTime) &&
                    time.isBefore(currentTime.plusHours(12)) &&
                    time.hour % 2 == 0
        }
        .map { it.first }
        .take(6)
        .toMutableList()

    if (next12HoursForecast.size < 6) {
        val nextDayHours = forecast.forecastday.getOrNull(1)?.hour
            ?.map { it to LocalDateTime.parse(it.time, formatter) }
            ?.filter { (_, time) -> time.hour % 2 == 0 }
            ?.map { it.first }
            ?.toMutableList() ?: mutableListOf()

        val missing = 6 - next12HoursForecast.size
        next12HoursForecast.addAll(nextDayHours.take(missing))
    }

    val hourlyForecast = next12HoursForecast.map {
        HourlyForecast(
            time = it.time,
            temperature = it.temp_c,
            conditionText = it.condition.text,
            conditionIconUrl ="https:${it.condition.icon}",
            conditionCode = it.condition.code,
            chanceOfRain = it.chance_of_rain,
            chanceOfSnow = it.chance_of_snow,
            windSpeed = it.wind_kph,
            windDegree = it.wind_degree,
        )
    }

    val today = forecast.forecastday.firstOrNull()


    val countryMap = Locale.getISOCountries().associateWith { code ->
        Locale("", code).getDisplayCountry(Locale.ENGLISH)
    }

    val countryCode = countryMap.entries.firstOrNull {
        location.country.contains(it.value, ignoreCase = true) ||
                it.value.contains(location.country, ignoreCase = true)
    }?.key

    return Weather(
        CurrentWeather(
            locationName = localizedLocation.name ?: location.name,
            country = countryCode ?:"",
            fullCountyName = localizedLocation.country ?: location.country,
            temperature = current.temp_c,
            conditionText = current.condition.text,
            conditionIconUrl = "https:${current.condition.icon}",
            conditionCode = current.condition.code,
            windSpeed = (current.wind_kph * 1000.0) / 3600.0 ,
            windDirection = current.wind_dir,
            windDegree = current.wind_degree,
            isDay = if(current.is_day == 1) true else false,
            sunrise = today?.astro?.sunrise,
            sunset = today?.astro?.sunset,
            moonIllumination = today?.astro?.moon_illumination ?: 100
        ),

        forecastDays = forecast.forecastday.map { day ->
            ForecastDay(
                date = day.date,
                maxTemp = day.day.maxtemp_c,
                minTemp = day.day.mintemp_c,
                conditionText = day.day.condition.text,
                conditionIconUrl = "https:${day.day.condition.icon }",
                conditionCode = day.day.condition.code,
                sunrise = day.astro.sunrise,
                sunset = day.astro.sunset
            )
        },
        hourlyForecast = hourlyForecast
    )
}