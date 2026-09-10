package widgets.widgetsModule.widgets.clock

import androidx.lifecycle.viewModelScope
import widgets.domain.AppSettings.AppSettingsRepository
import widgets.domain.DensityProvider.DensityProvider
import widgets.domain.TimeService.DayInfo
import widgets.domain.TimeService.TimeData
import widgets.domain.TimeService.TimeService
import widgets.widgetsModule.data.models.WidgetSizes
import widgets.widgetsModule.data.models.WidgetsTypes
import widgets.widgetsModule.managers.GridPositioningManager.GridPositioningManager
import widgets.widgetsModule.managers.SystemDataManager.SystemDataManager
import widgets.widgetsModule.widgets.WidgetBaseViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ClockViewModel(
    appSettingsRepository: AppSettingsRepository,
    gridPositioningManager: GridPositioningManager,
    systemDataManager: SystemDataManager,
    densityProvider: DensityProvider,
    private val timeService: TimeService,
) : WidgetBaseViewModel(
    WidgetsTypes.Clock,
    appSettingsRepository,
    gridPositioningManager,
    densityProvider,
    systemDataManager,
) {


   private val timeData: StateFlow<TimeData> = timeService.timeData
    val hours: StateFlow<Int?> = timeData
        .map { it.hours }
        .stateIn(viewModelScope, WhileSubscribed(5000), null)

    val minutes: StateFlow<Int?>  = timeData
        .map { it.minutes }
        .stateIn(viewModelScope, WhileSubscribed(5000), null)

    val dayInfo: StateFlow<DayInfo?> = timeData
        .map { it.dayInfo }
        .distinctUntilChanged()
        .stateIn(viewModelScope, WhileSubscribed(5000), null)


}


