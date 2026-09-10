package widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui

import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import widgets.Theme.Colors
import widgets.Theme.Paddings
import widgets.widgetsModule.data.models.WidgetSizes
import widgets.widgetsModule.data.models.roundedShape
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ConfigDimens
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.extentions.animatedGradientBorder
import kotlin.enums.EnumEntries


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Enum<T>> DropDownMenu(
    dimens: ConfigDimens,
    selectedItem: T,
    widgets: EnumEntries<T>,
    onSelectedItemChanged: (T) -> Unit,
    expanded: Boolean,
    onExpandedChanged: (Boolean) -> Unit,
    textSize: TextUnit,
    itemHeight: Dp? = null,
    modifier: Modifier = Modifier,
    itemLabel: @Composable (T) -> String = { it.name },
) {
    val cornerShape = RoundedCornerShape(WidgetSizes.Large.roundedShape - Paddings.large)
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChanged(!expanded) },
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
            .clip(cornerShape)
            .padding(Paddings.xtraSmall),
    ) {
        Text(
            itemLabel(selectedItem), color = Colors.solidWhite, fontSize = textSize,
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChanged(false) },
            containerColor = Color.Transparent,
            shadowElevation = 0.dp,
            tonalElevation = 0.dp,
            modifier = modifier,
            scrollState = rememberScrollState(),
        ) {
            widgets.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = {
                        Text(itemLabel(item), color = Colors.solidWhite, fontSize = textSize)
                    },
                    onClick = {
                        onSelectedItemChanged(widgets[index])
                        onExpandedChanged(false)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    modifier = Modifier
                        .let { base -> itemHeight?.let { base.height(it) } ?: base }
                        .clip(cornerShape)
                        .animatedGradientBorder(
                            false,
                            width = 1.dp,
                            shape = cornerShape,
                            durationMillis = 0,
                        )
                )
                if (index != widgets.lastIndex) {
                    Spacer(
                        Modifier.fillMaxWidth(0.95f).height(Paddings.medium)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}


/*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Enum<T>> DropDownMenu(
    dimens: ConfigDimens,
    selectedItem: T,
    widgets: EnumEntries<T>,
    onSelectedItemChanged: (T) -> Unit,
    expanded: Boolean,
    onExpandedChanged: (Boolean) -> Unit,
    textSize: TextUnit,
    itemHeight: Dp? = null,
    modifier: Modifier = Modifier
) {
    val cornerShape = RoundedCornerShape(WidgetSizes.Large.roundedShape - Paddings.large)
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChanged(!expanded) },
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
            .clip(cornerShape)
            .padding(Paddings.xtraSmall),
    ) {
        Text(
            selectedItem.name, color = Colors.solidWhite, fontSize = textSize,
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChanged(false) },
            containerColor = Color.Transparent,
            shadowElevation = 0.dp,
            tonalElevation = 0.dp,
            modifier = modifier,
            scrollState = rememberScrollState(),

        ) {
            widgets.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            item.name, color = Colors.solidWhite, fontSize = textSize)
                    },
                    onClick = {
                        onSelectedItemChanged(widgets[index])
                        onExpandedChanged(false)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    modifier = Modifier
                        .let { base -> itemHeight?.let { base.height(it) } ?: base }
                        .clip(cornerShape)
                        .animatedGradientBorder(
                            false,
                            width = 1.dp,
                            shape = cornerShape,
                            durationMillis = 0,
                        )
                )
                if (index != widgets.lastIndex) {
                    Spacer(
                        Modifier.fillMaxWidth(0.95f).height(Paddings.medium)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}*/
