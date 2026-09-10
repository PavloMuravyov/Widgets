package widgets.widgetsModule.data.models

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp

@Stable
data class BaseWidgetScaledDimensions(
    val width: Dp,
    val height: Dp,
    val spacing: Dp,
)
