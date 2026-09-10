package widgets.widgetsModule.widgets.celsius.ui.CelsiusMediumView

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import widgets.Theme.Paddings
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel
import org.jetbrains.compose.resources.stringResource
import widgets.composeapp.generated.resources.MaxTemp
import widgets.composeapp.generated.resources.MinTemp
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.degreeSymbol

@Composable
fun widgets.widgetsModule.widgets.celsius.ui.CelsiusMediumView.CelsiusMediumScope.CurrentWeatherLocationInfoContent(
    celsiusViewModel: CelsiusViewModel,
    dimens: widgets.widgetsModule.widgets.celsius.ui.CelsiusDimens,
    modifier: Modifier = Modifier
) {


    val maxTemp = stringResource(Res.string.MaxTemp)
    val minTemp = stringResource(Res.string.MinTemp)
    val degreeSymbol = stringResource(Res.string.degreeSymbol)


    Column(
        modifier.fillMaxSize()
            .padding(horizontal = Paddings.xtraSmall),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {

        LocationContent(Modifier.weight(1f), dimens, celsiusViewModel)

        TemperatureRange(celsiusViewModel, dimens, Modifier.weight(1f), maxTemp, minTemp, degreeSymbol)

        CurrentConditionTextContent(celsiusViewModel, dimens, Modifier.weight(1f))
    }
}




