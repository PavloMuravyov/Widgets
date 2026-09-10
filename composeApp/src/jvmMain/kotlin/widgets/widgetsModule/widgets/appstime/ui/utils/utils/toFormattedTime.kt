package widgets.widgetsModule.widgets.appstime.ui.utils.utils

import widgets.domain.SystemLocale
import com.ibm.icu.text.MeasureFormat
import com.ibm.icu.util.Measure
import com.ibm.icu.util.MeasureUnit
import com.ibm.icu.util.ULocale
import java.util.Locale

fun Int.toFormattedTime(
    locale: Locale = SystemLocale.locale,
    multiline: Boolean = false
): String {
    val hours = this / 60
    val minutes = this % 60
    val format = MeasureFormat.getInstance(
        ULocale.forLocale(locale),
        MeasureFormat.FormatWidth.SHORT
    )
    return when {
        hours > 0 -> if (multiline) {
            "${format.formatMeasures(Measure(hours, MeasureUnit.HOUR))}\n${format.formatMeasures(Measure(minutes, MeasureUnit.MINUTE))}"
        } else {
            format.formatMeasures(
                Measure(hours, MeasureUnit.HOUR),
                Measure(minutes, MeasureUnit.MINUTE)
            )
        }
        else -> format.formatMeasures(
            Measure(minutes, MeasureUnit.MINUTE)
        )
    }
}

fun Int.toFormattedHours(
    locale: Locale = SystemLocale.locale
): String {
    val format = MeasureFormat.getInstance(
        ULocale.forLocale(locale),
        MeasureFormat.FormatWidth.SHORT
    )
    return format.formatMeasures(Measure(this, MeasureUnit.HOUR))
}