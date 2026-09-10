package widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.widgetCard

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import widgets.Theme.Colors
import widgets.Theme.Paddings
import widgets.widgetsModule.data.models.DesignEtalon
import widgets.widgetsModule.data.models.WidgetSizes
import widgets.widgetsModule.data.models.WidgetsTypes
import widgets.widgetsModule.data.models.availableSizes
import widgets.widgetsModule.data.models.getAbsoluteSize
import widgets.widgetsModule.data.models.roundedShape
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ConfigDimens
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.extentions.animatedGradientBorder
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.extentions.animatedScale
import widgets.widgetsModule.widgetsManager.WidgetsManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.widget_size_title


@Composable
fun WidgetSizeSelector(
    modifier: Modifier = Modifier,
    dimens: ConfigDimens,
    selectedItem: WidgetsTypes,
    size: WidgetSizes,
    onSizeChange: (WidgetSizes) -> Unit
) {
    val widgetSizeLabel = stringResource(Res.string.widget_size_title)

    Column(
        modifier.fillMaxSize()
            .padding(Paddings.medium),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Text(widgetSizeLabel, color = Colors.semiTransparentWhite, fontSize = dimens.smallInfoTextSize)

        SizesVisualizedSelector(selectedItem, size, dimens, onSizeChange)


    }

}

@Composable
private fun SizesVisualizedSelector(
    selectedItem: WidgetsTypes,
    selectedWidgetSize: WidgetSizes,
    dimens: ConfigDimens,
    onSizeChange: ( WidgetSizes) -> Unit
) {


    BoxWithConstraints(Modifier.fillMaxSize()) {

        val baseHeight = maxHeight / 5f
        val smallWidgetVisualSize = DpSize(baseHeight * DesignEtalon().widgetAspectRatio, baseHeight)

        val cellWidth = maxWidth / 3.5f

        Row(
            Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            val availableSizes = selectedItem.availableSizes

            availableSizes.forEach { size ->

                SizeVisualizedContainer(
                    Modifier.width(cellWidth).aspectRatio(1.1f), size = size,
                    smallWidgetVisualSize,
                    size == selectedWidgetSize,
                    dimens.smallInfoTextSize
                ) { newSize -> onSizeChange( newSize) }
                }
            }
        }
}

@Composable
private fun SizeVisualizedContainer(
    modifier: Modifier,
    size: WidgetSizes,
    baseSize: DpSize,
    isSelectedSize: Boolean,
    textSize: TextUnit,
    onSizeChange: (WidgetSizes) -> Unit
) {


    val baseColor = if (isSelectedSize) {
        Colors.semiTransparentWhite
    } else Colors.semiTransparentWhite40Percent
    val backgroundColor = if (isSelectedSize) {
        Colors.transparentWhite
    } else Color.Transparent

    Column(
        modifier
            .animatedScale(isSelectedSize = isSelectedSize)
            .clip(RoundedCornerShape(WidgetSizes.Large.roundedShape - Paddings.large - Paddings.small))
            .animatedGradientBorder(
                false,
                width = 1.dp,
                shape = RoundedCornerShape(WidgetSizes.Large.roundedShape - Paddings.large - Paddings.small)
            )
            .background(backgroundColor)
            .clickable(onClick = { onSizeChange(size) })
            .padding(Paddings.medium),

        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val visualizerSize = size.getAbsoluteSize(baseSize, size)

        Box(
            Modifier.size(visualizerSize)
                .clip(RoundedCornerShape(WidgetSizes.Large.roundedShape - Paddings.large - Paddings.medium))
                .background(baseColor)
        )

        Text(size.name, fontSize = textSize, color = baseColor)

    }
}

