package widgets.domain.AppSettings

import widgets.widgetsModule.data.models.BackgroundTypes
import widgets.widgetsModule.data.models.BlurOverlayModes
import widgets.widgetsModule.data.models.Widget
import widgets.widgetsModule.data.models.WidgetsTypes
import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val backgroundType: BackgroundTypes? = null,
    val enabledAutostart: Boolean = false,
    val blurOverlayMode: BlurOverlayModes? = null,
    val enabledWidgets: List<WidgetsTypes> = emptyList(),
    val widgets: List<Widget> = emptyList(),

)


