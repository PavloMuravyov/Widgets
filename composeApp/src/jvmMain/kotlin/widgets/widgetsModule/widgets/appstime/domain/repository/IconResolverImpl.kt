package widgets.widgetsModule.widgets.appstime.domain.repository

import widgets.domain.JNA.GtkLibrary
import com.sun.jna.Pointer
import java.util.concurrent.ConcurrentHashMap
import kotlin.text.ifEmpty

class IconResolverImpl (
    private val gtk: GtkLibrary,
    private val iconTheme: Pointer
): IconResolver {


    private  val ICON_SIZE = 64
    private  val GTK_ICON_LOOKUP_USE_BUILTIN = 0

    private val cache = ConcurrentHashMap<String, String>()

    override fun resolve(iconName: String): String {
        if (iconName.isBlank()) return ""
        if (iconName.startsWith("/")) return iconName

        return cache.getOrPut(iconName){ lookupViaGtk(iconName)
            ?: lookupInPixmaps(iconName)
            ?: ""
        }
    }



    private fun lookupViaGtk(iconName: String): String? {
        val iconInfo = gtk.gtk_icon_theme_lookup_icon(
            iconTheme,
            iconName,
            ICON_SIZE,
            GTK_ICON_LOOKUP_USE_BUILTIN
        ) ?: return null

        val path = gtk.gtk_icon_info_get_filename(iconInfo)
        gtk.g_object_unref(iconInfo)
        return path?.ifEmpty { null }
    }

    private fun lookupInPixmaps(iconName: String): String? {
        val extensions = listOf("png", "svg", "xpm")
        return extensions
            .map { java.io.File("/usr/share/pixmaps/$iconName.$it") }
            .firstOrNull { it.exists() }
            ?.absolutePath
    }
}