package widgets.domain.dbus

import org.freedesktop.dbus.annotations.DBusInterfaceName
import org.freedesktop.dbus.exceptions.DBusException
import org.freedesktop.dbus.interfaces.DBusInterface
import org.freedesktop.dbus.messages.DBusSignal

@DBusInterfaceName("org.freedesktop.login1.Manager")
interface Login1Manager : DBusInterface {

    class PrepareForSleep(
        path: String,
        val goingToSleep: Boolean
    ) : DBusSignal(path, goingToSleep)

    override fun getObjectPath(): String {
        return "/org/freedesktop/login1"
    }

    override fun isRemote(): Boolean {
        return false
    }
}