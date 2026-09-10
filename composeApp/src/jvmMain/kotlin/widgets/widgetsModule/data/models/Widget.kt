package widgets.widgetsModule.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Widget (
    val widgetType: WidgetsTypes,
    val widgetSize: WidgetSizes? = null,
    val widgetPosition: WidgetPosition? = null,
    val widgetData: String? = null,
)
