package widgets.domain.dbus.Location

import org.freedesktop.dbus.DBusPath
import org.freedesktop.dbus.annotations.DBusInterfaceName
import org.freedesktop.dbus.interfaces.DBusInterface


@DBusInterfaceName("org.freedesktop.GeoClue2.Manager")
interface Manager : DBusInterface {
    fun GetClient(): DBusPath
}