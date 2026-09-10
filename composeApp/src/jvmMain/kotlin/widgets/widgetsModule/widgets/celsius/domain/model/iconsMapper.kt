package widgets.widgetsModule.widgets.celsius.domain.model

import org.jetbrains.compose.resources.DrawableResource
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.wi_cloudy
import widgets.composeapp.generated.resources.wi_day_cloudy
import widgets.composeapp.generated.resources.wi_day_cloudy_high
import widgets.composeapp.generated.resources.wi_day_fog
import widgets.composeapp.generated.resources.wi_day_hail
import widgets.composeapp.generated.resources.wi_day_lightning
import widgets.composeapp.generated.resources.wi_day_rain
import widgets.composeapp.generated.resources.wi_day_rain_mix
import widgets.composeapp.generated.resources.wi_day_showers
import widgets.composeapp.generated.resources.wi_day_sleet
import widgets.composeapp.generated.resources.wi_day_snow
import widgets.composeapp.generated.resources.wi_day_snow_thunderstorm
import widgets.composeapp.generated.resources.wi_day_sprinkle
import widgets.composeapp.generated.resources.wi_day_storm_showers
import widgets.composeapp.generated.resources.wi_day_sunny
import widgets.composeapp.generated.resources.wi_day_thunderstorm
import widgets.composeapp.generated.resources.wi_hail
import widgets.composeapp.generated.resources.wi_night_alt_partly_cloudy
import widgets.composeapp.generated.resources.wi_night_alt_snow
import widgets.composeapp.generated.resources.wi_night_clear
import widgets.composeapp.generated.resources.wi_night_cloudy_high
import widgets.composeapp.generated.resources.wi_night_fog
import widgets.composeapp.generated.resources.wi_night_hail
import widgets.composeapp.generated.resources.wi_night_lightning
import widgets.composeapp.generated.resources.wi_night_rain
import widgets.composeapp.generated.resources.wi_night_rain_mix
import widgets.composeapp.generated.resources.wi_night_showers
import widgets.composeapp.generated.resources.wi_night_sleet
import widgets.composeapp.generated.resources.wi_night_snow
import widgets.composeapp.generated.resources.wi_night_snow_thunderstorm
import widgets.composeapp.generated.resources.wi_night_sprinkle
import widgets.composeapp.generated.resources.wi_night_storm_showers
import widgets.composeapp.generated.resources.wi_night_thunderstorm
import widgets.composeapp.generated.resources.wi_rain
import widgets.composeapp.generated.resources.wi_snow_wind

