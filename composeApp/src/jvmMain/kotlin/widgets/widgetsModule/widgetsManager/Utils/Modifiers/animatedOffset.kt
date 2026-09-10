package widgets.widgetsModule.widgetsManager.Utils.Modifiers

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp


fun Modifier.animatedOffset(offset: Animatable<DpOffset, AnimationVector2D>) =
    this.graphicsLayer {
        translationX = offset.value.x.toPx()
        translationY = offset.value.y.toPx()
    }

val DpOffsetToVector: TwoWayConverter<DpOffset, AnimationVector2D> =
    TwoWayConverter(
        convertToVector = { AnimationVector2D(it.x.value, it.y.value) },
        convertFromVector = { DpOffset(it.v1.dp, it.v2.dp) }
    )

