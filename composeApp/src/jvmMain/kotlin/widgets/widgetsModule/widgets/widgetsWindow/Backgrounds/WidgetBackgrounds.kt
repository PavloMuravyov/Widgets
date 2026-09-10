package widgets.widgetsModule.widgets.widgetsWindow.Backgrounds


import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import widgets.Theme.Colors
import widgets.widgetsModule.data.models.BackgroundTypes
import widgets.widgetsModule.widgets.WidgetBaseViewModel
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.extentions.animatedGradientBorder
import widgets.widgetsModule.widgetsManager.WidgetsManager
import io.github.fletchmckee.liquid.LiquidScope
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.liquefiable
import io.github.fletchmckee.liquid.liquid


fun Modifier.widgetLiquidSource(
    viewModel: WidgetBaseViewModel,
    widgetsManager: WidgetsManager,
    wallpaperLiquidState: LiquidState,
): Modifier = composed {


    val anyWidgetDragging by widgetsManager.isAnyWidgetDragging.collectAsState()
    val thisWidgetDragging by viewModel.draggingState.collectAsState()

    if (anyWidgetDragging && !thisWidgetDragging) {
        this.liquefiable(wallpaperLiquidState)
    }
    else {
        this
    }
}

fun Modifier.widgetLiquidEffect(
    wallpaperLiquidState: LiquidState,
    backgroundType: BackgroundTypes,
    cornerShape: Dp,
): Modifier = composed {
    val liquidParams: (LiquidScope.() -> Unit)? = remember(backgroundType, cornerShape) {
        when (backgroundType) {
            BackgroundTypes.Blur -> buildBlurParams(cornerShape)
            BackgroundTypes.Liquid -> buildLiquidParams(cornerShape)
            else -> null
        }
    }

    if (liquidParams == null) return@composed this
    this.liquid(wallpaperLiquidState, liquidParams)
}


private fun buildBlurParams(cornerShape: Dp): LiquidScope.() -> Unit = {
    frost = LiquidEffects.Blur.FROST.dp
    shape = RoundedCornerShape(cornerShape)
    refraction = LiquidEffects.Blur.REFRACTION
    curve = LiquidEffects.Blur.CURVE
    edge = LiquidEffects.Blur.EDGE
    tint = Color.Transparent
    saturation = LiquidEffects.Blur.SATURATION
    dispersion = LiquidEffects.Blur.DISPERSION
}


 fun buildContainerBlurParams(cornerShape: Dp): LiquidScope.() -> Unit = {
     frost = 15.dp
     shape = RoundedCornerShape(cornerShape)
     refraction = LiquidEffects.Simple.REFRACTION
     curve = LiquidEffects.Simple.CURVE
     edge = LiquidEffects.Simple.EDGE
     tint = Color.Transparent
     saturation = LiquidEffects.Simple.SATURATION
     dispersion = LiquidEffects.Simple.DISPERSION
}


fun buildContainerStrongBlurParams(cornerShape: Dp): LiquidScope.() -> Unit = {
    frost = 15.dp
    shape = RoundedCornerShape(cornerShape)
    refraction = LiquidEffects.Simple.REFRACTION
    curve = LiquidEffects.Simple.CURVE
    edge = 0.0025f
    tint = Color.Black.copy(0.5f)
    saturation = LiquidEffects.Simple.SATURATION
    dispersion = LiquidEffects.Simple.DISPERSION
}

 fun buildLiquidParams(cornerShape: Dp): LiquidScope.() -> Unit = {


    frost = LiquidEffects.Simple.FROST.dp
    shape = RoundedCornerShape(cornerShape)
    refraction = LiquidEffects.Simple.REFRACTION
    curve = LiquidEffects.Simple.CURVE
    edge = LiquidEffects.Simple.EDGE
    tint = Color.Transparent
    saturation = LiquidEffects.Simple.SATURATION
    dispersion = LiquidEffects.Simple.DISPERSION
}


object LiquidEffects {
    object Blur {
        const val FROST = 20f
        const val REFRACTION =  0.0f
        const val CURVE = 0.0f
        const val EDGE = 0.02f
        const val SATURATION = 1f
        const val DISPERSION = 0.0f

    }

    object Simple {
        const val FROST = 6f

        const val REFRACTION = 0.1f
        const val CURVE = 0.6f
        const val EDGE = 0.02f
        const val SATURATION = 1.0f
        const val DISPERSION = 0.35f

    }
}


