package widgets.widgetsModule.widgets.clock.Composables.mechanicalClock

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import widgets.Theme.Colors
import widgets.widgetsModule.widgets.clock.ClockViewModel


@Composable
fun DrawClockHands(viewModel: ClockViewModel) {
    val minutesState = viewModel.minutes.collectAsState()
    val hoursState = viewModel.hours.collectAsState()


    Box(modifier = Modifier.fillMaxSize()) {

        minutesState.value?.let { minutes ->
            MinuteHand(minutes = minutes)

        hoursState.value?.let { hours ->
            HourHand(
                hours =  hours,
                minutes = minutes
            )
        }
        }

        ClockCenter()
    }
}


@Composable
fun MinuteHand(minutes: Int) {
    val brush = remember {
        Brush.linearGradient(colors = listOf(Color(0xFFFFFFFF), Colors.solidWhite))
    }
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                rotationZ = minutes * 6f - 90f
                transformOrigin = TransformOrigin(0.5f, 0.5f)
            }
    ) {

        val cx = size.width / 2f
        val cy = size.height / 2f
        val radius = minOf(size.width, size.height) * 0.45f
        drawLine(
            brush = brush,
            start = Offset(cx - radius * 0.01f, cy),
            end = Offset(cx + radius * 0.65f, cy),
            strokeWidth = 2.8f,
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun HourHand(hours: Int, minutes: Int) {
    val brush = remember {
        Brush.linearGradient(colors = listOf(Colors.solidWhite, Color.LightGray))
    }
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                val hour12 = hours % 12
                rotationZ = hour12 * 30f + minutes * 0.5f - 90f
                transformOrigin = TransformOrigin(0.5f, 0.5f)
            }
    ) {


        val cx = size.width / 2f
        val cy = size.height / 2f
        val radius = minOf(size.width, size.height) * 0.45f
        drawLine(
            brush = brush,
            start = Offset(cx - radius * 0.01f, cy),
            end = Offset(cx + radius * 0.52f, cy),
            strokeWidth = 4.5f,
            cap = StrokeCap.Round
        )
    }
}


@Composable
private fun ClockCenter() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        drawCircle(color = Colors.solidWhite, radius = 5f, center = Offset(cx, cy))
        drawCircle(color = Color(0xFF000000), radius = 2f, center = Offset(cx, cy))
    }
}
