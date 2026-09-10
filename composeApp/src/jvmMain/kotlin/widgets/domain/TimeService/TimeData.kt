package widgets.domain.TimeService

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable @Serializable
data class TimeData(
    val hours: Int,
    val minutes: Int,
    val dayInfo: DayInfo,
)