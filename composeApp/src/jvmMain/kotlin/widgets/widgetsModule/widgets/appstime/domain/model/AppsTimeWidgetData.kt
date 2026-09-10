package widgets.widgetsModule.widgets.appstime.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AppsTimeWidgetData(
    val screenTimeHistory: ScreenTimeHistory = ScreenTimeHistory(),
    val appsTimeData: AppsTimeData = AppsTimeData(),
)