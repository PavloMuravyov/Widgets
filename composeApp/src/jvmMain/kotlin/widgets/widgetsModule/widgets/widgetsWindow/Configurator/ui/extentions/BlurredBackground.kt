package widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.extentions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import widgets.Theme.Paddings
import widgets.widgetsModule.data.models.BlurOverlayModes
import widgets.widgetsModule.data.models.WidgetSizes
import widgets.widgetsModule.data.models.roundedShape
import widgets.widgetsModule.widgets.widgetsWindow.Backgrounds.buildContainerStrongBlurParams
import widgets.widgetsModule.widgetsManager.Utils.BlurSurface.blurFromLayer
import widgets.widgetsModule.widgetsManager.Utils.BlurSurface.captureToLayer
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.CapturedLayerState
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.rememberCapturedLayerState
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.liquid


@Composable
fun BlurredBackground(
    liquidState: LiquidState,
    capturedLayerState: CapturedLayerState ,
) {
    Box(
        Modifier.fillMaxSize()
            .captureToLayer(capturedLayerState)
    ) {
        Box(
            Modifier.fillMaxSize()
                .liquid(
                    liquidState,
                    buildContainerStrongBlurParams(WidgetSizes.Large.roundedShape - Paddings.xtraSmall)
                )
        )
    }

    Box(
        Modifier.fillMaxSize().blurFromLayer(
            capturedLayerState,
            needBlurEffect = true,
            needDarkOverlay = false,
            blurOverlayMode = BlurOverlayModes.Always,
            radius = 50f
        )
    )
}