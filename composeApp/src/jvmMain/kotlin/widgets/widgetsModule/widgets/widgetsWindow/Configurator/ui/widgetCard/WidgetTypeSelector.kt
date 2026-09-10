package widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.widgetCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import widgets.Theme.Colors
import widgets.Theme.Paddings
import widgets.widgetsModule.data.models.WidgetsTypes
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ConfigDimens
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.DropDownMenu
import org.jetbrains.compose.resources.stringResource
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.widget_name_title


@Composable
fun WidgetTypeSelector(
    modifier: Modifier,
    dimens: ConfigDimens,
    selectedItem: WidgetsTypes,
    dropDownMenuExpanded: Boolean,
    onDropDownMenuChanged: (Boolean) -> Unit,
    onSelectedItemChanged: (WidgetsTypes) -> Unit
) {
    val widgetNameLabel = stringResource(Res.string.widget_name_title)

    Column(
        modifier
            .fillMaxSize()
            .padding(horizontal = Paddings.medium),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {

        Text(widgetNameLabel, color = Colors.semiTransparentWhite, fontSize = dimens.smallInfoTextSize)

        DropDownMenu(
            dimens = dimens,
            selectedItem = selectedItem,
            widgets = WidgetsTypes.entries,
            onSelectedItemChanged = onSelectedItemChanged,
            expanded = dropDownMenuExpanded,
            onExpandedChanged = onDropDownMenuChanged,
            textSize = dimens.baseInfoTextSize,
        )    }

}
/*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropDownMenu(
    dimens: ConfigDimens,
    selectedItem: WidgetsTypes,
    widgets: EnumEntries<WidgetsTypes>,
    onSelectedItemChanged: (WidgetsTypes) -> Unit,
    expanded: Boolean,
    onExpandedChanged: (Boolean) -> Unit
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
            selectedItem.name, color = Colors.solidWhite, fontSize = dimens.baseInfoTextSize,

            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,

            onDismissRequest = {
                onExpandedChanged(false)
            },

            containerColor = Color.Transparent,

            shadowElevation = 0.dp,

            tonalElevation = 0.dp,
        ) {
            widgets.forEachIndexed { index, item ->
                DropdownMenuItem(

                    text = {
                        Text(item.name, color = Colors.solidWhite, fontSize = dimens.baseInfoTextSize)
                    },
                    onClick = {
                        onSelectedItemChanged(widgets[index])
                        onExpandedChanged(false)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    modifier = Modifier

                        .clip(cornerShape)

                        .animatedGradientBorder(
                            false,
                            width = 1.dp,
                            shape = cornerShape,
                            durationMillis = 0,

                        )

                )

                if(index != widgets.lastIndex) {
                    Spacer(
                        Modifier.fillMaxWidth(0.95f).height(Paddings.medium)
                            .align (Alignment.CenterHorizontally)
                    )
                }
            }

        }
    }
}
*/
