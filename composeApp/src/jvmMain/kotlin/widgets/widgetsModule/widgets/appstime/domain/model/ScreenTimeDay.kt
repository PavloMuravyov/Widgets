package widgets.widgetsModule.widgets.appstime.domain.model

import widgets.domain.TimeService.DayInfo
import kotlinx.serialization.Serializable

@Serializable
data class ScreenTimeDay(
    val date: DayInfo,
    val totalMinutes: Int,
)
