package widgets.widgetsModule.managers.GridPositioningManager

import androidx.compose.ui.unit.Dp
import kotlin.math.roundToInt
import kotlin.math.sqrt
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import widgets.widgetsModule.data.models.BaseWidgetScaledDimensions
import widgets.widgetsModule.data.models.Widget
import widgets.widgetsModule.data.models.WidgetMovingDirection
import widgets.widgetsModule.data.models.WidgetPosition
import widgets.widgetsModule.data.models.WidgetSizes
import widgets.widgetsModule.data.models.WindowConstraints
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.roundToInt


class CellValidator {

    fun isCellOccupied(
        cell: WidgetPosition,
        occupiedCells: List<Pair<WidgetSizes,WidgetPosition?>>,
        widgetSize: WidgetSizes,
        baseCellDimensions: BaseWidgetScaledDimensions

    ): Boolean {
        val cellSpacing = baseCellDimensions.spacing

        val cellWidthInSteps = when (widgetSize) {
            WidgetSizes.Small -> 1
            WidgetSizes.Medium -> 2
            WidgetSizes.Large -> 2
        }

        val cellHeightInSteps = when (widgetSize) {
            WidgetSizes.Small -> 1
            WidgetSizes.Medium -> 1
            WidgetSizes.Large -> 2
        }

        return occupiedCells.any { occupied ->
            val occupiedWidthInSteps = when (occupied.first) {
                WidgetSizes.Small -> 1
                WidgetSizes.Medium -> 2
                WidgetSizes.Large -> 2
            }

            val occupiedHeightInSteps = when (occupied.first) {
                WidgetSizes.Small -> 1
                WidgetSizes.Medium -> 1
                WidgetSizes.Large -> 2
            }

            val stepX = (baseCellDimensions.width.value + cellSpacing.value).roundToInt()
            val stepY = (baseCellDimensions.height.value + cellSpacing.value).roundToInt()

            val cellRight = (cell.xPosition ?: 0 ) + (cellWidthInSteps * stepX)
            val cellBottom = (cell.yPosition ?: 0) + (cellHeightInSteps * stepY)
            val occupiedRight = (occupied.second?.xPosition ?: 0 )  + (occupiedWidthInSteps * stepX)
            val occupiedBottom = (occupied.second?.yPosition ?: 0 )  + (occupiedHeightInSteps * stepY)

            !((cellRight <= (occupied.second?.xPosition ?: 0) ||
                    (cell?.xPosition ?: 0) >= occupiedRight ||
                    cellBottom <= (occupied.second?.yPosition ?: 0) ||
                    (cell.yPosition ?: 0) >= occupiedBottom))
        }
    }

    fun findNearestCell(
        occupiedCells: List<Pair<WidgetSizes, WidgetPosition?>>,
        targetCell: WidgetPosition,
        baseCellSize: BaseWidgetScaledDimensions,
        xDelimiters: List<Int>,
        yDelimiters: List<Int>,
        widgetSize: WidgetSizes,
        calculatingPoint: Pair<Dp, Dp>
    ) : WidgetPosition {

        val targetX = calculatingPoint.first.value.toInt() ?: 0
        val targetY = calculatingPoint.second.value.toInt() ?: 0

        val possiblePositions = mutableListOf<WidgetPosition>()

        val maxRowIndex = xDelimiters.size - if (widgetSize == WidgetSizes.Small) 1 else 2
        val maxColIndex = yDelimiters.size - if (widgetSize == WidgetSizes.Large) 2 else 1

        for (rowIndex in 0..maxRowIndex) {
            for (colIndex in 0..maxColIndex) {
                val candidate = WidgetPosition(
                    xPosition = xDelimiters[rowIndex],
                    yPosition = yDelimiters[colIndex],
                    rowPosition = rowIndex,
                    columnPosition = colIndex,
                    containerConstraints = targetCell.containerConstraints
                )


                if (!isCellOccupied(candidate, occupiedCells, widgetSize, baseCellSize)) {
                    possiblePositions.add(candidate)
                }
            }
        }


        if (possiblePositions.isEmpty()) {
            return targetCell
        }


        return possiblePositions.minByOrNull { candidate ->
            val dx = (candidate.xPosition ?: 0) - targetX
            val dy = (candidate.yPosition ?: 0) - targetY
            sqrt((dx * dx + dy * dy).toDouble())
        } ?: targetCell

    }


}