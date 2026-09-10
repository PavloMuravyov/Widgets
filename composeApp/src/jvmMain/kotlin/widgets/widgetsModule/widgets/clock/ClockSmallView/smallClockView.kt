package widgets.widgetsModule.widgets.clock.ClockSmallView

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import widgets.Theme.Paddings
import widgets.widgetsModule.widgets.clock.ClockViewModel
import widgets.widgetsModule.widgets.clock.Composables.mechanicalClock.MechanicalClock
import widgets.widgetsModule.widgetsManager.BlurSurface
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.CapturedLayerState

@Composable
fun smallClockView(viewModel: ClockViewModel, capturedLayerState: CapturedLayerState) {

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
            .padding(Paddings.medium),
        contentAlignment = Alignment.Center
    ) {


        BlurSurface(Modifier.fillMaxSize(), capturedLayerState, viewModel) {
            MechanicalClock(
                viewModel,
                Modifier.fillMaxSize()
            )
        }
    }
}