package widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.extentions

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


fun Modifier.animatedIndicatorShadowOffset(isChecked: Boolean): Modifier =
    this then AnimatedIndicatorOffsetElement(isChecked)

private class AnimatedIndicatorOffsetElement(
    private val isChecked: Boolean,
) : ModifierNodeElement<AnimatedIndicatorOffsetNode>() {

    override fun create(): AnimatedIndicatorOffsetNode {
        val initialOffset = if (isChecked) -8f else 8f
        return AnimatedIndicatorOffsetNode(isChecked, initialOffset)
    }

    override fun update(node: AnimatedIndicatorOffsetNode) {
        node.updateChecked(isChecked)
    }

    override fun hashCode(): Int = isChecked.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AnimatedIndicatorOffsetElement) return false
        return isChecked == other.isChecked
    }
}

private class AnimatedIndicatorOffsetNode(
    var isChecked: Boolean,
    initialOffset: Float,
) : Modifier.Node(), LayoutModifierNode {

    val offset = Animatable(initialOffset)

    fun updateChecked(checked: Boolean) {
        if (isChecked == checked) return
        isChecked = checked
        coroutineScope.launch {
            offset.animateTo(
                targetValue = if (checked) -8f else 8f,
                animationSpec = tween(durationMillis = 500),
            )
        }
    }

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureResult {
        val placeable = measurable.measure(constraints)
        return layout(placeable.width, placeable.height) {
            placeable.placeWithLayer(0, 0) {
                translationX = offset.value.dp.toPx()
            }
        }
    }
}