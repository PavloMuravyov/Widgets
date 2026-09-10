package widgets.widgetsModule.widgetsManager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import widgets.Theme.Colors
import widgets.Theme.Paddings
import widgets.widgetsModule.data.models.WidgetSizes
import widgets.widgetsModule.data.models.WidgetsTypes
import widgets.widgetsModule.data.models.roundedShape
import widgets.widgetsModule.widgets.WidgetBaseViewModel
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.CapturedLayerState
import widgets.widgetsModule.widgetsManager.Utils.BlurSurface.blurFromLayer


@Composable
fun BlurSurface(
    modifier: Modifier,
    capturedLayerState: CapturedLayerState,
    widgetViewModel: WidgetBaseViewModel,
    content: @Composable () -> Unit
) {
    val cornerShape = widgetViewModel.widgetSize.value.roundedShape.minus(Paddings.small)


    val contentOverlayState by widgetViewModel.contentOverlayState.collectAsState()

    val blurOverlayMode by capturedLayerState.blurOverlayModeState.collectAsState()

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(
            Modifier.fillMaxSize()
                .clip(RoundedCornerShape(cornerShape))
                .blurFromLayer(capturedLayerState, contentOverlayState, blurOverlayMode)
        )

        content()
    }
}


