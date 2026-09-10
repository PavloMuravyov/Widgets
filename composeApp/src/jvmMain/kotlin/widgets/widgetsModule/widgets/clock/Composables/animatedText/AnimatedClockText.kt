package widgets.widgetsModule.widgets.clock.Composables.animatedText

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit


@Composable
fun AnimatedClockText(value: String, fontSize: TextUnit, color: Color,modifier: Modifier) {


    val digitAnimSpec = tween<IntOffset>(durationMillis = 300, easing = EaseInOutCubic)
    val fadeAnimSpec = tween<Float>(durationMillis = 300)
    Row {

        value.forEachIndexed { index, digit ->
            key(index) {

                AnimatedContent(
                    targetState = digit,
                    transitionSpec = {
                        slideInVertically(digitAnimSpec) { it } + fadeIn(fadeAnimSpec) togetherWith
                                slideOutVertically(digitAnimSpec) { -it } + fadeOut(fadeAnimSpec)
                    },
                    label = "clock_digit"

                ) { targetDigit ->
                    Text(
                        text = targetDigit.toString(),
                        fontSize = fontSize,
                        color = color
                    )

                }
            }
        }
    }
}

