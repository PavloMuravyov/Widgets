package widgets.widgetsModule.widgets.notes.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.unit.Dp
import widgets.Theme.Colors
import widgets.widgetsModule.widgets.notes.NoteContentType
import kotlinx.coroutines.launch
import java.util.Objects


@Composable
fun ControlPanel (
    modifier: Modifier,
    iconSize: Dp,
    onContentTypeChanged: (NoteContentType) -> Unit){

    Row(modifier.fillMaxWidth()
        .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {

        ControlPanelIcon(Icons.Default.List, iconSize, onClick = {
            onContentTypeChanged(NoteContentType.Created)
        })

        ControlPanelIcon(Icons.Default.Add, iconSize, onClick = {
            onContentTypeChanged(NoteContentType.New)
        })

    }

}

@Composable
fun ControlPanelIcon(icon: ImageVector, iconSize: Dp, onClick: () -> Unit, modifier: Modifier = Modifier) {

    val interactionSource = remember { MutableInteractionSource() }

    Icon(icon,
        contentDescription = "",
        modifier = modifier
            .size(iconSize)
            .clickable(
                onClick = {onClick()},
                interactionSource = interactionSource,
                indication = ScaleIndication())
        ,
        tint = Colors.solidWhite,
    )
}

data class ScaleIndication(
    val targetScale: Float = 1.3f,
    val durationMillis: Int = 150
) : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return ScaleIndicationNode(interactionSource, targetScale, durationMillis)
    }
    override fun hashCode(): Int = Objects.hash(targetScale, durationMillis)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ScaleIndication) return false
        return targetScale == other.targetScale && durationMillis == other.durationMillis
    }
}

private class ScaleIndicationNode(
    private val interactionSource: InteractionSource,
    private val targetScale: Float,
    private val durationMillis: Int
) : Modifier.Node(), DrawModifierNode {
    private val animatable = Animatable(1f)

    override fun ContentDrawScope.draw() {
        scale(animatable.value) {
            this@draw.drawContent()
        }
    }

    override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is HoverInteraction.Enter -> animatable.animateTo(targetScale, tween(durationMillis))
                    is HoverInteraction.Exit -> animatable.animateTo(1.0f, tween(durationMillis))
                }
            }
        }
    }
}


