package widgets.widgetsModule.widgets.appstime.domain

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import widgets.domain.TimeService.DayInfo
import widgets.domain.TimeService.TimeData
import widgets.domain.TimeService.TimeService
import widgets.domain.TimeService.isOlderThan
import widgets.widgetsModule.managers.SystemDataManager.SystemDataManager
import widgets.widgetsModule.widgets.appstime.domain.model.*
import widgets.widgetsModule.widgets.appstime.domain.repository.AppsTimeStorageRepository
import widgets.widgetsModule.widgets.appstime.domain.repository.ScreenTime.ScreenTimeArchiveProvider
import java.time.ZoneId


class TimesRepository(
    private val appsTimesStorageRepository: AppsTimeStorageRepository,
    private val timeService: TimeService,
    private val screenTimeArchiveProvider: ScreenTimeArchiveProvider,
    private val systemDataManager: SystemDataManager,
    private val retentionDays: Int = 7,
) {

    private val _data = MutableStateFlow(appsTimesStorageRepository.appsTimeData.value)

    val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    val timeZone: StateFlow<ZoneId> = systemDataManager.locationState
        .filterNotNull()
        .map { locationData -> ZoneId.of(locationData.timeZoneId) }
        .distinctUntilChanged()
        .stateIn(scope, SharingStarted.WhileSubscribed(5_000), ZoneId.systemDefault())

    val timeData: StateFlow<TimeData> = timeService.timeData

    val minutes: StateFlow<Int?> = timeData
        .map { it.minutes }
        .stateIn(scope, SharingStarted.Eagerly, null)

    val dayInfo: StateFlow<DayInfo> = timeData
        .map { it.dayInfo }
        .distinctUntilChanged()
        .stateIn(scope, SharingStarted.Eagerly, timeData.value.dayInfo)

    private val _appsMap = MutableStateFlow<Map<String, AppData>>(emptyMap())
    val appsMap: StateFlow<Map<String, AppData>> = _appsMap.asStateFlow()

    private val _screenArchive = MutableStateFlow<List<ScreenTimeDay>>(emptyList())
    val screenArchive: StateFlow<List<ScreenTimeDay>> = _screenArchive.asStateFlow()

    val todayScreenTime = MutableStateFlow<ScreenTimeDay?>(null)

    val appsForDay: StateFlow<List<Pair<AppData, ScreenTimeDay>>> =
        combine(appsMap, dayInfo) { map, day ->
            map.values
                .mapNotNull { app ->
                    app.days.find { it.date == day }?.let { app to it }
                }

                .sortedByDescending { (_, d) -> d.totalMinutes }
        }
            .distinctUntilChanged { old, new ->
                old.size == new.size && old.zip(new).all { (a, b) ->
                    a.first.appID == b.first.appID &&
                            a.second.totalMinutes == b.second.totalMinutes
                }
            }
            .stateIn(scope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        scope.launch {
            loadScreenTimeFromJournal()
            observeDayChange()
        }
        scope.launch {
            initAppsMap()
        }

        scope.launch {
            timeZone
                .drop(1)
                .collect {
                screenTimeArchiveProvider.getArchive(it)
            }
        }
    }

    private suspend fun loadScreenTimeFromJournal() {
        val today = dayInfo.value
        val archive = withContext(Dispatchers.IO) {
            screenTimeArchiveProvider.getArchive(timeZone.value)
        }


        _screenArchive.value = archive.lastDays.map { it.toScreenTimeDay() }
        todayScreenTime.value = ScreenTimeDay(today, archive.today.totalMinutes.toInt())

        pruneOldData(today)
    }

    private suspend fun observeDayChange() {
        dayInfo
            .drop(1)
            .collect { newDay ->
                val finishedDay = todayScreenTime.value ?: return@collect
                _screenArchive.update { archive -> archive + finishedDay }

                val freshToday = withContext(Dispatchers.IO) {
                    screenTimeArchiveProvider.getArchive(timeZone.value).today
                }
                todayScreenTime.value = ScreenTimeDay(newDay, freshToday.totalMinutes.toInt())

                pruneOldData(newDay)
                saveCurrentState()
            }
    }

    private suspend fun initAppsMap() {
        _data
            .first()
            .let { _appsMap.value = it.appsTimeData.appsTimes.associateBy { a -> a.appID } }
    }

    fun incrementScreenTime() {
        todayScreenTime.update { current ->
            current?.let { ScreenTimeDay(date = it.date, totalMinutes = it.totalMinutes + 1) }
        }
    }

    fun updateAppDays(appID: String, newDays: List<ScreenTimeDay>) {
        _appsMap.update { current ->
            val existing = current[appID] ?: return@update current
            current + (appID to AppData(existing.appID, existing.appIconUrl, existing.appName, newDays))
        }
    }

    fun addApp(app: AppData) {
        _appsMap.update { current -> current + (app.appID to app) }
    }

    fun saveCurrentState() {
        val snapshot = buildSnapshot()
        appsTimesStorageRepository.update { snapshot }
    }

    private fun buildSnapshot(): AppsTimeWidgetData {
        return AppsTimeWidgetData(
            appsTimeData = AppsTimeData(appsTimes = appsMap.value.values.toList())
        )
    }

    private fun pruneOldData(today: DayInfo) {
        _screenArchive.update { archive ->
            archive.filterNot { it.date.isOlderThan(today, retentionDays) }
        }

        _appsMap.update { current ->
            current.mapValues { (_, app) ->
                val prunedDays = app.days.filterNot { it.date.isOlderThan(today, retentionDays) }
                if (prunedDays.size == app.days.size) app
                else app.copy(days = prunedDays)
            }
        }
    }

    fun onClose() {
        saveCurrentState()
        scope.cancel()
    }
}

private fun DailyMinutes.toScreenTimeDay(): ScreenTimeDay =
    ScreenTimeDay(date = date.toDayInfo(), totalMinutes = minutes.toInt())

private fun java.time.LocalDate.toDayInfo(): DayInfo = DayInfo(
    dayNum = dayOfMonth.toString().padStart(2, '0'),
    monthNum = monthValue.toString().padStart(2, '0'),
    year = year.toString()
)
