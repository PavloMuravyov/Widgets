package widgets.domain.dbus

import widgets.widgetsModule.managers.SystemDataManager.model.MonitorArea
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.exceptions.DBusException
import java.awt.GraphicsEnvironment
import java.awt.MouseInfo
import java.awt.Point
import java.awt.Rectangle
import kotlin.math.roundToInt

class ShowDesktopMenuService(
    private val connection: DBusConnection
) {
    private val GALA_SERVICE = "org.pantheon.gala.daemon"
    private val GALA_PATH = "/org/pantheon/gala/daemon"

    suspend fun invoke(mousePoint: Point) {
        withContext(Dispatchers.IO) {
            try {
                val gala = connection.getRemoteObject(
                    GALA_SERVICE,
                    GALA_PATH,
                    GalaDaemon::class.java
                )


                val virtualBounds = getVirtualDesktopBounds()

               gala.ShowDesktopMenu(
                    virtualBounds.width,
                    virtualBounds.height,
                    mousePoint.x,
                    mousePoint.y
                )
            } catch (e: DBusException) {
                println("DBus error: ${e.message}")
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }
    }
}


private fun getVirtualDesktopBounds(): Rectangle {
    val devices = GraphicsEnvironment.getLocalGraphicsEnvironment().screenDevices
    var bounds: Rectangle? = null
    for (device in devices) {
        val screenBounds = device.defaultConfiguration.bounds
        bounds = bounds?.union(screenBounds) ?: screenBounds
    }
    return bounds ?: Rectangle(0, 0, 0, 0)
}


