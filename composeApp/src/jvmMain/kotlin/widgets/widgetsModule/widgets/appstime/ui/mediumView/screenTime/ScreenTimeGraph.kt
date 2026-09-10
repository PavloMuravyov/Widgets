package widgets.widgetsModule.widgets.appstime.ui.mediumView.screenTime

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import widgets.Theme.Colors
import widgets.widgetsModule.widgets.appstime.domain.model.ScreenTimeDay
import widgets.widgetsModule.widgets.appstime.ui.utils.utils.toFormattedTime
import kotlin.math.ceil


@Composable
fun ScreenTimeGraph(
    days: List<ScreenTimeDay>,
    graphTimeSize: TextUnit,
    graphLabelSize: TextUnit,
    graphYAxisWidth: Dp,
    density: Density,
    modifier: Modifier = Modifier,
) {
    if (days.isEmpty()) return

    val textMeasurer = rememberTextMeasurer()
    val layoutDirection = LocalLayoutDirection.current

    val graphMaxMinutes = remember(days) {
        val maxMinutes = days.maxOf { it.totalMinutes }.coerceAtLeast(1)
        val maxHour = ceil(maxMinutes / 60f).toInt()
            .let { if (maxMinutes % 60 == 0) it + 1 else it }
        maxHour * 60
    }

    val maxHour = remember(graphMaxMinutes) { graphMaxMinutes / 60 }

    val labelStyle = remember(graphLabelSize) {
        TextStyle(
            fontSize = graphLabelSize,
            color = Colors.semiTransparentWhite,
            fontWeight = FontWeight.SemiBold,
        )
    }

    val timeTextStyle = remember(graphTimeSize) {
        TextStyle(
            fontSize = graphTimeSize,
            color = Colors.semiTransparentWhite,
        )
    }

    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    var cachedBitmap by remember { mutableStateOf<ImageBitmap?>(null) }


    var hitAreas by remember { mutableStateOf<List<BarHitArea>>(emptyList()) }
    var hoveredIndex by remember { mutableStateOf<Int?>(null) }


    LaunchedEffect(days, canvasSize) {
        val size = canvasSize
        if (size.width < 1 || size.height < 1) return@LaunchedEffect


        val collectedAreas = mutableListOf<BarHitArea>()
        val newBitmap = ImageBitmap(size.width, size.height)

        CanvasDrawScope().draw(
            density = density,
            layoutDirection = layoutDirection,
            canvas = Canvas(newBitmap),
            size = Size(size.width.toFloat(), size.height.toFloat()),
        ) {
            drawScreenTimeGraph(
                days = days,
                graphMaxMinutes = graphMaxMinutes,
                maxHour = maxHour,
                yAxisWidthPx = with(density) { graphYAxisWidth.toPx() },
                textMeasurer = textMeasurer,
                labelStyle = labelStyle,
                timeTextStyle = timeTextStyle,
                onBarMeasured = { area -> collectedAreas.add(area) },

                )
        }

        cachedBitmap = newBitmap
        hitAreas = collectedAreas
        hoveredIndex = null
    }

    Box(
        modifier = modifier
            .fillMaxHeight(0.75f)
            .fillMaxWidth()
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { if (it != canvasSize) canvasSize = it }
                .pointerInput(hitAreas) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            if (event.type == PointerEventType.Move) {
                                val pos = event.changes.first().position
                                hoveredIndex = hitAreas
                                    .firstOrNull { it.contains(pos.x, pos.y) }
                                    ?.index
                            }
                            if (event.type == PointerEventType.Exit) {
                                hoveredIndex = null
                            }
                        }
                    }
                }
        ) {
            cachedBitmap?.let { drawImage(it) }
        }

        hoveredIndex?.let { idx ->
            hitAreas.getOrNull(idx)?.let { area ->

                GraphPopup(density, area, canvasSize, graphLabelSize)
            }
        }
    }

}


@Composable
private fun GraphPopup(density: Density, area: BarHitArea, canvasSize: IntSize, graphLabelSize: TextUnit) {

    val barCenterDp = with(density) { ((area.left + area.right) / 2).toDp() }
    val barTopDp = with(density) { area.top.toDp() }
    val canvasWidthDp = with(density) { canvasSize.width.toDp() }
    val tooltipWidth = 64.dp

    val tooltipX = (barCenterDp - tooltipWidth / 2)
        .coerceIn(0.dp, canvasWidthDp - tooltipWidth)
    val tooltipY = (barTopDp - 28.dp).coerceAtLeast(0.dp)
    Popup(
        offset = IntOffset(x = tooltipX.value.toInt(), y = tooltipY.value.toInt())
    ) {
        Box(
            Modifier
                .background(
                    color = Colors.tooltipBlackBackground,
                )
                .clip(RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ){


            Text(
                text = area.day.totalMinutes.toFormattedTime(),
                style = TextStyle(
                    fontSize = graphLabelSize,
                    color = Colors.solidWhite,
                    fontWeight = FontWeight.Bold,
                ),
            )
        }
    }
}



data class BarHitArea(
    val index: Int,
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
    val day: ScreenTimeDay,
) {
    fun contains(x: Float, y: Float) = x in left..right && y in top..bottom
}