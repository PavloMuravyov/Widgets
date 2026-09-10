package widgets.domain.JNA.useCases

// WallpaperMonitor.kt
/**
 * Стежить за URI шпалер робочого столу GNOME.
 * Значення: "file:///home/user/photo.jpg" тощо.
 */

import widgets.domain.JNA.GSettingsMonitor
import widgets.domain.JNA.SystemContext
import com.sun.jna.*
import java.net.URI
import java.nio.file.Paths

class WallpaperMonitor(
    private val system: SystemContext

) : GSettingsMonitor<String>(
    system,
    schema = "org.gnome.desktop.background",
    key = "picture-uri"
) {

    override fun readValue(settings: Pointer): String =
        Paths.get(URI(readString(settings, "picture-uri"))).toString()
}


class WallpaperMonitorMode(
    private val system: SystemContext

) : GSettingsMonitor<String>(
    system,
    schema = "org.gnome.desktop.background",
    key = "picture-options"
) {

    override fun readValue(settings: Pointer): String = readString(settings, "picture-options")

}

enum class PictureOption(val gsettingsValue: String) {
    ZOOM("zoom"),        // збільшити — заповнити з обрізкою країв
    SCALED("scaled"),    // заповнити — вписати повністю, можливі смуги
    CENTERED("centered") ,// центрувати — оригінальний розмір по центру

    OTHER("other")


}