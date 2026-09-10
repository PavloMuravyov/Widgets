package widgets.widgetsModule.widgetsManager.Utils.DragGusturesHandler

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import widgets.widgetsModule.data.models.BaseWidgetScaledDimensions
import widgets.widgetsModule.data.models.Directions
import widgets.widgetsModule.data.models.MutableMovementDirection
import widgets.widgetsModule.managers.SystemDataManager.model.MonitorArea
import widgets.widgetsModule.widgets.WidgetBaseViewModel
import widgets.widgetsModule.widgetsManager.WidgetsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.dragGesturesHandler(
    viewModel: WidgetBaseViewModel,
    widgetsManager: WidgetsManager,
    availableArea: MonitorArea,
    animatedOffset: Animatable<DpOffset, AnimationVector2D>,
    widgetSize: DpSize,
    baseWidgetScaledDimensions: BaseWidgetScaledDimensions,
    density: Density,
    scope: CoroutineScope,

    ) =
    this
        .pointerInput(Unit) {

            val movementDirection = MutableMovementDirection(
                horizontal = mutableStateOf(Directions.STILL),
                vertical = mutableStateOf(Directions.STILL)
            )
            val gestureScope = DragGestureScope()

            fun selectWidgetIfConfigOpen() {
                viewModel.widgetSettings.value?.widgetType?.let { widgetType ->
                    if (widgetsManager.showConfigWindow.value) {
                        widgetsManager.updateSelectedWidgetType(widgetType)
                    }
                }
            }
            coroutineScope {
                launch {
                    detectTapGestures(
                        onTap = { selectWidgetIfConfigOpen() }
                    )
                }
                launch {
                    detectDragGestures(
                        onDragStart = {
                            selectWidgetIfConfigOpen()
                            gestureScope.handleDragStart(viewModel)
                        },
                        onDragEnd = {
                            gestureScope.handleDragEnd(
                                viewModel,
                                widgetsManager,
                                animatedOffset,
                                baseWidgetScaledDimensions,
                                availableArea,
                                movementDirection,
                            )
                            viewModel.updateDraggingState(false)
                        },
                        onDrag = { change, dragAmount ->
                            gestureScope.handleDrag(
                                availableArea,
                                density,
                                change,
                                dragAmount,
                                animatedOffset,
                                widgetSize,
                                movementDirection,
                                scope
                            )
                        }
                    )
                }
            }
        }
        /*.pointerInput(Unit) {

            val movementDirection = MutableMovementDirection(
                horizontal = mutableStateOf(Directions.STILL),
                vertical = mutableStateOf(Directions.STILL)
            )

            val gestureScope = DragGestureScope()

            detectDragGestures(
                onDragStart = {
                    gestureScope.handleDragStart(
                        viewModel,
                    )
                },
                onDragEnd = {


                    gestureScope.handleDragEnd(
                        viewModel,
                        widgetsManager,
                        animatedOffset,
                        baseWidgetScaledDimensions,
                        availableArea,
                        movementDirection,
                    )

                    viewModel.updateDraggingState(false)


                },
                onDrag = { change, dragAmount ->
                    gestureScope.handleDrag(
                        availableArea,
                        density,
                        change,
                        dragAmount,
                        animatedOffset,
                        widgetSize,
                        movementDirection,
                        scope
                    )

                }

            )
        }*/





