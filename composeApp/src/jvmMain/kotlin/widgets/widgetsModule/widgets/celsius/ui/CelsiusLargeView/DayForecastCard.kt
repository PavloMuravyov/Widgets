package widgets.widgetsModule.widgets.celsius.ui.CelsiusLargeView

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import widgets.Theme.Colors
import widgets.Theme.Paddings
import widgets.domain.SystemLocale
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel.TemperatureData
import widgets.widgetsModule.widgets.celsius.domain.model.ForecastDay
import widgets.widgetsModule.widgets.celsius.ui.Composables.ConditionIcon
import widgets.widgetsModule.widgets.celsius.ui.NumericText
import widgets.widgetsModule.widgets.celsius.ui.TextLabel
import java.time.LocalDate
import java.time.format.TextStyle


@Composable
fun CelsiusLargeScope.DayForecastCard(
    forecastItem: ForecastDay,
    textSize: TextUnit,
    iconSize: Dp,
    degreeSymbol: String,
    currentWeather: TemperatureData,
    globalMin: Double,
    globalMax: Double,
    cardHeight: Dp,
    lineHeight: Dp,
    density: Density,
) {
    Row(
        modifier = Modifier
            .height(cardHeight)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            Modifier
                .weight(1f)
                .background(Color.Transparent)
        ) {


            val dayName = remember(forecastItem.date) {
                val date = LocalDate.parse(forecastItem.date)
                date.dayOfWeek.getDisplayName(TextStyle.FULL, SystemLocale.locale)
                    .replaceFirstChar { it.uppercase() }
            }

            TextLabel(textSize, dayName, color = Colors.solidWhite)

        }
        Row(
            modifier = Modifier.weight(2f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                Modifier.wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {


                ConditionIcon(forecastItem.conditionCode, iconSize)

                val labelMin = remember(forecastItem.minTemp.toInt()) {
                    "${forecastItem.minTemp.toInt()}$degreeSymbol"
                }

                Spacer(Modifier.width(Paddings.large))

                NumericText(textSize, labelMin, color = Colors.solidWhite, density = density)
            }


            BarTemperatureIndicator(
                forecastItem.minTemp,
                forecastItem.maxTemp,
                currentWeather,
                globalMin,
                globalMax,
                lineHeight,
                Modifier.fillMaxWidth(0.75f),
            )

            val labelMax = remember(forecastItem.maxTemp.toInt()) {
                "${forecastItem.maxTemp.toInt()}$degreeSymbol"
            }
            NumericText(textSize, labelMax, color = Colors.solidWhite, density = density)
        }

    }
}
