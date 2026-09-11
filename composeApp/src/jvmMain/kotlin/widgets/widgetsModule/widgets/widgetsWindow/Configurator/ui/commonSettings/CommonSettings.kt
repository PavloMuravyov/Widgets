package widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.commonSettings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.AwtWindow
import org.jetbrains.compose.resources.stringResource
import widgets.Theme.Colors
import widgets.Theme.Paddings
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.add_widgets_title
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ConfigDimens
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.extentions.visibilityAlpha
import widgets.widgetsModule.widgetsManager.WidgetsManager
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
fun CommonSettings(modifier: Modifier, dimens: ConfigDimens, widgetsManager: WidgetsManager) {

    var dropDownMenuBackgroundExpanded by remember { mutableStateOf(false) }
    val onDropDownMenuBackgroundExpandedChanged = { expanded: Boolean -> dropDownMenuBackgroundExpanded = expanded }

    var dropDownMenuOverlayExpanded by remember { mutableStateOf(false) }
    val onDropDownMenuOverlayExpandedChanged = { expanded: Boolean -> dropDownMenuOverlayExpanded = expanded }


    Column(modifier
        .padding(horizontal = Paddings.large, vertical = Paddings.large),
    ){


        BackgroundSettings(Modifier.weight(1.25f), dimens, widgetsManager,
            dropDownMenuBackgroundExpanded,
            onDropDownMenuBackgroundExpandedChanged )

        BlurOverlaySettings(Modifier
            .weight(1.25f)
            .visibilityAlpha(!dropDownMenuBackgroundExpanded), dimens, widgetsManager,
            dropDownMenuOverlayExpanded,
            onDropDownMenuOverlayExpandedChanged)



        AutostartSettings (Modifier
            .weight(1f)
            .visibilityAlpha(!dropDownMenuOverlayExpanded)
            ,dimens
            ,widgetsManager)



    }
}
