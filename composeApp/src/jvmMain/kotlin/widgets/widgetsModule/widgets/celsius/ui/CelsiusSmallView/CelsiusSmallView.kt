package widgets.widgetsModule.widgets.celsius.ui.CelsiusSmallView

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import widgets.Theme.Paddings
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel
import widgets.widgetsModule.widgets.celsius.ui.CelsiusDimens
import widgets.widgetsModule.widgets.celsius.ui.CelsiusSmallView.HoursForecastContent
import widgets.widgetsModule.widgets.celsius.ui.CelsiusSmallView.LocationAndCurrentDataContent
import widgets.widgetsModule.widgetsManager.BlurSurface
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.CapturedLayerState



class CelsiusSmallScope private constructor() {
    companion object {
        internal val instance =
            CelsiusSmallScope()
    }
}

@Composable
fun CelsiusSmallScope(content: @Composable CelsiusSmallScope.() -> Unit) {
    CelsiusSmallScope.instance.content()
}


@Composable
fun CelsiusSmallView(
    celsiusViewModel: CelsiusViewModel,
    capturedLayerState: CapturedLayerState
) {

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {

        val dimens = remember(maxWidth, maxHeight) {
            CelsiusDimens.calculate(maxWidth, maxHeight, celsiusViewModel.widgetSize.value)
        }


        CelsiusSmallScope {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(Paddings.medium)

            ) {

                BlurSurface(modifier = Modifier.weight(1f), capturedLayerState, celsiusViewModel) {
                    LocationAndCurrentDataContent(Modifier, celsiusViewModel, dimens)
                }

                Spacer(Modifier.width(Paddings.medium))

                BlurSurface(modifier = Modifier.weight(1f), capturedLayerState, celsiusViewModel) {
                    HoursForecastContent(Modifier, celsiusViewModel, dimens)
                }

            }
        }
    }

}


