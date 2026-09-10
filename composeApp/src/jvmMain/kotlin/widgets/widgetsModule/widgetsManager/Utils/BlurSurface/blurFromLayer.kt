package widgets.widgetsModule.widgetsManager.Utils.BlurSurface

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import widgets.Theme.Colors
import widgets.widgetsModule.data.models.BlurOverlayModes
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.CapturedLayerState


fun Modifier.blurFromLayer(state: CapturedLayerState,
                           needBlurEffect: Boolean,
                           blurOverlayMode: BlurOverlayModes = BlurOverlayModes.Always,
                           needDarkOverlay : Boolean = true,
                           radius: Float = 20f): Modifier = composed {

    var selfBoundsInWindow by remember { mutableStateOf(Rect.Zero) }

    if (blurOverlayMode != BlurOverlayModes.Never) {

        onGloballyPositioned { coordinates ->
            selfBoundsInWindow = coordinates.boundsInWindow()
        }

        .graphicsLayer {
            renderEffect = BlurEffect(radiusX = radius, radiusY =radius)
        }
        .drawWithContent {
            if (needBlurEffect || blurOverlayMode == BlurOverlayModes.Always) {
                val sourceLayer = state.layer

                if (sourceLayer != null && !sourceLayer.isReleased) {
                    val offsetX = selfBoundsInWindow.left - state.boundsInWindow.left
                    val offsetY = selfBoundsInWindow.top - state.boundsInWindow.top
                    translate(left = -offsetX, top = -offsetY) {
                        drawLayer(sourceLayer)

                    }
                    if (blurOverlayMode == BlurOverlayModes.Always || (needDarkOverlay && blurOverlayMode == BlurOverlayModes.Auto)) {
                        drawRect(color = Colors.transparentBlackOverlays)
                    }
                }
            }
        }
    } else this
}
