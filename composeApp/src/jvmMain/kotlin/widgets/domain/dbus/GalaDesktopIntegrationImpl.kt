package widgets.domain.dbus
import widgets.domain.JNA.GtkLibrary
import widgets.widgetsModule.widgets.appstime.domain.model.RunningApplication
import com.sun.jna.Pointer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.freedesktop.dbus.connections.impl.DBusConnection
import java.io.File



class GalaDesktopIntegrationImpl : DesktopIntegration {

    companion object {
        const val BUS_NAME = "org.pantheon.gala"
        const val OBJECT_PATH = "/org/pantheon/gala/DesktopInterface"
    }
    override fun getObjectPath(): String = OBJECT_PATH
    override fun isRemote(): Boolean = false
    override fun GetRunningApplications(): List<Array<Any>> {
        throw UnsupportedOperationException()
    }

}
