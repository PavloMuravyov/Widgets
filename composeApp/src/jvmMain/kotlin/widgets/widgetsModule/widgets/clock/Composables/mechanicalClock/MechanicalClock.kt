package widgets.widgetsModule.widgets.clock.Composables.mechanicalClock

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import widgets.Theme.Colors
import widgets.widgetsModule.widgets.clock.ClockViewModel


@Composable
fun MechanicalClock(
    viewModel: ClockViewModel,
    modifier: Modifier,
){
    Box(
        modifier = modifier
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
           ,
        contentAlignment = Alignment.Center
    ){

        DrawClockDecorations()
        DrawClockHands(viewModel)

    }

}
