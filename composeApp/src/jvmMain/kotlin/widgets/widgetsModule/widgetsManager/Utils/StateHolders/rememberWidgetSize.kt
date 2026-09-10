package widgets.widgetsModule.widgetsManager.Utils.StateHolders

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import widgets.widgetsModule.data.models.BaseWidgetScaledDimensions
import widgets.widgetsModule.data.models.scaledSize
import widgets.widgetsModule.widgets.WidgetBaseViewModel

@Composable
fun rememberWidgetSize(
    viewModel: WidgetBaseViewModel,
    baseWidgetScaledDimensions: BaseWidgetScaledDimensions,
): DpSize {
    val size by viewModel.widgetSize.collectAsState()
    return remember(baseWidgetScaledDimensions, size) {
        size.scaledSize(baseWidgetScaledDimensions)
    }
}

