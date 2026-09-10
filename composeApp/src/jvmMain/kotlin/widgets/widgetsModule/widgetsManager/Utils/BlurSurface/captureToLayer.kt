package widgets.widgetsModule.widgetsManager.Utils.BlurSurface

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalGraphicsContext
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.CapturedLayerState

fun Modifier.captureToLayer(state: CapturedLayerState): Modifier = composed {

    val graphicsContext = LocalGraphicsContext.current
    onGloballyPositioned { coordinates ->
        state.boundsInWindow = coordinates.boundsInWindow()
    }
        .drawWithContent {

                val layer = state.layer
                    ?.takeUnless { it.isReleased }
                    ?: graphicsContext.createGraphicsLayer()
                        .also { state.layer = it }

                layer.record { this@drawWithContent.drawContent() }
                drawLayer(layer)
            }

}
