package widgets.widgetsModule.data.models

import androidx.compose.ui.unit.DpSize
import kotlinx.serialization.Serializable

@Serializable
enum class WidgetsTypes {
    Celsius,
    Notes,
    Clock,
    AppTimes,

}


val WidgetsTypes.availableSizes: List<WidgetSizes>
        get() = when (this) {
            WidgetsTypes.Celsius -> listOf(WidgetSizes.Small, WidgetSizes.Medium, WidgetSizes.Large)
            WidgetsTypes.Notes -> listOf(WidgetSizes.Small, WidgetSizes.Medium, WidgetSizes.Large)
            WidgetsTypes.Clock -> listOf(WidgetSizes.Small, WidgetSizes.Medium)
            WidgetsTypes.AppTimes -> listOf(WidgetSizes.Small, WidgetSizes.Medium)
        }

