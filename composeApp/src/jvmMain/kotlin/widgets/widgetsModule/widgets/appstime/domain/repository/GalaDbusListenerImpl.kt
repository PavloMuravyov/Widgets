package widgets.widgetsModule.widgets.appstime.domain.repository

import widgets.domain.dbus.DesktopIntegration
import widgets.domain.dbus.GalaDesktopIntegrationImpl
import widgets.domain.dbus.GalaDesktopIntegrationImpl.Companion.BUS_NAME
import widgets.widgetsModule.widgets.appstime.domain.model.RunningApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.freedesktop.dbus.connections.impl.DBusConnection


class GalaDbusListenerImpl (
    private val desktopEntryReader: DesktopEntryReader,
    private val connection: DBusConnection
) : GalaDbusListener {


    private val remoteObject = connection.getRemoteObject(
        BUS_NAME,
        GalaDesktopIntegrationImpl.OBJECT_PATH,
        DesktopIntegration::class.java
    )

    private val _runningApplication = MutableStateFlow<List<RunningApplication>>(emptyList())

    override val runningApplication: StateFlow<List<RunningApplication>> = _runningApplication


    override suspend fun startListening() {
        getRunningApplications()

        connection.addSigHandler(
            DesktopIntegration.RunningApplicationsChanged::class.java,
            remoteObject
        ) { _ ->
            getRunningApplications()
        }
        awaitCancellation()
    }

    override fun getRunningApplications() {

        val current = _runningApplication.value.associateBy { it.appId }
        val raw = try {
            remoteObject.GetRunningApplications()
        } catch (e: Exception) {

            return
        }
        _runningApplication.value = raw
            .map { it[0] as String }
            .filter { it.endsWith(".desktop") && !it.contains("wingpanel")}
            .mapNotNull { appId ->
                current[appId] ?: run {
                    val appData = try {
                        desktopEntryReader.read(appId)
                    } catch (e: Exception) {
                        Pair(appId, "")
                    }
                    if (appData.first.isNotBlank() && appData.second.isNotBlank()) {
                        RunningApplication(appId = appId, appName = appData.first, iconPath = appData.second)
                    } else null
                }
            }

    }


}