package widgets.domain.JNA.useCases

import widgets.domain.JNA.GSettingsMonitor
import widgets.domain.JNA.SystemContext
import com.sun.jna.*

/*enum class ColorScheme(val gsettingsValue: String) {
    DEFAULT("default"),
    PREFER_DARK("prefer-dark"),
    PREFER_LIGHT("prefer-light"),
    UNKNOWN("unknown");

    companion object {
        fun from(value: String?): ColorScheme =
            entries.find { it.gsettingsValue == value } ?: UNKNOWN
    }
}*/

class ColorSchemeMonitor(
    private val system: SystemContext

) : GSettingsMonitor<String>(
    system,
    schema = "org.gnome.desktop.interface",
    key = "color-scheme"
) {

    override fun readValue(settings: Pointer): String = readString(settings, "color-scheme")

}