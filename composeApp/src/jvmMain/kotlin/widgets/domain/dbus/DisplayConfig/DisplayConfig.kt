package widgets.domain.dbus.DisplayConfig

import org.freedesktop.dbus.annotations.DBusInterfaceName
import org.freedesktop.dbus.interfaces.DBusInterface
import org.freedesktop.dbus.messages.DBusSignal

@DBusInterfaceName("org.gnome.Mutter.DisplayConfig")
interface DisplayConfig : DBusInterface {
    class MonitorsChanged(path: String) : DBusSignal(path)
}