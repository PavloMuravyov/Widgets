package widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.switcher

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import widgets.Theme.Paddings
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ConfigDimens

@Composable
fun AnimatedSwitcher(
    modifier: Modifier,
    isChecked: Boolean,
    density: Density,
    isCheckedChanged: (Boolean) -> Unit
) {



    Box(
        modifier
            .aspectRatio(1.2f)
            .padding(Paddings.small),
        contentAlignment = Alignment.Center
    ) {

        SwitcherThumb(isChecked, isCheckedChanged = { isCheckedChanged(it) })

        SwitcherIndicators(isChecked, density)
    }
}
