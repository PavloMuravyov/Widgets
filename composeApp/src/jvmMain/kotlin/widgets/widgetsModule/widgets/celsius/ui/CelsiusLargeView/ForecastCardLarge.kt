package widgets.widgetsModule.widgets.celsius.ui.CelsiusLargeView

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import widgets.Theme.Colors
import widgets.widgetsModule.widgets.celsius.domain.model.HourlyForecast
import widgets.widgetsModule.widgets.celsius.ui.Composables.ConditionIcon
import widgets.widgetsModule.widgets.celsius.ui.NumericText
import widgets.widgetsModule.widgets.celsius.ui.TextLabel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ForecastCardLarge(
    forecastItem: HourlyForecast,
    width: Dp,
    textSize: TextUnit,
    timeTextSize: TextUnit,
    iconSize: Dp,
    degreeSymbol: String,
    formatter: DateTimeFormatter,
    density: Density,
) {


    val label = remember(forecastItem.temperature) {
        "${forecastItem.temperature.toInt()}$degreeSymbol"
    }

    val time = remember(forecastItem.time) {
        LocalDateTime.parse(forecastItem.time, formatter).format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    Column(
        Modifier.fillMaxHeight()
            .width(width),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        TextLabel(timeTextSize, time, color = Colors.semiTransparentWhite)
        ConditionIcon(forecastItem.conditionCode, iconSize)
        NumericText(textSize, label, color = Colors.solidWhite, density = density)

    }
}
