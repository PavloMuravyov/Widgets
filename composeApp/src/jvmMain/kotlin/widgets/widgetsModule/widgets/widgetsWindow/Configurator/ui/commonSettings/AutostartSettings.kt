package widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.commonSettings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ConfigDimens
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.switcher.AnimatedSwitcher
import widgets.widgetsModule.widgetsManager.WidgetsManager
import org.jetbrains.compose.resources.stringResource
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.widget_launch_at_startup_title


@Composable
fun AutostartSettings(
    modifier: Modifier,
    dimens: ConfigDimens,
    widgetsManager: WidgetsManager,
){
    val autostartLabel = stringResource(Res.string.widget_launch_at_startup_title)

    var isChecked by remember { mutableStateOf( widgetsManager.autostartState.value) }
    val isCheckedChanged = { newChecked: Boolean ->
        isChecked = newChecked
        widgetsManager.updateAutostartState(newChecked)
    }
    Row(modifier
        .fillMaxWidth()
        .padding(Paddings.medium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,){

        Text(autostartLabel,
            modifier = Modifier.weight(4f),
            color = Colors.solidWhite,
            fontSize = dimens.showLabelTextSize)

        AnimatedSwitcher(
            Modifier.weight(1f).fillMaxHeight(0.85f),
            isChecked = isChecked,
            density = widgetsManager.density.value,
            isCheckedChanged = isCheckedChanged
        )
    }

}