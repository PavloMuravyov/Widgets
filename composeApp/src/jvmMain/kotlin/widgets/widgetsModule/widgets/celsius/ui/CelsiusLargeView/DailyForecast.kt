package widgets.widgetsModule.widgets.celsius.ui.CelsiusLargeView

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import widgets.Theme.Paddings
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel
import widgets.widgetsModule.widgets.celsius.ui.CelsiusDimens


@Composable
fun CelsiusLargeScope.DailyForecast(
    celsiusViewModel: CelsiusViewModel,
    dimens: CelsiusDimens,
    degreeSymbol: String,
) {


    val forecast by celsiusViewModel.forecastDays.collectAsState()
    val temperature by celsiusViewModel.temperature.collectAsState()
    val density = celsiusViewModel.density.value
    temperature?.let { currentTemperature ->

        val globalMin = remember(forecast) { forecast.minOfOrNull { it.minTemp } ?: 0.0 }
        val globalMax = remember(forecast) { forecast.maxOfOrNull { it.maxTemp } ?: 0.0 }

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(Paddings.small),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(forecast) { forecastItem ->
                DayForecastCard(
                    forecastItem,
                    dimens.forecastCardTextSizeLarge,
                    dimens.conditionIconSizeLarge,
                    degreeSymbol,
                    currentTemperature,
                    globalMin,
                    globalMax,
                    dimens.forecastCardLargeHeight,
                    dimens.forecastLineIndicatorHeight,
                    density
                )
            }

        }
    }
}


