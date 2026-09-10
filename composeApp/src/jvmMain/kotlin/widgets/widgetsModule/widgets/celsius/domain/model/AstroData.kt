package widgets.widgetsModule.widgets.celsius.domain.model

import java.time.ZonedDateTime
import kotlin.time.Instant

data class AstroData(
    val isDay: Boolean,
    val length: Int,
    val sunrise: ZonedDateTime?,
    val sunset: ZonedDateTime?,
)
