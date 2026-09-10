package widgets.widgetsModule.widgets.appstime.ui.mediumView.screenTime

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp
import widgets.Theme.Colors
import widgets.domain.TimeService.localizedDayNameShort
import widgets.widgetsModule.widgets.appstime.domain.model.ScreenTimeDay
import widgets.widgetsModule.widgets.appstime.ui.utils.utils.toFormattedHours


private val SPACING_FRACTION = 0.3f
private val BAR_COUNT = 7
private val GRAPH_HEIGHT_FRACTION = 0.7f


fun DrawScope.drawScreenTimeGraph(
    days: List<ScreenTimeDay>,
    graphMaxMinutes: Int,
    maxHour: Int,
    yAxisWidthPx: Float,
    textMeasurer: TextMeasurer,
    labelStyle: TextStyle,
    timeTextStyle: TextStyle,
    onBarMeasured: (BarHitArea) -> Unit = {}
) {
    val padStart = 4.dp.toPx()
    val graphWidth = size.width - padStart - yAxisWidthPx
    val graphHeight = size.height * GRAPH_HEIGHT_FRACTION

    drawYAxis(
        maxHour = maxHour,
        graphHeight = graphHeight,
        graphWidth = graphWidth,
        padStart = padStart,
        yAxisWidthPx = yAxisWidthPx,
        textMeasurer = textMeasurer,
        timeTextStyle = timeTextStyle,
    )

    drawBars(
        days = days,
        graphMaxMinutes = graphMaxMinutes,
        graphHeight = graphHeight,
        graphWidth = graphWidth,
        padStart = padStart,
        yAxisWidthPx = yAxisWidthPx,
        textMeasurer = textMeasurer,
        labelStyle = labelStyle,
        onBarMeasured = onBarMeasured
    )
}





private fun DrawScope.drawYAxis(
    maxHour: Int,
    graphHeight: Float,
    graphWidth: Float,
    padStart: Float,
    yAxisWidthPx: Float,
    textMeasurer: TextMeasurer,
    timeTextStyle: TextStyle,
) {
    val lineStartX = padStart + yAxisWidthPx
    val lineEndX = lineStartX + graphWidth

    val yLabels = buildList {
        add(0)
        if (maxHour >= 2) add(maxHour / 2)
        add(maxHour)
    }

    yLabels.forEach { hour ->
        val y = graphHeight - graphHeight * (hour.toFloat() / maxHour)

        drawLine(
            color = Colors.transparentWhite,
            start = Offset(lineStartX, y),
            end = Offset(lineEndX, y),
            strokeWidth = 0.5.dp.toPx(),
        )

        val measured = textMeasurer.measure(hour.toFormattedHours(), timeTextStyle)
        val textY = when (hour) {
            0 -> y - measured.size.height
            maxHour -> y
            else -> y - measured.size.height / 2
        }

        drawText(
            textLayoutResult = measured,
            topLeft = Offset(
                x = padStart + yAxisWidthPx - measured.size.width - 2.dp.toPx(),
                y = textY,
            ),
        )
    }
}

private fun DrawScope.drawBars(
    days: List<ScreenTimeDay>,
    graphMaxMinutes: Int,
    graphHeight: Float,
    graphWidth: Float,
    padStart: Float,
    yAxisWidthPx: Float,
    textMeasurer: TextMeasurer,
    labelStyle: TextStyle,
    onBarMeasured: (BarHitArea) -> Unit = {}
) {
    val totalSpacing = graphWidth * SPACING_FRACTION
    val barWidth = (graphWidth - totalSpacing) / BAR_COUNT
    val spacing = totalSpacing / (BAR_COUNT + 1)
    val labelY = graphHeight + 4.dp.toPx()

    days.forEachIndexed { index, day ->
        val barHeight = (day.totalMinutes.toFloat() / graphMaxMinutes) * graphHeight
        val barX = padStart + yAxisWidthPx + spacing + index * (barWidth + spacing)
        val barY = graphHeight - barHeight

        onBarMeasured(
            BarHitArea(
                index = index,
                left = barX,
                top = barY,
                right = barX + barWidth,
                bottom = graphHeight,
                day = day,
            )
        )
        drawRoundRect(
            color = Colors.semiTransparentWhite,
            topLeft = Offset(barX, graphHeight - barHeight),
            size = Size(barWidth, barHeight),
            cornerRadius = CornerRadius(4.dp.toPx()),
        )

        val measured = textMeasurer.measure(
            text = day.date.localizedDayNameShort().take(2).replaceFirstChar { it.uppercase() },
            style = labelStyle,
        )
        drawText(
            textLayoutResult = measured,
            topLeft = Offset(
                x = barX + barWidth / 2 - measured.size.width / 2,
                y = labelY,
            ),
        )
    }
}