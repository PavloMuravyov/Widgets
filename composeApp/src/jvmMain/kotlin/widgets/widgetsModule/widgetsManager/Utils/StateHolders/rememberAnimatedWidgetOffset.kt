package widgets.widgetsModule.widgetsManager.Utils.StateHolders

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import widgets.widgetsModule.data.models.WidgetPosition
import widgets.widgetsModule.widgetsManager.Utils.Modifiers.DpOffsetToVector
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.absoluteValue
import kotlin.math.sqrt


@Composable
fun rememberAnimatedWidgetOffset(
    positionFlow: StateFlow<WidgetPosition?>,
    onAnimationEndChange: (Long) -> Unit,
): Animatable<DpOffset, AnimationVector2D> {


    val animatedOffset = remember {
        val initial = positionFlow.value
        Animatable(
            initialValue = DpOffset(
                initial?.xPosition?.absoluteValue?.dp ?: 0.dp,
                initial?.yPosition?.absoluteValue?.dp ?: 0.dp
            ),
            typeConverter = DpOffsetToVector
        )
    }

    LaunchedEffect(Unit) {
        positionFlow.collectLatest { position ->
            if (position?.xPosition != null && position.yPosition != null) {


                val target = DpOffset(position.xPosition.dp, position.yPosition.dp)

                val current = animatedOffset.value
                val dx = (target.x - current.x).value
                val dy = (target.y - current.y).value
                val distance = sqrt(dx * dx + dy * dy)

                val durationMillis = (distance * 1.5f)  // 1.5ms на dp
                    .coerceIn(10f, 200f)
                    .toInt()

                animatedOffset.animateTo(
                    targetValue = target,
                    animationSpec = tween(
                        durationMillis = durationMillis,
                        easing = EaseIn
                    )
                )

                onAnimationEndChange(System.currentTimeMillis())


            }
        }
    }

    return animatedOffset
}
