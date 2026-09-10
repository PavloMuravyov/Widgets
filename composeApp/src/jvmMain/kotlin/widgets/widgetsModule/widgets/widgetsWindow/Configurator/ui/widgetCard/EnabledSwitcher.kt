package widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.widgetCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import widgets.Theme.Colors
import widgets.Theme.Paddings
import widgets.widgetsModule.data.models.WidgetsTypes
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ConfigDimens
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.switcher.AnimatedSwitcher
import widgets.widgetsModule.widgetsManager.WidgetsManager
import org.jetbrains.compose.resources.stringResource
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.widget_show_toggle

@Composable
fun EnabledSwitcher(
    modifier: Modifier,
    dimens: ConfigDimens,
    selectedWidgetType: WidgetsTypes,
    widgetsManager: WidgetsManager
) {

    val showWidgetLabel = stringResource(Res.string.widget_show_toggle)

    Row(
        modifier.fillMaxSize()
            .padding(Paddings.medium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(showWidgetLabel, Modifier.weight(4f), color = Colors.solidWhite, fontSize = dimens.showLabelTextSize)

        val enabledTypes = widgetsManager.enabledWidgetTypes
        var isChecked by remember(selectedWidgetType) { mutableStateOf( enabledTypes.value.contains(selectedWidgetType)) }


        val isCheckedChanged = { newChecked: Boolean ->
            isChecked = newChecked
            widgetsManager.updateWidgetEnabledState(selectedWidgetType, newChecked)
        }

        AnimatedSwitcher(
            Modifier.weight(1f),
            isChecked = isChecked,
            widgetsManager.density.value,
            isCheckedChanged
        )
    }
}
