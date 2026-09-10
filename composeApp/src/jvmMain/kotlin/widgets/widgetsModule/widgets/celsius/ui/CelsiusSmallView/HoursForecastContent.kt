package widgets.widgetsModule.widgets.celsius.ui.CelsiusSmallView

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import widgets.Theme.Paddings
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel
import widgets.widgetsModule.widgets.celsius.ui.Composables.ConditionIcon
import widgets.widgetsModule.widgets.celsius.domain.model.HourlyForecast
import org.jetbrains.compose.resources.stringResource
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.degreeSymbol


@Composable
fun widgets.widgetsModule.widgets.celsius.ui.CelsiusSmallView.CelsiusSmallScope.HoursForecastContent(
    modifier: Modifier,
    celsiusViewModel: CelsiusViewModel, dimens: widgets.widgetsModule.widgets.celsius.ui.CelsiusDimens
){
    val forecastHours by celsiusViewModel.hourlyForecast.collectAsState()
    LazyColumn(
        modifier.fillMaxSize()
            .padding(Paddings.small)

        ,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start,

        ) {

        items(forecastHours){ hourForecast ->
                forecastSmallCard(hourForecast, dimens)
        }

    }

}

@Composable
private fun widgets.widgetsModule.widgets.celsius.ui.CelsiusSmallView.CelsiusSmallScope.forecastSmallCard(hourForecast: HourlyForecast, dimens: widgets.widgetsModule.widgets.celsius.ui.CelsiusDimens) {
    Row(
        Modifier.fillMaxWidth().height(dimens.forecastCardHeight),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ConditionIcon(
            conditionCode = hourForecast.conditionCode,
            iconSize = dimens.forecastIconSize
        )
        ForecastTemperature(
            temperature = hourForecast.temperature,
            textSize = dimens.baseInfoTextSizeSmall
        )
    }
}




@Composable
private fun widgets.widgetsModule.widgets.celsius.ui.CelsiusSmallView.CelsiusSmallScope.ForecastTemperature(temperature: Double, textSize: TextUnit) {
    val degreeSymbol = stringResource(Res.string.degreeSymbol)
    _root_ide_package_.widgets.widgetsModule.widgets.celsius.ui.TextLabel(
        textSize,
        temperature.toInt().toString() + degreeSymbol
    )
}
