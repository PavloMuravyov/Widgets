package widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.extentions

import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import widgets.Theme.Colors
import kotlinx.coroutines.launch

private class AnimatedGradientBorderNode(
    var isChecked: Boolean,
    var width: Dp,
    var shape: Shape,
    var durationMillis: Int,
    var onColor: Color,
    var offColor: Color
) : Modifier.Node(), DrawModifierNode {

    private val startColorAnim = Animatable(colorFor(start = true, isChecked))
    private val endColorAnim = Animatable(colorFor(start = false, isChecked))

    private fun colorFor(start: Boolean, checked: Boolean): Color {
        return if (start) {
            if (checked) offColor else onColor
        } else {
            if (checked) onColor else offColor
        }
    }

    fun update(
        isChecked: Boolean,
        width: Dp,
        shape: Shape,
        durationMillis: Int,
        onColor: Color,
        offColor: Color
    ) {
        this.width = width
        this.shape = shape
        this.durationMillis = durationMillis
        this.onColor = onColor
        this.offColor = offColor

        if (this.isChecked == isChecked) return
        this.isChecked = isChecked

        coroutineScope.launch {
            startColorAnim.animateTo(
                colorFor(start = true, isChecked),
                animationSpec = tween(durationMillis)
            )
        }
        coroutineScope.launch {
            endColorAnim.animateTo(
                colorFor(start = false, isChecked),
                animationSpec = tween(durationMillis)
            )
        }
    }

    override fun ContentDrawScope.draw() {
        drawContent()

        val strokeWidthPx = width.toPx()
        val outline = shape.createOutline(size, layoutDirection, this)

        val brush = Brush.linearGradient(
            colors = listOf(startColorAnim.value, endColorAnim.value)
        )

        drawOutline(
            outline = outline,
            brush = brush,
            style = Stroke(width = strokeWidthPx)
        )
    }
}

private data class AnimatedGradientBorderElement(
    val isChecked: Boolean,
    val width: Dp,
    val shape: Shape,
    val durationMillis: Int,
    val onColor: Color,
    val offColor: Color
) : ModifierNodeElement<AnimatedGradientBorderNode>() {

    override fun create() = AnimatedGradientBorderNode(
        isChecked, width, shape, durationMillis, onColor, offColor
    )

    override fun update(node: AnimatedGradientBorderNode) {
        node.update(isChecked, width, shape, durationMillis, onColor, offColor)
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "animatedGradientBorder"
        properties["isChecked"] = isChecked
        properties["width"] = width
        properties["shape"] = shape
        properties["durationMillis"] = durationMillis
        properties["onColor"] = onColor
        properties["offColor"] = offColor
    }
}

fun Modifier.animatedGradientBorder(
    isChecked: Boolean,
    width: Dp = 2.dp,
    shape: Shape = CircleShape,
    durationMillis: Int = 1000,
    onColor: Color = Colors.solidWhite,
    offColor: Color = Color.Transparent
): Modifier = this then AnimatedGradientBorderElement(
    isChecked, width, shape, durationMillis, onColor, offColor
)