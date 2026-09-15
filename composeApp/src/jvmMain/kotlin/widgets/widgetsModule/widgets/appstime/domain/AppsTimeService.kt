package widgets.widgetsModule.widgets.appstime.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import widgets.domain.TimeService.DayInfo
import widgets.widgetsModule.widgets.appstime.domain.model.AppData
import widgets.widgetsModule.widgets.appstime.domain.model.RunningApplication
import widgets.widgetsModule.widgets.appstime.domain.model.ScreenTimeDay
import widgets.widgetsModule.widgets.appstime.domain.repository.GalaDbusListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppsTimeService(
    private val repository: TimesRepository,
    private val galaDbusListener: GalaDbusListener,
) {
    val runningApplications = galaDbusListener.runningApplication
    private val seenThisMinute = MutableStateFlow<Set<RunningApplication>>(emptySet())


    val scope = CoroutineScope(Dispatchers.Default)

    init {
        repository.scope.launch { galaDbusListener.startListening() }
        repository.scope.launch { collectRunningApps() }
        repository.scope.launch { collectMinuteTick() }

    }

    private suspend fun collectRunningApps() {
        runningApplications.collect { currentApps ->
            if (currentApps.isNotEmpty()) {
                seenThisMinute.update { it + currentApps }
            }
        }
    }

    private suspend fun collectMinuteTick() {
        repository.minutes
            .filter { repository.todayScreenTime.value != null }
            .collect {
                repository.incrementScreenTime()
                val today = repository.dayInfo.value
                val activeApps = runningApplications.value.toSet()
                if (activeApps.isNotEmpty()) {
                    val apps = repository.appsMap.value
                    activeApps.forEach { runningApp -> updateOrAddApp(apps, runningApp, today) }
                }
                repository.saveCurrentState()
            }
    }
 /*   private suspend fun collectMinuteTick() {
        repository.minutes
            .filter { repository.todayScreenTime.value != null }
            .collect {
                repository.incrementScreenTime()

                val today = repository.dayInfo.value
                val activeApps = seenThisMinute.getAndUpdate { runningApplications.value.toSet() }

                if (activeApps.isNotEmpty()) {
                    val apps = repository.appsMap.value
                    activeApps.forEach { runningApp ->
                        updateOrAddApp(apps, runningApp, today)
                    }
                }

                repository.saveCurrentState()
            }
    }*/

    private fun updateOrAddApp(
        apps: Map<String, AppData>,
        runningApp: RunningApplication,
        today: DayInfo,
    ) {
        val existing = apps[runningApp.appId]
        if (existing == null) {
            repository.addApp(
                AppData(
                    appID = runningApp.appId,
                    appIconUrl = runningApp.iconPath,
                    appName = runningApp.appName,
                    days = listOf(ScreenTimeDay(today, 1)),
                )
            )
        } else {
            val todayIndex = existing.days.indexOfFirst { it.date == today }
            val updatedDays = if (todayIndex < 0) {
                existing.days + ScreenTimeDay(today, 1)
            } else {
                existing.days.toMutableList().apply {
                    set(todayIndex, ScreenTimeDay(today, existing.days[todayIndex].totalMinutes + 1))
                }
            }
            repository.updateAppDays(runningApp.appId, updatedDays)
        }
    }

}