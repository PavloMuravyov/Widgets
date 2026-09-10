package widgets.domain.TimeService

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface TimeService {

    val timeData: StateFlow<TimeData>



    fun currentTimeData(): TimeData

    suspend fun minuteCount()
    fun onClose()
}