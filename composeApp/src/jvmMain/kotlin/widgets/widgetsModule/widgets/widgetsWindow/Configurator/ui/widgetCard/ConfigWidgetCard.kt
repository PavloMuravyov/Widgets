package widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.widgetCard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import widgets.Theme.Paddings
import widgets.widgetsModule.data.models.WidgetSizes
import widgets.widgetsModule.data.models.WidgetsTypes
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ConfigDimens
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.extentions.visibilityAlpha
import widgets.widgetsModule.widgetsManager.WidgetsManager


@Composable
fun ConfigWidgetCard(
    configWidgetCardHeight: Dp,
    dimens: ConfigDimens,
    widgetsManager: WidgetsManager,
    dropDownMenuExpanded: Boolean,
    onDropDownMenuExpandedChanged: (Boolean) -> Unit
) {


    val selectedWidgetType by widgetsManager.selectedWidgetType.collectAsState()

    val sizes by widgetsManager.widgetSizes.collectAsState()

    val size = sizes[selectedWidgetType] ?: WidgetSizes.Medium



    Column(
        Modifier
            .height(configWidgetCardHeight)
            .fillMaxWidth()
            .padding(horizontal = Paddings.large, vertical = Paddings.large),
    ) {


        WidgetTypeSelector(
            Modifier.weight(1f),
            dimens,
            selectedWidgetType,
            dropDownMenuExpanded,
            onDropDownMenuExpandedChanged
        ) { widgetType ->
            widgetsManager.updateSelectedWidgetType(widgetType)
        }



        WidgetSizeSelector(
            Modifier.weight(2f).visibilityAlpha(!dropDownMenuExpanded),
            dimens,
            selectedWidgetType,
            size,
        ) { size ->
            widgetsManager.updateWidgetSize(selectedWidgetType, size)
        }


        EnabledSwitcher(
            Modifier.weight(1f).visibilityAlpha(!dropDownMenuExpanded),
            dimens,
            selectedWidgetType,
            widgetsManager
        )

    }
}

