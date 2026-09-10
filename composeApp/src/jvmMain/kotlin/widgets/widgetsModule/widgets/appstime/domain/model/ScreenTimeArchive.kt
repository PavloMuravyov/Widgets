package widgets.widgetsModule.widgets.appstime.domain.model


import java.time.Instant
import java.time.LocalDate

data class ScreenTimeArchive(
    val lastDays: List<DailyMinutes>,
    val today: TodayScreenTime
)

data class DailyMinutes(val date: LocalDate, val minutes: Long)

data class TodayScreenTime(
    val date: LocalDate,
    val totalMinutes: Long,
)



