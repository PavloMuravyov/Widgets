package widgets.widgetsModule.widgets.celsius.ui.CelsiusMediumView

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import widgets.Theme.Colors
import widgets.Theme.Paddings
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.degreeSymbol
import widgets.widgetsModule.data.models.WidgetSizes
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel
import widgets.widgetsModule.widgets.celsius.ui.CelsiusDimens
import widgets.widgetsModule.widgets.celsius.ui.CelsiusLargeView.ForecastCardLarge
import java.time.format.DateTimeFormatter

@Composable
fun HoursForecast(
    celsiusViewModel: CelsiusViewModel,
    dimens: CelsiusDimens,
    formatter: DateTimeFormatter) {


    val density = celsiusViewModel.density.value
    val widgetSize by celsiusViewModel.widgetSize.collectAsState()
    val forecast by celsiusViewModel.hourlyForecast.collectAsState()

    val degreeSymbol = stringResource(Res.string.degreeSymbol)

    LazyRow (
        Modifier
            .fillMaxSize()
            .padding(Paddings.small),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {


        itemsIndexed(forecast) { index, forecastItem ->

            when (widgetSize){
                WidgetSizes.Large -> {
                    ForecastCardLarge(
                        forecastItem,
                        dimens.forecastCardLargeWidth,
                        dimens.forecastCardTextSizeLarge,
                        dimens.baseInfoTextSizeLarge,
                        dimens.forecastCardIconSizeLarge,
                        degreeSymbol,
                        formatter,
                        density
                    )

                }
                WidgetSizes.Medium -> {
                    ForecastCardMedium(
                        forecastItem,
                        dimens.forecastCardMediumWidth,
                        dimens.baseInfoTextSizeMedium,
                        dimens.smallInfoTextSizeMedium,
                        dimens.forecastCardIconSizeMedium,
                        degreeSymbol,
                        formatter)
                }
                WidgetSizes.Small -> {

                }
            }



            if (index < forecast.lastIndex) {
                Spacer(Modifier
                    .fillMaxHeight(0.8f)
                    .width(1.dp)
                    .background(Colors.semiTransparentWhite))
            }
        }

    }

}
