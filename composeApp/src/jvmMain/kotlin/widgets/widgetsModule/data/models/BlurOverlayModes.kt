package widgets.widgetsModule.data.models

import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.widget_mode_always_title
import widgets.composeapp.generated.resources.widget_mode_auto_title
import widgets.composeapp.generated.resources.widget_mode_never_title

@Serializable
enum class BlurOverlayModes{
    Always,
    Auto,
    Never
}


val BlurOverlayModes.localizedLabels : StringResource
    get() = when (this) {
        BlurOverlayModes.Always -> Res.string.widget_mode_always_title
        BlurOverlayModes.Auto -> Res.string.widget_mode_auto_title
        BlurOverlayModes.Never -> Res.string.widget_mode_never_title
    }