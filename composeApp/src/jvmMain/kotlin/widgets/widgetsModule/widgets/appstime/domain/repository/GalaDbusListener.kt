package widgets.widgetsModule.widgets.appstime.domain.repository

import widgets.widgetsModule.widgets.appstime.domain.model.RunningApplication
import kotlinx.coroutines.flow.StateFlow

interface GalaDbusListener {

    val runningApplication: StateFlow<List<RunningApplication>>
    val focusedApplication: StateFlow<RunningApplication?>
    val secondsAccumulator: StateFlow<Map<String, Int>>


    suspend fun startListening()

    fun getRunningApplications()
    fun getFocusedApplication()
    fun consumeMinute(appId: String)
}