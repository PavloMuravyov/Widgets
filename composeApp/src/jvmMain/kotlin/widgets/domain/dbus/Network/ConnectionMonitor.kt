package widgets.domain.dbus.Network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.connections.impl.DBusConnectionBuilder
import org.freedesktop.dbus.interfaces.DBusSigHandler
import org.freedesktop.dbus.interfaces.Properties
import org.freedesktop.dbus.types.UInt32
import java.io.Closeable
import kotlin.time.Duration.Companion.milliseconds


class InternetConnectivityMonitor(
    private val connection: DBusConnection,
)  {

    private val NM_SERVICE = "org.freedesktop.NetworkManager"
    private val NM_PATH    = "/org/freedesktop/NetworkManager"

    fun connectivityFlow(): Flow<Boolean> {
        val props = connection.getRemoteObject(
            NM_SERVICE, NM_PATH, Properties::class.java
        )
        val reader  = ConnectivityReader(props)
        val signals = ConnectivitySignalSource(connection, reader)

        return signals.signals()
            .onStart { emit(reader.read() ?: false) }
            .distinctUntilChanged()
    }

}

class ConnectivityReader(
    private val props: Properties,
) {
    private val NM_INTERFACE = "org.freedesktop.NetworkManager"

    fun read(): Boolean? = try {
        when (props.Get<UInt32>(NM_INTERFACE, "Connectivity").toInt()) {
            4    -> true   // NM_CONNECTIVITY_FULL
            3    -> null   // NM_CONNECTIVITY_CHECKING — transitional
            else -> false  // NONE, PORTAL, UNKNOWN
        }
    } catch (e: Exception) {
        println("Error reading connectivity: ${e.message}")
        false
    }
}


class ConnectivitySignalSource(
    private val connection: DBusConnection,
    private val reader: ConnectivityReader,
) {
    private val NM_PATH = "/org/freedesktop/NetworkManager"

    fun signals(): Flow<Boolean> = callbackFlow {
        val handler = DBusSigHandler<Properties.PropertiesChanged> { signal ->
            if (signal.path == NM_PATH) {
                reader.read()?.let { trySend(it) }
            }
        }
        connection.addSigHandler(Properties.PropertiesChanged::class.java, handler)
        awaitClose {
            connection.removeSigHandler(Properties.PropertiesChanged::class.java, handler)
        }
    }
}

