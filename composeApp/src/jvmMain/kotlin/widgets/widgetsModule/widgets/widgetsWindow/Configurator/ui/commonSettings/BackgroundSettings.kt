package widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.commonSettings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import widgets.Theme.Colors
import widgets.Theme.Paddings
import widgets.widgetsModule.data.models.BackgroundTypes
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ConfigDimens
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.DropDownMenu
import widgets.widgetsModule.widgetsManager.WidgetsManager
import org.jetbrains.compose.resources.stringResource
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.widget_background_style_title


@Composable
fun BackgroundSettings (
    modifier: Modifier,
    dimens: ConfigDimens,
    widgetsManager: WidgetsManager,
    dropDownMenuExpanded: Boolean,
    onDropDownMenuExpandedChanged: (Boolean) -> Unit
){

    val selectedBackgroundType by widgetsManager.backgroundType.collectAsState()

    val backgroundLabel = stringResource(Res.string.widget_background_style_title)

    Column(modifier
        .fillMaxWidth()
        .padding(Paddings.medium),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceEvenly,){

        Text(backgroundLabel, color = Colors.semiTransparentWhite, fontSize = dimens.smallInfoTextSize)

        DropDownMenu(
            dimens = dimens,
            selectedItem = selectedBackgroundType,
            widgets = BackgroundTypes.entries,
            onSelectedItemChanged = {
                widgetsManager.updateBackgroundType(it)
            },
            expanded = dropDownMenuExpanded,
            onExpandedChanged = onDropDownMenuExpandedChanged,
            textSize = dimens.showLabelTextSize,
            itemHeight = dimens.dropDownMenuBoxHeight,
        )
    }

}
