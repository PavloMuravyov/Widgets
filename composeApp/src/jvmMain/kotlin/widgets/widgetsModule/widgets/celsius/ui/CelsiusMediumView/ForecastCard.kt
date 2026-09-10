package widgets.widgetsModule.widgets.celsius.ui.CelsiusMediumView

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import widgets.Theme.Colors
import widgets.widgetsModule.widgets.celsius.ui.Composables.ConditionIcon
import widgets.widgetsModule.widgets.celsius.domain.model.HourlyForecast
import widgets.widgetsModule.widgets.celsius.ui.TextLabel
import org.jetbrains.compose.resources.stringResource
import widgets.composeapp.generated.resources.RainSchemeIcon
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.SnowSchemeIcon
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ForecastCardMedium(
    forecastItem: HourlyForecast,
    width: Dp,
    textSize: TextUnit,
    textSizeSmall: TextUnit,
    iconSize: Dp,
    degreeSymbol: String,
    formatter: DateTimeFormatter
) {
    val label = remember(forecastItem.temperature) {
        "${forecastItem.temperature.toInt()}$degreeSymbol"
    }

    val time = LocalDateTime.parse(forecastItem.time, formatter)
        .format(DateTimeFormatter.ofPattern("HH:mm"))

    val rainSchemeIcon = stringResource(Res.string.RainSchemeIcon)
    val snowSchemeIcon = stringResource(Res.string.SnowSchemeIcon)

    TooltipArea(
        tooltip = {
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = Colors.semitransparentBlack,
                elevation = 0.dp
            ) {
                Column(Modifier.padding(8.dp)) {
                    TextLabel(
                        textSize,
                        time,
                        color = Colors.solidWhite
                    )
                    TextLabel(
                        textSize,
                        label,
                        color = Colors.solidWhite
                    )
                    TextLabel(
                        textSizeSmall,
                        forecastItem.conditionText,
                        color = Colors.solidWhite
                    )
                    if (forecastItem.chanceOfRain > 0) {
                        TextLabel(
                            textSizeSmall,
                            "$rainSchemeIcon ${forecastItem.chanceOfRain}%",
                            color = Colors.solidWhite
                        )
                    }
                    if (forecastItem.chanceOfSnow > 0) {
                        TextLabel(
                            textSizeSmall,
                            "$snowSchemeIcon ${forecastItem.chanceOfSnow}%",
                            color = Colors.solidWhite
                        )
                    }
                }
            }
        },
    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .width(width),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TextLabel(textSizeSmall, time)

            }

            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {

                ConditionIcon(
                    forecastItem.conditionCode,
                    iconSize
                )
            }
            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TextLabel(textSize, label)
            }
        }

    }
}

