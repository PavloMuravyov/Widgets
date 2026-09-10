package widgets.widgetsModule.widgets.appstime.domain.repository

import widgets.widgetsModule.widgets.appstime.domain.model.RunningApplication
import kotlinx.coroutines.flow.StateFlow

interface GalaDbusListener {

    val runningApplication: StateFlow<List<RunningApplication>>


    suspend fun startListening()

    fun getRunningApplications()
}