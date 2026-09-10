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


fun Modifier.visibilityAlpha(visible: Boolean): Modifier =
    this then VisibilityAlphaElement(visible)

private class VisibilityAlphaElement(
    private val visible: Boolean,
) : ModifierNodeElement<VisibilityAlphaNode>() {

    override fun create(): VisibilityAlphaNode {
        return VisibilityAlphaNode(visible)
    }

    override fun update(node: VisibilityAlphaNode) {
        node.updateVisible(visible)
    }

    override fun hashCode(): Int = visible.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is VisibilityAlphaElement) return false
        return visible == other.visible
    }
}

private class VisibilityAlphaNode(
    var visible: Boolean,
) : Modifier.Node(), LayoutModifierNode {

    var visibleAlpha = Animatable(if(visible) 1f else 0f)

    fun updateVisible(visible: Boolean) {
        coroutineScope.launch {
            if (visible) {
                visibleAlpha.animateTo(1.0f, animationSpec = tween(250))
            } else visibleAlpha.animateTo(0.0f, animationSpec = tween(250))
        }
    }


    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureResult {
        val placeable = measurable.measure(constraints)
        return layout(placeable.width, placeable.height) {
            placeable.placeWithLayer(0, 0) {
                alpha = visibleAlpha.value
            }
        }
    }
}

