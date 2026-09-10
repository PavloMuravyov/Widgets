package widgets.widgetsModule.widgets.appstime.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ScreenTimeHistory(
    val days: List<ScreenTimeDay> = emptyList(),
)
