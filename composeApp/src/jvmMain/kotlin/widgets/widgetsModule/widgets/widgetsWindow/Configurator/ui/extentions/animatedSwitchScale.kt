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


fun Modifier.animatedSwitchScale(isChecked: Boolean, enabledIndicator:Boolean ): Modifier =
    this then AnimatedSwitchScaleElement(isChecked, enabledIndicator)


private class AnimatedSwitchScaleElement(
    private val isChecked: Boolean,
    private val enabledIndicator: Boolean,
) : ModifierNodeElement<AnimatedSwitchScaleNode>() {

    override fun create(): AnimatedSwitchScaleNode {
        val initialScale = if ((isChecked && enabledIndicator) || (!isChecked && !enabledIndicator)) 1f else 0f
        return AnimatedSwitchScaleNode(isChecked, initialScale)
    }
    override fun update(node: AnimatedSwitchScaleNode) {
        node.updateScale( isChecked, enabledIndicator )
    }

    override fun hashCode(): Int = 31 * isChecked.hashCode() + enabledIndicator.hashCode()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AnimatedSwitchScaleElement) return false
        return isChecked == other.isChecked && enabledIndicator == other.enabledIndicator
    }

}

private class AnimatedSwitchScaleNode(
    var isChecked: Boolean,
    initialScale: Float, ) : Modifier.Node(), LayoutModifierNode {
    val scale = Animatable(initialScale)

    fun updateScale(checked: Boolean, enabledIndic: Boolean) {

        if (isChecked == checked) return
        isChecked = checked

        coroutineScope.launch {
            if((checked && enabledIndic) || (!checked && !enabledIndic)) {
                scale.animateTo(1.0f, animationSpec = tween(durationMillis = 500) )
            } else scale.animateTo(0.0f, animationSpec = tween(durationMillis = 500))
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