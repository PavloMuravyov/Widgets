package widgets.widgetsModule.widgets.celsius.ui.CelsiusMediumView

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import widgets.Theme.Paddings
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel
import widgets.widgetsModule.widgets.celsius.ui.CelsiusDimens
import widgets.widgetsModule.widgets.celsius.ui.Composables.TemperatureContent
import widgets.widgetsModule.widgetsManager.BlurSurface
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.CapturedLayerState
import java.time.format.DateTimeFormatter

class CelsiusMediumScope private constructor() {
    companion object {
        internal val instance =
            CelsiusMediumScope()
    }
}

@Composable
fun CelsiusMediumScope(content: @Composable CelsiusMediumScope.() -> Unit) {
    CelsiusMediumScope.instance.content()
}


@Composable
fun CelsiusMediumView(
    celsiusViewModel: CelsiusViewModel,
    capturedLayerState: CapturedLayerState
) {

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {

        val dimens = remember(maxWidth, maxHeight) {
            CelsiusDimens.calculate(maxWidth, maxHeight, celsiusViewModel.widgetSize.value)
        }

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

        CelsiusMediumScope {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Paddings.medium)
            ) {

                BlurSurface(modifier = Modifier.weight(1f), capturedLayerState, celsiusViewModel) {
                    CurrentWeatherData(celsiusViewModel, dimens)
                }

                Spacer(Modifier.height(Paddings.medium))


                BlurSurface(modifier = Modifier.weight(1f), capturedLayerState, celsiusViewModel) {

                    HoursForecast(celsiusViewModel, dimens, formatter)

                }
            }
        }
    }
}


@Composable
private fun CelsiusMediumScope.CurrentWeatherData(celsiusViewModel: CelsiusViewModel, dimens: CelsiusDimens) {

    Row(
        Modifier.fillMaxSize()
            .padding(horizontal = Paddings.medium, vertical = Paddings.small)
    ) {

        CurrentWeatherLocationInfoContent(celsiusViewModel, dimens, Modifier.weight(2.1f))

        WindContent(celsiusViewModel, dimens, Modifier.weight(1f))

        CurrentWeatherIcon(celsiusViewModel, dimens, Modifier.weight(1f))

        TemperatureContent(dimens.temperatureLabelTextSizeMedium, celsiusViewModel, Modifier.weight(1f))
    }
}







