package widgets.domain.TimeService


import androidx.compose.runtime.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.milliseconds

class TimeServiceImpl : TimeService {


    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _timeData = MutableStateFlow<TimeData>(currentTimeData())
    override val timeData: StateFlow<TimeData> = _timeData

    companion object {
        private val hoursFormatter = DateTimeFormatter.ofPattern("HH")
        private val minutesFormatter = DateTimeFormatter.ofPattern("mm")
        private val dayNumFormatter = DateTimeFormatter.ofPattern("d")
        private val monthNumFormatter = DateTimeFormatter.ofPattern("M")

        private val yearFormatter = DateTimeFormatter.ofPattern("yyyy")
    }

    init {

        scope.launch {
            minuteCount()
        }
    }


    override fun currentTimeData(): TimeData {
        val now = ZonedDateTime.now()
        return TimeData(
            hours = now.format(hoursFormatter).toInt(),
            minutes = now.format(minutesFormatter).toInt(),
            dayInfo = DayInfo(
                dayNum = now.format(dayNumFormatter),
                monthNum = now.format(monthNumFormatter),
                year = now.format(yearFormatter)
            ))
    }



    override suspend fun minuteCount(){
        while (true){
            _timeData.value = currentTimeData()
            val delayMillis = 60000 - (System.currentTimeMillis() % 60000)
            delay(delayMillis.milliseconds)
        }
    }



  override fun onClose() {
        scope.cancel()
    }

}