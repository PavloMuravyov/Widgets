package widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.extentions

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.unit.Constraints
import widgets.Theme.Colors
import kotlinx.coroutines.launch
import kotlinx.serialization.EncodeDefault


fun Modifier.animatedThumbColor(isChecked: Boolean): Modifier =
    this then AnimatedColorElement(isChecked)





private class AnimatedColorNode(
    var isChecked: Boolean
) : Modifier.Node(), DrawModifierNode {

    val colorAnim = Animatable(colorFor(isChecked))

    private fun colorFor(checked: Boolean) =
        if (checked) Colors.switcherEnabled else Colors.switcherDisabled

    fun updateChecked(checked: Boolean) {
        if (isChecked == checked) return
        isChecked = checked
        coroutineScope.launch {
            colorAnim.animateTo(colorFor(checked), animationSpec = tween(1000))
        }
    }

    override fun ContentDrawScope.draw() {
        drawRect(color = colorAnim.value)
        drawContent()
    }
}

private class AnimatedColorElement(
    private val isChecked: Boolean
) : ModifierNodeElement<AnimatedColorNode>() {
    override fun create() = AnimatedColorNode(isChecked)
    override fun update(node: AnimatedColorNode) {
        node.updateChecked(isChecked)
    }
    override fun hashCode() = isChecked.hashCode()
    override fun equals(other: Any?) = other is AnimatedColorElement && other.isChecked == isChecked
}

