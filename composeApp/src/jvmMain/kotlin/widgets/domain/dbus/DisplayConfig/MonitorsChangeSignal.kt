package widgets.domain.dbus.DisplayConfig

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.interfaces.DBusSigHandler

class MonitorsChangeSignal (private val connection: DBusConnection) {
    private val _changedAt = MutableStateFlow<Long?>(null)
    val changedAt: StateFlow<Long?> = _changedAt.asStateFlow()


    fun start() {

        connection.addSigHandler(
            DisplayConfig.MonitorsChanged::class.java,
            DBusSigHandler<DisplayConfig.MonitorsChanged> {
                _changedAt.value = System.currentTimeMillis()
            }
        )
    }

    fun stop() {
        connection.disconnect()
    }
}