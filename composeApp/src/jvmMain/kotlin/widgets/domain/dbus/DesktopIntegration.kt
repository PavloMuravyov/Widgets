package widgets.domain.dbus

import org.freedesktop.dbus.annotations.DBusInterfaceName
import org.freedesktop.dbus.interfaces.DBusInterface
import org.freedesktop.dbus.messages.DBusSignal

@DBusInterfaceName("org.pantheon.gala.DesktopIntegration")
interface DesktopIntegration : DBusInterface {
    class RunningApplicationsChanged(path: String) : DBusSignal(path)
    class WindowsChanged(path: String) : DBusSignal(path)

    fun GetRunningApplications(): List<Array<Any>>

}