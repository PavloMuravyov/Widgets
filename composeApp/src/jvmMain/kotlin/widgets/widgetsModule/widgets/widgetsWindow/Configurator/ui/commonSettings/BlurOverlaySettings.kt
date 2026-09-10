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
import widgets.widgetsModule.data.models.BlurOverlayModes
import widgets.widgetsModule.data.models.localizedLabels
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ConfigDimens
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.DropDownMenu
import widgets.widgetsModule.widgetsManager.WidgetsManager
import org.jetbrains.compose.resources.stringResource
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.widget_extra_dimming_title


@Composable
fun BlurOverlaySettings(
    modifier: Modifier,
    dimens: ConfigDimens,
    widgetsManager: WidgetsManager,
    dropDownMenuExpanded: Boolean,
    onDropDownMenuExpandedChanged: (Boolean) -> Unit,
) {
    val blurModeLabel = stringResource(Res.string.widget_extra_dimming_title)
    val selectedMode by widgetsManager.blurOverlayMode.collectAsState()

    Column(
        modifier
            .fillMaxWidth()
            .padding(Paddings.medium),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Text(
            blurModeLabel,
            color = Colors.semiTransparentWhite, fontSize = dimens.smallInfoTextSize,
        )

        DropDownMenu(
            expanded = dropDownMenuExpanded,
            dimens = dimens,
            selectedItem = selectedMode,
            widgets = BlurOverlayModes.entries,
            onSelectedItemChanged = { newBlurOverlayMode ->
                widgetsManager.updateBlurOverlayMode(newBlurOverlayMode)
            },
            onExpandedChanged = onDropDownMenuExpandedChanged,
            textSize = dimens.showLabelTextSize,
            itemHeight = dimens.dropDownMenuBoxHeight,
            itemLabel = { stringResource(it.localizedLabels) },
        )
    }
}
