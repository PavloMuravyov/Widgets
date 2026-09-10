package widgets.widgetsModule.managers.GridPositioningManager

import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import widgets.widgetsModule.data.models.BaseWidgetScaledDimensions
import widgets.widgetsModule.data.models.Directions
import widgets.widgetsModule.data.models.Widget
import widgets.widgetsModule.data.models.WidgetMovingDirection
import widgets.widgetsModule.data.models.WidgetPosition
import widgets.widgetsModule.data.models.WidgetSizes
import widgets.widgetsModule.data.models.WindowConstraints
import widgets.widgetsModule.data.models.scaledSize
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.roundToInt

class GridPositioningManager {

    private val _gridDelimiters = MutableStateFlow<GridDelimiters>(GridDelimiters())
    val gridDelimiters: StateFlow<GridDelimiters> = _gridDelimiters

    fun onClose(){
        _gridDelimiters.value = GridDelimiters()
    }


    fun generateGrid(
        baseCellSize: BaseWidgetScaledDimensions,
        parentContainerConstraints: Constraints,
    ){
        val windowWidth = parentContainerConstraints.maxWidth
        val windowHeight = parentContainerConstraints.maxHeight
        val cellSpacing = baseCellSize.spacing
        val stepX = (baseCellSize.width.value + cellSpacing.value).roundToInt()
        val stepY = (baseCellSize.height.value + cellSpacing.value).roundToInt()
        require(stepX > 0 && stepY > 0)
        val maxXPos = windowWidth - (baseCellSize.width.value)
        val maxYPos = windowHeight - (baseCellSize.height.value)
        val xDelimiters = (0..maxXPos.toInt() step stepX).toMutableList()
        val yDelimiters = (0..maxYPos.toInt() step stepY).toMutableList()

        _gridDelimiters.value = GridDelimiters(
            xDelimiters = xDelimiters,
            yDelimiters = yDelimiters
        )

    }

    fun getWidgetCell(
        widget: Widget,
        baseCellSize: BaseWidgetScaledDimensions,
        parentContainerConstraints: Constraints,
        newPosition: WidgetPosition? = null,
        occupiedCells: List<Pair<WidgetSizes, WidgetPosition?>> = emptyList(),
        widgetMovingDirection: WidgetMovingDirection = WidgetMovingDirection(),
        expectedSize: WidgetSizes? = null,
    ): WidgetPosition {

        val widgetPosition = newPosition ?: widget.widgetPosition
        val widgetSize = expectedSize ?: widget.widgetSize ?: WidgetSizes.Small

        val calculatingPoint = getCalculatingPoint(
            widgetMovingDirection,
            widgetPosition?.xPosition ?: 0,
            yPosition = widgetPosition?.yPosition ?: 0,
            widgetSize,
            baseCellSize
        )

        val xDelimiters = gridDelimiters.value.xDelimiters.dropLast(
            if(widgetSize == WidgetSizes.Small) {
                0
            } else {
                1
            }
        )

        val yDelimiters = gridDelimiters.value.yDelimiters.dropLast(
            if(widgetSize == WidgetSizes.Large) {
                1
            } else {
                0
            }
        )

        val cellX = xDelimiters
            .zipWithNext()
            .firstOrNull { (start, end ) -> calculatingPoint.first.value.toInt() in start until end }
            ?.first ?: xDelimiters.lastOrNull() ?: 0

        val cellY = yDelimiters
            .zipWithNext()
            .firstOrNull { (start, end) -> calculatingPoint.second.value.toInt() in start until end }
            ?.first ?:  yDelimiters.lastOrNull() ?: 0


        val targetCell = WidgetPosition(
            cellX,
            cellY,
            gridDelimiters.value.xDelimiters.indexOf(cellX),
            gridDelimiters.value.yDelimiters.indexOf(cellY),
            containerConstraints = WindowConstraints(
                parentContainerConstraints.maxWidth,
                parentContainerConstraints.maxHeight,
            )
        )


        return  if(CellValidator().isCellOccupied(targetCell, occupiedCells, widgetSize,baseCellSize)){
            CellValidator().findNearestCell(
                occupiedCells,
                targetCell,
                baseCellSize,
                gridDelimiters.value.xDelimiters,
                gridDelimiters.value.yDelimiters,
                widgetSize,
                calculatingPoint
            )
        } else {
            targetCell
        }


    }

    fun getPositionByCell(position: WidgetPosition,
                          parentContainerConstraints: Constraints) : WidgetPosition {

        val rowPos = position.rowPosition ?: 0
        val colPos = position.columnPosition ?: 0

        val xValue =
            try {
                gridDelimiters.value.xDelimiters[rowPos]
            } catch (e: IndexOutOfBoundsException) {
                gridDelimiters.value.xDelimiters.last()
            }

        val yValue =
            try {
                gridDelimiters.value.yDelimiters[colPos]
            }
            catch (e: IndexOutOfBoundsException) {
                gridDelimiters.value.yDelimiters.last()
            }

        return WidgetPosition(
            xValue,
            yValue,
            position.rowPosition,
            position.columnPosition,
            containerConstraints = WindowConstraints(
                parentContainerConstraints.maxWidth,
                parentContainerConstraints.maxHeight,
            )
        )
    }

    private fun getCalculatingPoint(
        direction: WidgetMovingDirection,
        xPosition: Int,
        yPosition: Int,
        widgetSize: WidgetSizes,
        baseWidgetScaledDimensions: BaseWidgetScaledDimensions) : Pair<Dp, Dp> {

        val size = widgetSize.scaledSize(baseWidgetScaledDimensions)
        val x = (xPosition.dp + size.width / 2 ) + if (direction.right == Directions.POSITIVE) (size.width / 4 ) else  ( - (size.width / 4))
        val y = (yPosition.dp + size.height / 2 ) + if (direction.down == Directions.POSITIVE) (size.height / 4 ) else ( - (size.height / 4))

        return Pair(x, y)
    }
}