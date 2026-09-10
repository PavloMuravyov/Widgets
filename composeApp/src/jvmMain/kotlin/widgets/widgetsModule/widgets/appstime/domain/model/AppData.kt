package widgets.widgetsModule.widgets.appstime.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class AppData(
    val appID: String,
    val appIconUrl: String,
    val appName: String,
    val days: List<ScreenTimeDay> = emptyList()
    )
