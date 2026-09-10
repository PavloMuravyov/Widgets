package widgets.domain.dbus

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.exceptions.DBusException
import org.freedesktop.dbus.interfaces.DBusSigHandler
import kotlin.jvm.java


class SleepWakeListener(private val dbusconnection: DBusConnection) {
    val sleepState: Flow<Boolean> = callbackFlow {
        val handler = DBusSigHandler<Login1Manager.PrepareForSleep> { signal ->
            trySend(!signal.goingToSleep)
        }
        try {
            dbusconnection.addSigHandler(Login1Manager.PrepareForSleep::class.java, handler)
        } catch (e: DBusException) {
            close(e)
            return@callbackFlow
        }

        awaitClose {
            try {
                dbusconnection.removeSigHandler(Login1Manager.PrepareForSleep::class.java, handler)
            } catch (e: DBusException) {
                // логування
            }
        }
    }
}