package widgets.widgetsModule.widgets.celsius.ui.CelsiusLargeView

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import widgets.Theme.Paddings
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel
import widgets.widgetsModule.widgets.celsius.ui.CelsiusDimens
import widgets.widgetsModule.widgetsManager.BlurSurface
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.CapturedLayerState

class CelsiusLargeScope private constructor() {
    companion object {
        internal val instance =
            CelsiusLargeScope()
    }
}

@Composable
fun CelsiusLargeScope(content: @Composable CelsiusLargeScope.() -> Unit) {
    CelsiusLargeScope.instance.content()
}


@Composable
fun CelsiusLargeView(
    celsiusViewModel: CelsiusViewModel,
    capturedLayerState: CapturedLayerState
) {

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {

        val dimens = remember(maxWidth, maxHeight) {
            CelsiusDimens.calculate(maxWidth, maxHeight, celsiusViewModel.widgetSize.value)
        }

        CelsiusLargeScope {

            Column(
                Modifier.fillMaxSize()
                    .padding(Paddings.medium),
            ) {

                BlurSurface(Modifier.weight(1f), capturedLayerState, celsiusViewModel) {
                    CurrentWeatherLocationContent(celsiusViewModel, dimens, capturedLayerState)
                }

                Spacer(Modifier.height(Paddings.medium))

                BlurSurface(Modifier.weight(1f), capturedLayerState, celsiusViewModel) {
                    ForecastContent(celsiusViewModel, Modifier.fillMaxSize(), dimens, capturedLayerState)
                }

            }
        }

    }

}


@Composable
fun CelsiusLargeScope.CurrentWeatherLocationContent(
    celsiusViewModel: CelsiusViewModel,
    dimens: CelsiusDimens,
    capturedLayerState: CapturedLayerState
) {

    Column(Modifier.fillMaxSize()) {

        CurrentWeatherLocation(celsiusViewModel, Modifier.weight(3f), dimens, capturedLayerState)

        Spacer(Modifier.width(Paddings.medium))

        AstroInfo(celsiusViewModel, Modifier.weight(1f), dimens)
    }

}






