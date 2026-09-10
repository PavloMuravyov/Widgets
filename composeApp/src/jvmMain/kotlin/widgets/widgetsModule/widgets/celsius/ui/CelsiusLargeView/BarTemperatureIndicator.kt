package widgets.widgetsModule.widgets.celsius.ui.CelsiusLargeView

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import widgets.Theme.Colors
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel.TemperatureData


@Composable
fun CelsiusLargeScope.BarTemperatureIndicator(
    minTemp: Double,
    maxTemp: Double,
    currentTemp: TemperatureData,
    globalMin: Double,
    globalMax: Double,
    lineHeight: Dp,
    modifier: Modifier = Modifier
) {

    Box(
        modifier.height(lineHeight).background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {

        val tempFraction = remember(minTemp, maxTemp, currentTemp.current, globalMin, globalMax) {
            val range = (globalMax - globalMin).takeIf { it > 0 } ?: 1.0
            Triple(
                (minTemp - globalMin) / range,
                (maxTemp - globalMin) / range,
                (currentTemp.current - globalMin) / range
            )
        }


        Canvas(modifier = Modifier.fillMaxSize()) {


            val w = size.width
            val h = size.height
            val centerY = h / 2f
            drawLine(
                color = Colors.transparentBlackOverlays,
                start = Offset(0f, centerY),
                end = Offset(w, centerY),
                strokeWidth = h, cap = StrokeCap.Round
            )

            val startX = (tempFraction.first * w).toFloat().coerceIn(0f, w)
            val endX = (tempFraction.second * w).toFloat().coerceIn(0f, w)
            val markerX = (tempFraction.third * w).toFloat().coerceIn(startX, endX)


            val gradient = Brush.horizontalGradient(
                0f to Colors.blueTempIndicator,
                ((markerX - startX) / (endX - startX)).coerceIn(0f, 1f) to Colors.greenTempIndicator,
                1f to Colors.yellowTempIndicator,
                startX = 0f,
                endX = Float.POSITIVE_INFINITY
            )

            drawLine(
                brush = gradient,
                start = Offset(startX, centerY),
                end = Offset(endX, centerY),
                strokeWidth = h,
                cap = StrokeCap.Round
            )
        }

    }

}