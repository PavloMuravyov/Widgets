package widgets.widgetsModule.widgetsManager.Utils.BlurSurface

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import widgets.widgetsModule.data.models.BlurOverlayModes
import widgets.widgetsModule.widgets.WidgetBaseViewModel
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.CapturedLayerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun Modifier.recalculateOverlayOnTrigger(
    viewModel: WidgetBaseViewModel,
    capturedLayerState: CapturedLayerState
): Modifier = composed {


    val needRecalculate by viewModel.needRecalculateOverlay.collectAsState()
    val blurOverlayMode by capturedLayerState.blurOverlayModeState.collectAsState()


    LaunchedEffect(needRecalculate, blurOverlayMode) {
        if (needRecalculate > 0L && blurOverlayMode == BlurOverlayModes.Auto) {
            val bitmap =
                capturedLayerState.layer
                    ?.takeUnless { it.isReleased }
                    ?.toSkiaBitmapSampled()
                    ?: return@LaunchedEffect

            val needOverlay = withContext(Dispatchers.Default) {
                bitmap.needsDarkOverlay()
            }
            bitmap.close()
            viewModel.updateContentOverlayState(needOverlay)
        }
    }
    this
}

