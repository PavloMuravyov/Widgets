package widgets.domain.dbus.Location

import org.freedesktop.dbus.DBusPath
import org.freedesktop.dbus.annotations.DBusInterfaceName
import org.freedesktop.dbus.interfaces.DBusInterface
import org.freedesktop.dbus.messages.DBusSignal
import org.freedesktop.dbus.types.Variant

@DBusInterfaceName("org.freedesktop.GeoClue2.Client")
interface Client : DBusInterface {
    fun Start()

    fun Stop()

}