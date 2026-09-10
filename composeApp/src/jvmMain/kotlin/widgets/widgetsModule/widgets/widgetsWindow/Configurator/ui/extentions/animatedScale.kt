package widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.extentions

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Constraints
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private class AnimatedScaleElement(
    private val isSelectedSize: Boolean,
    private val durationMillis: Int,
    private val targetScale: Float,
    private val defaultScale: Float
) : ModifierNodeElement<AnimatedScaleNode>() {

    override fun create() = AnimatedScaleNode(
        isSelectedSize, durationMillis, targetScale, defaultScale
    )

    override fun update(node: AnimatedScaleNode) {
        node.updateParams(isSelectedSize, durationMillis, targetScale, defaultScale)
    }

    override fun hashCode(): Int {
        var result = isSelectedSize.hashCode()
        result = 31 * result + durationMillis
        result = 31 * result + targetScale.hashCode()
        result = 31 * result + defaultScale.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AnimatedScaleElement) return false
        return isSelectedSize == other.isSelectedSize &&
                durationMillis == other.durationMillis &&
                targetScale == other.targetScale &&
                defaultScale == other.defaultScale
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "animatedScale"
        properties["isSelectedSize"] = isSelectedSize
    }
}

private class AnimatedScaleNode(
    private var isSelectedSize: Boolean,
    private var durationMillis: Int,
    private var targetScale: Float,
    private var defaultScale: Float
) : Modifier.Node(), LayoutModifierNode {

    private val scaleAnim = Animatable(
        if (isSelectedSize) targetScale else defaultScale
    )
    private var animJob: Job? = null

    fun updateParams(
        isSelectedSize: Boolean,
        durationMillis: Int,
        targetScale: Float,
        defaultScale: Float
    ) {
        val changed = this.isSelectedSize != isSelectedSize
        this.isSelectedSize = isSelectedSize
        this.durationMillis = durationMillis
        this.targetScale = targetScale
        this.defaultScale = defaultScale

        if (changed) {
            animJob?.cancel()
            animJob = coroutineScope.launch {
                scaleAnim.animateTo(
                    targetValue = if (isSelectedSize) targetScale else defaultScale,
                    animationSpec = tween(durationMillis, easing = LinearOutSlowInEasing)
                ) {
                }
            }
        }
    }

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val placeable = measurable.measure(constraints)
        return layout(placeable.width, placeable.height) {
            placeable.placeWithLayer(0, 0) {
                scaleX = scaleAnim.value
                scaleY = scaleAnim.value
            }
        }
    }
}

fun Modifier.animatedScale(
    isSelectedSize: Boolean,
    durationMillis: Int = 300,
    targetScale: Float = 1.15f,
    defaultScale: Float = 1f
): Modifier = this then AnimatedScaleElement(
    isSelectedSize, durationMillis, targetScale, defaultScale
)