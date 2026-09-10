package widgets.widgetsModule.widgetsManager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import widgets.widgetsModule.data.models.BackgroundTypes
import widgets.widgetsModule.data.models.BaseWidgetScaledDimensions
import widgets.widgetsModule.managers.SystemDataManager.model.MonitorArea
import widgets.widgetsModule.widgets.WidgetBaseViewModel
import widgets.widgetsModule.widgets.widgetsWindow.Backgrounds.widgetLiquidEffect
import widgets.widgetsModule.widgets.widgetsWindow.Backgrounds.widgetLiquidSource
import widgets.widgetsModule.widgetsManager.Utils.DragGusturesHandler.dragGesturesHandler
import widgets.widgetsModule.widgetsManager.Utils.Modifiers.animatedOffset
import widgets.widgetsModule.widgetsManager.Utils.Modifiers.zIndexAsState
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.CapturedLayerState
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.rememberAnimatedWidgetOffset
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.rememberCapturedLayerState
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.rememberWidgetRoundedShape
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.rememberWidgetSize
import widgets.widgetsModule.widgetsManager.Utils.BlurSurface.captureToLayer
import widgets.widgetsModule.widgetsManager.Utils.BlurSurface.recalculateOverlayOnTrigger
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.liquid
import org.jetbrains.compose.resources.stringResource
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.widgets_settings


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun widgetContainer(
    widgetsManager: WidgetsManager,
    viewModel: WidgetBaseViewModel,
    wallpaperLiquidState: LiquidState,
    baseWidgetScaledDimensions: BaseWidgetScaledDimensions,
    backgroundType: BackgroundTypes,
    density: Density,
    availableMonitorArea: MonitorArea,
    content: @Composable (CapturedLayerState) -> Unit
) {
    val scope = rememberCoroutineScope()

    val capturedLayerState = rememberCapturedLayerState(widgetsManager.blurOverlayMode)

    val animatedOffset = rememberAnimatedWidgetOffset(viewModel.position) {
        viewModel.updateDragAnimationEndState(it)
    }

    val widgetSize = rememberWidgetSize(viewModel, baseWidgetScaledDimensions)
    val roundedShape = rememberWidgetRoundedShape(viewModel)
    val zIndex = viewModel.widgetContainerZIndex.collectAsState()

    var contextMenuVisible by remember { mutableStateOf(false) }
    var contextMenuOffset by remember { mutableStateOf(IntOffset.Zero) }

    Box(
        Modifier
            .animatedOffset(animatedOffset)
            .zIndexAsState { zIndex.value }
            .size(widgetSize)
            .clip(RoundedCornerShape(roundedShape.value))
            .dragGesturesHandler(
                viewModel,
                widgetsManager,
                availableMonitorArea,
                animatedOffset,
                widgetSize,
                baseWidgetScaledDimensions,
                density,
                scope
            )
            .thirdMouseClickHandler(
                onRightClick = { position ->
                    contextMenuOffset = IntOffset(position.x.toInt(), position.y.toInt())
                    contextMenuVisible = true
                }
            )
            .widgetLiquidSource(
                viewModel,
                widgetsManager,
                wallpaperLiquidState,
            )
            .recalculateOverlayOnTrigger(viewModel, capturedLayerState)
    ) {

        Box(
            Modifier
                .fillMaxSize()
                .captureToLayer(capturedLayerState)
        ) {
            Box(
                Modifier.fillMaxSize()
                    .widgetLiquidEffect(
                        wallpaperLiquidState,
                        backgroundType,
                        roundedShape,
                    )
            ) {
            }
        }
        Box(Modifier.fillMaxSize()) {
            content(capturedLayerState)
        }

        if (contextMenuVisible) {
            ContextSettingsMenu(contextMenuOffset, roundedShape, widgetsManager, wallpaperLiquidState) { contextMenuVisibleNewState ->
                contextMenuVisible = contextMenuVisibleNewState
            }
        }
    }
}




@Composable
fun ContextSettingsMenu(
    contextMenuOffset: IntOffset,
    roundedShape: Dp,
    widgetsManager: WidgetsManager,
    wallpaperLiquidState: LiquidState,
    onContextVisibleChange: (Boolean) -> Unit
) {

    Popup(
        offset = contextMenuOffset,
        onDismissRequest = {onContextVisibleChange(false)},
        properties = PopupProperties(focusable = true),
    ) {

        Surface(
            shape = RoundedCornerShape(roundedShape),
            color = Color.Transparent,
            shadowElevation = 2.dp,
            modifier = Modifier.wrapContentWidth()
        ) {

            Box(Modifier.wrapContentSize()
                .liquid(wallpaperLiquidState){
                    frost = 30.dp
                    shape = RoundedCornerShape(roundedShape)
                    edge = 0.1f
                }) {

            val settingLabel = stringResource(Res.string.widgets_settings)
            Text(
                text = settingLabel,
                color = Color.White,
                modifier = Modifier
                    .clickable {
                        onContextVisibleChange(false)
                        widgetsManager.updateShowConfigWindow(true)
                    }
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            )
            }
        }
    }
}

fun Modifier.thirdMouseClickHandler(onRightClick: (Offset) -> Unit) =
    this.pointerInput(Unit) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent(PointerEventPass.Main)
                if (event.type == PointerEventType.Press && event.buttons.isSecondaryPressed) {
                    val position = event.changes.first().position
                    onRightClick(position)
                    event.changes.forEach { it.consume() }
                }
            }
        }
    }