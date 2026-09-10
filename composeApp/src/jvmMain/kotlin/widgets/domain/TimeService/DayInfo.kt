package widgets.domain.TimeService

import widgets.domain.SystemLocale
import kotlinx.serialization.Serializable
import java.util.Locale


@Serializable
data class DayInfo(
    val dayNum: String,
    val monthNum: String,
    val year: String,

)

fun DayInfo.toEpochDay(): Long {
    return java.time.LocalDate.of(year.toInt(), monthNum.toInt(), dayNum.toInt())
        .toEpochDay()
}

fun DayInfo.isOlderThan(other: DayInfo, days: Int): Boolean {
    return other.toEpochDay() - this.toEpochDay() > days
}

fun DayInfo.toLocalDate(): java.time.LocalDate? {
    val y = year.toIntOrNull() ?: return null
    val m = monthNum.toIntOrNull() ?: return null
    val d = dayNum.toIntOrNull() ?: return null
    return java.time.LocalDate.of(y, m, d)
}

fun DayInfo.localizedDayNameShort(locale: Locale = SystemLocale.locale): String =
    toLocalDate()?.dayOfWeek?.getDisplayName(java.time.format.TextStyle.SHORT, locale) ?: ""

fun DayInfo.localizedDayNameFull(locale: Locale = SystemLocale.locale): String =
    toLocalDate()?.dayOfWeek?.getDisplayName(java.time.format.TextStyle.FULL, locale) ?: ""

fun DayInfo.localizedMonth(locale: Locale = SystemLocale.locale): String =
   toLocalDate()?.month?.getDisplayName(java.time.format.TextStyle.FULL, locale) ?: ""

