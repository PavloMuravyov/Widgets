package widgets.widgetsModule.widgets.celsius

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import widgets.widgetsModule.data.models.WidgetSizes
import widgets.widgetsModule.widgets.celsius.ui.CelsiusLargeView.CelsiusLargeView
import widgets.widgetsModule.widgets.celsius.ui.CelsiusMediumView.CelsiusMediumView
import widgets.widgetsModule.widgets.celsius.ui.CelsiusSmallView.CelsiusSmallView
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.CapturedLayerState

@Composable
fun CelsiusView(celsiusViewModel: CelsiusViewModel, capturedLayerState: CapturedLayerState) {

    val widgetSize by celsiusViewModel.widgetSize.collectAsState()

    when (widgetSize) {

        WidgetSizes.Small -> {
            CelsiusSmallView(
                celsiusViewModel,
                capturedLayerState
            )
        }

        WidgetSizes.Medium -> {
            CelsiusMediumView(
                celsiusViewModel,
                capturedLayerState
            )
        }

        WidgetSizes.Large -> {
            CelsiusLargeView(
                celsiusViewModel,
                capturedLayerState
            )
        }
    }
}

