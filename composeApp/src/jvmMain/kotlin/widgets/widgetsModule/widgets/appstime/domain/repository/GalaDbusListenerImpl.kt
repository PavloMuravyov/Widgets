package widgets.widgetsModule.widgets.appstime.domain.repository

import widgets.domain.dbus.DesktopIntegration
import widgets.domain.dbus.GalaDesktopIntegrationImpl
import widgets.domain.dbus.GalaDesktopIntegrationImpl.Companion.BUS_NAME
import widgets.widgetsModule.widgets.appstime.domain.model.RunningApplication
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.freedesktop.dbus.connections.impl.DBusConnection
import kotlin.collections.emptyMap
import kotlin.time.Duration.Companion.milliseconds

class GalaDbusListenerImpl(
    private val desktopEntryReader: DesktopEntryReader,
    private val connection: DBusConnection
) : GalaDbusListener {

    companion object {
        private const val FOCUS_POLL_INTERVAL_MS = 1000L
    }

    private val remoteObject = connection.getRemoteObject(
        BUS_NAME,
        GalaDesktopIntegrationImpl.OBJECT_PATH,
        DesktopIntegration::class.java
    )

    private val _runningApplication = MutableStateFlow<List<RunningApplication>>(emptyList())
    override val runningApplication: StateFlow<List<RunningApplication>> = _runningApplication

    private val _focusedApplication = MutableStateFlow<RunningApplication?>(null)
    override val focusedApplication: StateFlow<RunningApplication?> = _focusedApplication


    override suspend fun startListening() = coroutineScope {
        getRunningApplications()
        getFocusedApplication()

        connection.addSigHandler(
            DesktopIntegration.RunningApplicationsChanged::class.java,
            remoteObject
        ) { _ ->
            getRunningApplications()
        }

        launch { pollFocusedApplication() }

        awaitCancellation()
    }

    private val _secondsAccumulator = MutableStateFlow<Map<String, Int>>(emptyMap())
    override val secondsAccumulator: StateFlow<Map<String, Int>> = _secondsAccumulator

    override fun consumeMinute(appId: String) {
        _secondsAccumulator.update { current ->
            val remaining = (current[appId] ?: 0) - 60
            if (remaining > 0) current + (appId to remaining) else current - appId
        }
    }

    private suspend fun pollFocusedApplication() {
        while (true) {
            try {
                getFocusedApplication()

                val focusedApp = focusedApplication.value
                if (focusedApp != null) {
                    _secondsAccumulator.update { current ->
                        val seconds = (current[focusedApp.appId] ?: 0) + 1
                        current + (focusedApp.appId to seconds)
                    }
                }
            } catch (e: Exception) {
            }
            delay(FOCUS_POLL_INTERVAL_MS.milliseconds)
        }
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
            .filter { it.endsWith(".desktop") && !it.contains("wingpanel") }
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

    override fun getFocusedApplication() {

        val raw = try {
            remoteObject.GetWindows()
        } catch (e: Exception) {
            return
        }

        val focusedAppId = try {
            raw.firstNotNullOfOrNull { window ->
                @Suppress("UNCHECKED_CAST")
                val props = window.getOrNull(1) as? Map<String, Any?> ?: return@firstNotNullOfOrNull null
                val hasFocus = props["has-focus"] as? Boolean ?: false
                if (!hasFocus) return@firstNotNullOfOrNull null
                props["app-id"] as? String
            }
        } catch (e: Exception) {
            null
        }

        if (focusedAppId == null || !focusedAppId.endsWith(".desktop") || focusedAppId.contains("wingpanel")) {
            _focusedApplication.value = null
            return
        }

        val cached = _runningApplication.value.firstOrNull { it.appId == focusedAppId }
        if (cached != null) {
            _focusedApplication.value = cached
            return
        }

        val appData = try {
            desktopEntryReader.read(focusedAppId)
        } catch (e: Exception) {
            Pair(focusedAppId, "")
        }

        _focusedApplication.value = if (appData.first.isNotBlank() && appData.second.isNotBlank()) {
            RunningApplication(appId = focusedAppId, appName = appData.first, iconPath = appData.second)
        } else null
    }

}