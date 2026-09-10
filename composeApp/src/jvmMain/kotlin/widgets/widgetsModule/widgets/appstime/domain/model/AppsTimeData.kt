package widgets.widgetsModule.widgets.appstime.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class AppsTimeData(
    val appsTimes: List<AppData> = emptyList(),
)