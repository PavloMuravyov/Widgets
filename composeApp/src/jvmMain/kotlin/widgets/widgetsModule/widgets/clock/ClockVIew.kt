package widgets.widgetsModule.widgets.clock

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import widgets.widgetsModule.data.models.WidgetSizes
import widgets.widgetsModule.widgets.clock.ClockMediumView.MediumClockView
import widgets.widgetsModule.widgets.clock.ClockSmallView.smallClockView
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.CapturedLayerState

@Composable
fun ClockView(clockViewModel: ClockViewModel, capturedLayerState: CapturedLayerState) {

    Box(Modifier.fillMaxSize()) {

        val widgetSize by clockViewModel.widgetSize.collectAsState()

        when (widgetSize) {
            WidgetSizes.Small -> smallClockView(clockViewModel, capturedLayerState)
            WidgetSizes.Medium -> MediumClockView(clockViewModel, capturedLayerState)
            WidgetSizes.Large -> {}
        }
    }

}
