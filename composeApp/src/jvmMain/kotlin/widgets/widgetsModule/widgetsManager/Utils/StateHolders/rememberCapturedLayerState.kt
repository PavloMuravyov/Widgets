package widgets.widgetsModule.widgetsManager.Utils.StateHolders

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.layer.GraphicsLayer
import widgets.widgetsModule.data.models.BlurOverlayModes
import kotlinx.coroutines.flow.StateFlow


@Composable
fun rememberCapturedLayerState(blurOverlayMode: StateFlow<BlurOverlayModes>) = remember { CapturedLayerState(blurOverlayMode) }


@Stable
class CapturedLayerState(blurOverlayMode: StateFlow<BlurOverlayModes>) {
    var layer: GraphicsLayer? by mutableStateOf(null)
    var boundsInWindow: Rect by mutableStateOf(Rect.Zero)

    var blurOverlayModeState: StateFlow<BlurOverlayModes> = blurOverlayMode

}
