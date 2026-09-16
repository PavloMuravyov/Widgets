package widgets.widgetsModule.widgets.appstime.domain

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import widgets.domain.TimeService.DayInfo
import widgets.widgetsModule.widgets.appstime.domain.model.AppData
import widgets.widgetsModule.widgets.appstime.domain.model.RunningApplication
import widgets.widgetsModule.widgets.appstime.domain.model.ScreenTimeDay
import widgets.widgetsModule.widgets.appstime.domain.repository.GalaDbusListener


class AppsTimeService(
    private val repository: TimesRepository,
    private val galaDbusListener: GalaDbusListener,
) {
    val runningApplications = galaDbusListener.runningApplication
    val focusedApplication = galaDbusListener.focusedApplication


    init {
        repository.scope.launch { galaDbusListener.startListening() }
        repository.scope.launch { collectMinuteTick() }
        repository.scope.launch { collectSecondsAccumulator() }
    }


    private suspend fun collectMinuteTick() {
        repository.minutes
            .filter { repository.todayScreenTime.value != null }
            .collect {
                repository.incrementScreenTime()
            }
    }

    private suspend fun collectSecondsAccumulator() {
        galaDbusListener.secondsAccumulator.collect { accumulator ->
            if (repository.todayScreenTime.value == null) return@collect

            val readyAppIds = accumulator.filterValues { it >= 60 }.keys
            if (readyAppIds.isEmpty()) return@collect

            val today = repository.dayInfo.value
            var apps = repository.appsMap.value
            var changed = false

            readyAppIds.forEach { appId ->
                val app = resolveApp(appId) ?: return@forEach
                updateOrAddApp(apps, app, today)
                galaDbusListener.consumeMinute(appId)
                apps = repository.appsMap.value
                changed = true
            }

            if (changed) {
                repository.saveCurrentState()
            }
        }
    }



    private fun resolveApp(appId: String): RunningApplication? =
        focusedApplication.value?.takeIf { it.appId == appId }
            ?: runningApplications.value.firstOrNull { it.appId == appId }

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

