package widgets.domain.dbus

import org.freedesktop.dbus.annotations.DBusInterfaceName
import org.freedesktop.dbus.interfaces.DBusInterface

@DBusInterfaceName("org.pantheon.gala.daemon")
interface GalaDaemon : DBusInterface {
    fun ShowDesktopMenu(
        monitorWidth: Int,
        monitorHeight: Int,
        x: Int,
        y: Int
    )
}


