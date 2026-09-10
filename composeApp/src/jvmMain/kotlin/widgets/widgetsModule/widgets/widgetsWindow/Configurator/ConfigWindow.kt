package widgets.widgetsModule.widgets.widgetsWindow.Configurator

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.zIndex
import widgets.Theme.Colors
import widgets.Theme.Paddings
import widgets.widgetsModule.data.models.BaseWidgetScaledDimensions
import widgets.widgetsModule.data.models.BlurOverlayModes
import widgets.widgetsModule.data.models.WidgetSizes
import widgets.widgetsModule.data.models.localizedLabels
import widgets.widgetsModule.data.models.roundedShape
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.ConfigContent
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.DropDownMenu
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.commonSettings.AutostartSettings
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.commonSettings.BackgroundSettings
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.commonSettings.BlurOverlaySettings
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.commonSettings.CommonSettings
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.extentions.BlurredBackground
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.extentions.visibilityAlpha
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.widgetCard.ConfigWidgetCard
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.rememberCapturedLayerState
import widgets.widgetsModule.widgetsManager.WidgetsManager
import io.github.fletchmckee.liquid.LiquidState
import org.jetbrains.compose.resources.stringResource
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.widget_extra_dimming_title


@Composable
fun ConfigWindow(
    widgetsManager: WidgetsManager,
    baseWidgetScaledDimensions: BaseWidgetScaledDimensions,
    liquidState: LiquidState,
    density: Density,
    containerSize: DpSize,
) {
    val showConfigWindow by widgetsManager.showConfigWindow.collectAsState()
    val isHidden by widgetsManager.isAnyWidgetDragging.collectAsState()

    val configWindowSize = remember(baseWidgetScaledDimensions) {
        DpSize(
            (baseWidgetScaledDimensions.width * 2) + baseWidgetScaledDimensions.spacing,
            (baseWidgetScaledDimensions.height * 4) + baseWidgetScaledDimensions.spacing * 3
        )
    }

    var offset by remember {
        mutableStateOf(
            with(density) {
                Offset(
                    x = (containerSize.width / 2 - configWindowSize.width / 2).toPx(),
                    y = (containerSize.height / 2 - configWindowSize.height / 2).toPx()
                )
            }
        )
    }

    if (showConfigWindow) {


        val maxOffset = remember {
            with(density) {
                Offset(
                    x = (containerSize.width.toPx() - configWindowSize.width.toPx()).coerceAtLeast(0f),
                    y = (containerSize.height.toPx() - configWindowSize.height.toPx()).coerceAtLeast(0f)
                )
            }
        }

        Box(
            Modifier
                .size(configWindowSize)
                .graphicsLayer {
                    translationX = offset.x
                    translationY = offset.y
                }
                .visibilityAlpha(!isHidden)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()
                            offset = Offset(
                                x = (offset.x + dragAmount.x).coerceIn(0f, maxOffset.x),
                                y = (offset.y + dragAmount.y).coerceIn(0f, maxOffset.y)
                            )
                        }
                    )
                }
                .zIndex(1f)
                .clip(RoundedCornerShape(WidgetSizes.Large.roundedShape))
                .padding(Paddings.xtraSmall),
            contentAlignment = Alignment.Center
        ) {

            ConfigWindowContent(
                liquidState,
                widgetsManager,
                baseWidgetScaledDimensions,
            )
        }
    }
}



@Composable
private fun ConfigWindowContent(
    liquidState: LiquidState,
    widgetsManager: WidgetsManager,
    baseWidgetScaledDimensions: BaseWidgetScaledDimensions,

    ) {

    val onClose = remember(widgetsManager) {
        { widgetsManager.updateShowConfigWindow(false) }
    }

    val configWidgetCardHeight = remember { baseWidgetScaledDimensions.height * 2f }

    val dimens = remember(configWidgetCardHeight) {
        ConfigDimens.calculate(configWidgetCardHeight)
    }

    BlurredBackground(liquidState,rememberCapturedLayerState(widgetsManager.blurOverlayMode))

    Column(Modifier.fillMaxSize()
        .padding(horizontal = Paddings.medium),) {

        ControlPanel(Modifier.weight(0.075f), liquidState, widgetsManager, dimens, onClose)

        ConfigContent(Modifier.weight(1f), widgetsManager, configWidgetCardHeight, dimens)
    }
}








