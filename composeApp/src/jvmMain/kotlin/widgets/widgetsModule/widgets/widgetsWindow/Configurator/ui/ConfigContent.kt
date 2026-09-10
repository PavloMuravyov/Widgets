package widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import widgets.Theme.Colors
import widgets.Theme.Paddings
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ConfigDimens
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.commonSettings.CommonSettings
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.extentions.visibilityAlpha
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.widgetCard.ConfigWidgetCard
import widgets.widgetsModule.widgetsManager.WidgetsManager


@Composable
fun ConfigContent(
    modifier: Modifier = Modifier,
    widgetsManager: WidgetsManager,
    configWidgetCardHeight: Dp,
    dimens: ConfigDimens
) {


    var dropDownMenuExpandedSelectWidget by remember { mutableStateOf(false) }
    val onDropDownMenuExpandedChanged = { expanded: Boolean -> dropDownMenuExpandedSelectWidget = expanded }


    Column(modifier,
        horizontalAlignment = Alignment.CenterHorizontally) {

        ConfigWidgetCard(configWidgetCardHeight, dimens, widgetsManager, dropDownMenuExpandedSelectWidget, onDropDownMenuExpandedChanged)

        Spacer(Modifier.height(Paddings.xtraSmall).fillMaxWidth(0.9f)
            .visibilityAlpha(!dropDownMenuExpandedSelectWidget)
            .background(Colors.transparentWhite)
        )

        CommonSettings(Modifier.height(configWidgetCardHeight).fillMaxWidth()
            .visibilityAlpha(!dropDownMenuExpandedSelectWidget)
            , dimens, widgetsManager)

    }
}
