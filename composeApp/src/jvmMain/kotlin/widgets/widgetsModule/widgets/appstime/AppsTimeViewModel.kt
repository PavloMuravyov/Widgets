package widgets.widgetsModule.widgets.appstime

import androidx.lifecycle.viewModelScope
import widgets.domain.AppSettings.AppSettingsRepository
import widgets.domain.DensityProvider.DensityProvider
import widgets.domain.TimeService.TimeService
import widgets.domain.dbus.Location.LocationData
import widgets.widgetsModule.data.models.WidgetsTypes
import widgets.widgetsModule.managers.GridPositioningManager.GridPositioningManager
import widgets.widgetsModule.managers.SystemDataManager.SystemDataManager
import widgets.widgetsModule.widgets.WidgetBaseViewModel
import widgets.widgetsModule.widgets.appstime.domain.AppsTimeService
import widgets.widgetsModule.widgets.appstime.domain.repository.AppsTimeStorageRepository
import widgets.widgetsModule.widgets.appstime.domain.TimesRepository
import widgets.widgetsModule.widgets.appstime.domain.model.AppData
import widgets.widgetsModule.widgets.appstime.domain.model.ScreenTimeDay
import widgets.widgetsModule.widgets.appstime.domain.repository.GalaDbusListener
import widgets.widgetsModule.widgets.appstime.domain.repository.ScreenTime.ScreenTimeArchiveProvider
import widgets.widgetsModule.widgets.celsius.domain.model.AstroData
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.File
import java.time.ZoneId


class AppsTimeViewModel(
    gridPositioningManager: GridPositioningManager,
    appSettingsRepository: AppSettingsRepository,
    json: Json,
    timeService: TimeService,
    galaDbusListener: GalaDbusListener,
    systemDataManager: SystemDataManager,
    densityProvider: DensityProvider,
    screenTimeArchiveProvider: ScreenTimeArchiveProvider
    ) : WidgetBaseViewModel (
    WidgetsTypes.AppTimes,
    appSettingsRepository,
    gridPositioningManager,
    densityProvider,
    systemDataManager,
) {


    private val appsTimesStorageRepository = AppsTimeStorageRepository(
        json = json,
        viewModelScope,
    )

    private val timesRepository = TimesRepository(
        appsTimesStorageRepository,
        timeService,
        screenTimeArchiveProvider,
        systemDataManager
        ).also {
            AppsTimeService(it, galaDbusListener)
    }


    private val screenTimeFlow = combine(
        timesRepository.screenArchive,
        timesRepository.todayScreenTime
    ) { archive, today ->
        Pair(archive, today)
    }


    val todayScreenTime = timesRepository.todayScreenTime
    val todayAppsTime = timesRepository.appsForDay
        .map { list ->
            list.filter { item ->
                val iconPath = item.first.appIconUrl
                iconPath.isNotBlank() && File(iconPath).exists()
        }
        }.stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = emptyList())





    val last7DaysScreenTime: StateFlow<List<ScreenTimeDay>> =
        screenTimeFlow
            .map { (archive, _) ->
                archive
                    .takeLast(7)
                    .sortedWith(compareBy(
                        { it.date.year.toInt() },
                        { it.date.monthNum.toInt() },
                        { it.date.dayNum.toInt() }
                    ))

            }
            .distinctUntilChanged()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val last7DaysAppsTime: StateFlow<List<Pair<AppData, Int>>> =
        timesRepository.appsMap.map { map ->
            map.values
                .map { app -> app to app.days.takeLast(7).sumOf { it.totalMinutes } }
                .filter { (app, total) ->
                    val iconPath = app.appIconUrl
                    total > 0 && iconPath.isNotBlank() && File(iconPath).exists()
                }
                .sortedByDescending { (_, total) -> total }
        }
            .distinctUntilChanged()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    override fun onCleared() {
        timesRepository.onClose()
        appsTimesStorageRepository.onClose()
        super.onCleared()
    }

}

