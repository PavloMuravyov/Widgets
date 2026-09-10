package widgets.widgetsModule.widgets.appstime.domain.repository.ScreenTime

import java.time.Instant

data class TimeInterval(
    val id: String,
    val start: Instant,
    val end: Instant,
)

typealias BootInterval = TimeInterval
typealias SleepInterval = TimeInterval