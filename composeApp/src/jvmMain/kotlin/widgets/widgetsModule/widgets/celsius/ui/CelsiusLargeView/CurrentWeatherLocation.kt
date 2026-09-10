package widgets.widgetsModule.widgets.celsius.ui.CelsiusLargeView

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import widgets.Theme.Paddings
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel
import widgets.widgetsModule.widgets.celsius.ui.CelsiusDimens
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.CapturedLayerState

@Composable
fun CelsiusLargeScope.CurrentWeatherLocation(
    celsiusViewModel: CelsiusViewModel,
    modifier: Modifier,
    dimens: CelsiusDimens,
    capturedLayerState: CapturedLayerState
) {

    Row(
        modifier.fillMaxSize()
            .padding(horizontal = Paddings.medium, vertical = Paddings.medium)
    ) {

        LocationWeatherContent(celsiusViewModel, Modifier.weight(1.1f), dimens, capturedLayerState)
        ConditionWindContent(celsiusViewModel, Modifier.weight(1f), dimens)

    }

}



