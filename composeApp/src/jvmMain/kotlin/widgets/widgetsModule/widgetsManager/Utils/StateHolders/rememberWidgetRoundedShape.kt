package widgets.widgetsModule.widgetsManager.Utils.StateHolders

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import widgets.widgetsModule.data.models.roundedShape
import widgets.widgetsModule.widgets.WidgetBaseViewModel

@Composable
fun rememberWidgetRoundedShape(viewModel: WidgetBaseViewModel): Dp {
    val size by viewModel.widgetSize.collectAsState()
    return remember(size) { size.roundedShape.value.dp }
}