object WeatherIconMapper {
    fun getIcon(code: Int, isDay: Boolean): DrawableResource = when (code) {
        // Clear / Sunny
        1000 -> if (isDay) Res.drawable.wi_day_sunny else Res.drawable.wi_night_clear
        // Partly cloudy
        1003 -> if (isDay) Res.drawable.wi_day_cloudy else Res.drawable.wi_night_alt_partly_cloudy
        // Cloudy
        1006 -> if (isDay) Res.drawable.wi_day_cloudy_high else Res.drawable.wi_night_cloudy_high
        // Overcast
        1009 -> Res.drawable.wi_cloudy
        // Mist
        1030 -> if (isDay) Res.drawable.wi_day_fog else Res.drawable.wi_night_fog
        // Patchy rain nearby
        1063 -> if (isDay) Res.drawable.wi_day_showers else Res.drawable.wi_night_showers
        // Patchy snow nearby
        1066 -> if (isDay) Res.drawable.wi_day_snow else Res.drawable.wi_night_snow
        // Patchy sleet nearby
        1069 -> if (isDay) Res.drawable.wi_day_sleet else Res.drawable.wi_night_sleet
        // Patchy freezing drizzle nearby
        1072 -> if (isDay) Res.drawable.wi_day_sleet else Res.drawable.wi_night_sleet
        // Thundery outbreaks nearby
        1087 -> if (isDay) Res.drawable.wi_day_lightning else Res.drawable.wi_night_lightning
        // Blowing snow
        1114 -> Res.drawable.wi_snow_wind
        // Blizzard
        1117 -> Res.drawable.wi_snow_wind
        // Fog
        1135 -> if (isDay) Res.drawable.wi_day_fog else Res.drawable.wi_night_fog
        // Freezing fog
        1147 -> if (isDay) Res.drawable.wi_day_fog else Res.drawable.wi_night_fog
        // Patchy light drizzle
        1150 -> if (isDay) Res.drawable.wi_day_sprinkle else Res.drawable.wi_night_sprinkle
        // Light drizzle
        1153 -> if (isDay) Res.drawable.wi_day_sprinkle else Res.drawable.wi_night_sprinkle
        // Freezing drizzle
        1168 -> if (isDay) Res.drawable.wi_day_sleet else Res.drawable.wi_night_sleet
        // Heavy freezing drizzle
        1171 -> if (isDay) Res.drawable.wi_day_sleet else Res.drawable.wi_night_sleet
        // Patchy light rain
        1180 -> if (isDay) Res.drawable.wi_day_showers else Res.drawable.wi_night_showers
        // Light rain
        1183 -> if (isDay) Res.drawable.wi_day_rain else Res.drawable.wi_night_rain
        // Moderate rain at times
        1186 -> if (isDay) Res.drawable.wi_day_rain else Res.drawable.wi_night_rain
        // Moderate rain
        1189 -> if (isDay) Res.drawable.wi_day_rain else Res.drawable.wi_night_rain
        // Heavy rain at times
        1192 -> if (isDay) Res.drawable.wi_day_rain else Res.drawable.wi_night_rain
        // Heavy rain
        1195 -> Res.drawable.wi_rain
        // Light freezing rain
        1198 -> if (isDay) Res.drawable.wi_day_rain_mix else Res.drawable.wi_night_rain_mix
        // Moderate or heavy freezing rain
        1201 -> if (isDay) Res.drawable.wi_day_rain_mix else Res.drawable.wi_night_rain_mix
        // Patchy light snow
        1204 -> if (isDay) Res.drawable.wi_day_sleet else Res.drawable.wi_night_sleet
        // Light sleet
        1207 -> if (isDay) Res.drawable.wi_day_sleet else Res.drawable.wi_night_sleet
        // Moderate or heavy sleet
        1210 -> if (isDay) Res.drawable.wi_day_sleet else Res.drawable.wi_night_sleet
        // Patchy light snow
        1213 -> if (isDay) Res.drawable.wi_day_snow else Res.drawable.wi_night_snow
        // Light snow
        1216 -> if (isDay) Res.drawable.wi_day_snow else Res.drawable.wi_night_snow
        // Patchy moderate snow
        1219 -> if (isDay) Res.drawable.wi_day_snow else Res.drawable.wi_night_snow
        // Moderate snow
        1222 -> if (isDay) Res.drawable.wi_day_snow else Res.drawable.wi_night_snow
        // Patchy heavy snow
        1225 -> Res.drawable.wi_day_snow
        // Heavy snow
        1237 -> Res.drawable.wi_hail
        // Light rain shower
        1240 -> if (isDay) Res.drawable.wi_day_showers else Res.drawable.wi_night_showers
        // Moderate or heavy rain shower
        1243 -> if (isDay) Res.drawable.wi_day_rain else Res.drawable.wi_night_rain
        // Torrential rain shower
        1246 -> Res.drawable.wi_rain
        // Light sleet showers
        1249 -> if (isDay) Res.drawable.wi_day_sleet else Res.drawable.wi_night_sleet
        // Moderate or heavy sleet showers
        1252 -> if (isDay) Res.drawable.wi_day_sleet else Res.drawable.wi_night_sleet
        // Light snow showers
        1255 -> if (isDay) Res.drawable.wi_day_snow else Res.drawable.wi_night_alt_snow
        // Moderate or heavy snow showers
        1258 -> if (isDay) Res.drawable.wi_day_snow else Res.drawable.wi_night_alt_snow
        // Light showers of ice pellets
        1261 -> if (isDay) Res.drawable.wi_day_hail else Res.drawable.wi_night_hail
        // Moderate or heavy showers of ice pellets
        1264 -> if (isDay) Res.drawable.wi_day_hail else Res.drawable.wi_night_hail
        // Patchy light rain with thunder
        1273 -> if (isDay) Res.drawable.wi_day_storm_showers else Res.drawable.wi_night_storm_showers
        // Moderate or heavy rain with thunder
        1276 -> if (isDay) Res.drawable.wi_day_thunderstorm else Res.drawable.wi_night_thunderstorm
        // Patchy light snow with thunder
        1279 -> if (isDay) Res.drawable.wi_day_snow_thunderstorm else Res.drawable.wi_night_snow_thunderstorm
        // Moderate or heavy snow with thunder
        1282 -> if (isDay) Res.drawable.wi_day_snow_thunderstorm else Res.drawable.wi_night_snow_thunderstorm

        else -> if (isDay) Res.drawable.wi_day_sunny else Res.drawable.wi_night_clear
    }
}


