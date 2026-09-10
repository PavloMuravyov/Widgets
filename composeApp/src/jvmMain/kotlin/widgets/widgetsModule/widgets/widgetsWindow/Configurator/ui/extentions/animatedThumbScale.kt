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
import kotlinx.coroutines.launch


fun Modifier.animatedThumbScale(isChecked: Boolean): Modifier =
    this then AnimatedThumbScaleElement(isChecked)


private class AnimatedThumbScaleElement(
    private val isChecked: Boolean,
) : ModifierNodeElement<AnimatedThumbScaleNode>() {

    override fun create(): AnimatedThumbScaleNode = AnimatedThumbScaleNode(isChecked)

    override fun update(node: AnimatedThumbScaleNode) {
        node.updateChecked(isChecked)
    }

    override fun hashCode(): Int {
        return isChecked.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AnimatedThumbScaleElement) return false
        return isChecked == other.isChecked
    }

}

private class AnimatedThumbScaleNode(var isChecked: Boolean): Modifier.Node(), LayoutModifierNode {
    val scale = Animatable(1f)

    fun updateChecked(checked: Boolean) {
        if (isChecked == checked) return
        isChecked = checked
        coroutineScope.launch {
            scale.animateTo(1.3f, tween(350))
            scale.animateTo(1f, tween(350))
        }
    }

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val placeable = measurable.measure(constraints)
        return layout(placeable.width, placeable.height) {
            placeable.placeWithLayer(0, 0) {
                scaleX = scale.value
                scaleY = scale.value
            }
        }
    }

}