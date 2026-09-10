package widgets.widgetsModule.widgetsManager.Utils.DragGusturesHandler

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import widgets.widgetsModule.data.models.BaseWidgetScaledDimensions
import widgets.widgetsModule.data.models.Directions
import widgets.widgetsModule.data.models.MutableMovementDirection
import widgets.widgetsModule.data.models.WidgetMovingDirection
import widgets.widgetsModule.data.models.WidgetPosition
import widgets.widgetsModule.data.models.WindowConstraints
import widgets.widgetsModule.managers.SystemDataManager.model.MonitorArea
import widgets.widgetsModule.widgets.WidgetBaseViewModel
import widgets.widgetsModule.widgetsManager.WidgetsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


internal fun DragGestureScope.handleDragStart(
    viewModel: WidgetBaseViewModel,
) {
    viewModel.updateDraggingState(true)
}


internal fun DragGestureScope.handleDrag(
    availableArea: MonitorArea?,
    density: Density,
    change: PointerInputChange,
    dragAmount: Offset,
    animatedOffset: Animatable<DpOffset, AnimationVector2D>,
    widgetSize: DpSize,
    movementDirection: MutableMovementDirection,
    scope: CoroutineScope,
){
    change.consume()

    when {
        dragAmount.x > 2f -> {
            if (movementDirection.horizontal.value != Directions.POSITIVE) {
                movementDirection.horizontal.value = Directions.POSITIVE
            }
        }
        dragAmount.x < -2f -> {
            if (movementDirection.horizontal.value != Directions.NEGATIVE) {
                movementDirection.horizontal.value = Directions.NEGATIVE
            }
        }
    }

    when {
        dragAmount.y > 2f -> {
            if (movementDirection.vertical.value != Directions.POSITIVE) {
                movementDirection.vertical.value = Directions.POSITIVE
            }
        }
        dragAmount.y < -2f -> {
            if (movementDirection.vertical.value != Directions.NEGATIVE) {
                movementDirection.vertical.value = Directions.NEGATIVE
            }
        }
    }

    availableArea?.areaSize?.let { areaSize ->
        val dragDp = with(density) {
            DpOffset(dragAmount.x.toDp(), dragAmount.y.toDp())
        }

        val current = animatedOffset.value
        val newOffset = current + dragDp

        val boundedX = newOffset.x.coerceIn(
            0.dp,
            areaSize.width - widgetSize.width,
        )

        val boundedY = newOffset.y.coerceIn(
            0.dp,
            areaSize.height - widgetSize.height,
        )

        scope.launch {
            animatedOffset.snapTo(DpOffset(boundedX, boundedY))
        }
    }

}

internal fun DragGestureScope.handleDragEnd(
    widgetViewModel: WidgetBaseViewModel,
    widgetsManager: WidgetsManager,
    animatedOffset: Animatable<DpOffset, AnimationVector2D>,
    baseWidgetScaledDimensions: BaseWidgetScaledDimensions,
    constraints: MonitorArea,
    movementDirection: MutableMovementDirection,
) {
    val widgetSize = widgetViewModel.widgetSize.value
    val widgetPosition = widgetViewModel.widgetSettings.value?.widgetPosition


    val occupiedCells = widgetsManager.occupiedCells.value.toMutableList()

    occupiedCells.remove(Pair(widgetSize, widgetPosition))

    widgetViewModel.updatePosition(
        newPosition = WidgetPosition(
            xPosition = animatedOffset.value.x.value.toInt(),
            yPosition = animatedOffset.value.y.value.toInt(),
            containerConstraints = WindowConstraints(
                width = constraints.areaSize.width.value.toInt(),
                height = constraints.areaSize.height.value.toInt(),
            )
        ),
        baseCellDimensions = baseWidgetScaledDimensions,
        constraints = Constraints(
            constraints.areaSize.width.value.toInt(),
            constraints.areaSize.width.value.toInt(),
            constraints.areaSize.height.value.toInt(),
            constraints.areaSize.height.value.toInt(),
        ),
        occupiedCells.toList(),
        WidgetMovingDirection(
            movementDirection.horizontal.value,
            movementDirection.vertical.value
        )
    )
}

