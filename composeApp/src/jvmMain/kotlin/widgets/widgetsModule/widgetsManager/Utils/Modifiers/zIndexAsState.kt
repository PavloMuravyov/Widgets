package widgets.widgetsModule.widgetsManager.Utils.Modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.unit.Constraints


fun Modifier.zIndexAsState(zIndexState: () -> Float): Modifier =
    this.then(ZIndexStateModifier(zIndexState))

private class ZIndexStateModifier(
    val zIndexState: () -> Float
) : ModifierNodeElement<ZIndexStateNode>() {
    override fun create() = ZIndexStateNode(zIndexState)
    override fun update(node: ZIndexStateNode) {
        node.zIndexState = zIndexState
    }
    override fun equals(other: Any?) =
        other is ZIndexStateModifier && other.zIndexState === zIndexState
    override fun hashCode() = zIndexState.hashCode()
}

private class ZIndexStateNode(
    var zIndexState: () -> Float
) : Modifier.Node(), LayoutModifierNode {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val placeable = measurable.measure(constraints)
        return layout(placeable.width, placeable.height) {
            placeable.placeRelativeWithLayer(0, 0, zIndex = zIndexState())
        }
    }
}
