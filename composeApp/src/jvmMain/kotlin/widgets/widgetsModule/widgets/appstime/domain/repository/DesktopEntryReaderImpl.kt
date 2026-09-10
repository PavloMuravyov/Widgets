package widgets.widgetsModule.widgets.appstime.domain.repository

import java.io.File
import java.util.concurrent.ConcurrentHashMap

class DesktopEntryReaderImpl (
    private val iconResolver: IconResolver

) : DesktopEntryReader {

    private val appDirs = listOf(
        "/usr/share/applications",
        "${System.getProperty("user.home")}/.local/share/applications",
        "/var/lib/flatpak/exports/share/applications",
        "${System.getProperty("user.home")}/.local/share/flatpak/exports/share/applications"
    )



    private val cache = ConcurrentHashMap<String, Pair<String, String>>()

    override fun read(appId: String): Pair<String, String> {

        return cache.getOrPut(appId) {

        val desktopFile = findDesktopFile(appId)
            ?: return Pair(appId, "")

        val lines = desktopFile.readLines()
        val name = lines.firstOrNull { it.startsWith("Name=") }
            ?.removePrefix("Name=") ?: appId
        val iconName = lines.firstOrNull { it.startsWith("Icon=") }
            ?.removePrefix("Icon=") ?: ""

        val iconPath = iconResolver.resolve(iconName)

         Pair(name, iconPath)
        }
    }

    override fun findDesktopFile(appId: String): File? {
        val fileName = if (appId.endsWith(".desktop")) appId else "$appId.desktop"
        return appDirs
            .map { File(it, fileName) }
            .firstOrNull { it.exists() }
    }
}