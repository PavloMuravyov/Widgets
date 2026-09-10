package widgets.widgetsModule.widgetsManager

import androidx.compose.runtime.*
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import widgets.widgetsModule.data.models.WidgetsTypes
import widgets.widgetsModule.managers.SystemDataManager.model.MonitorArea
import widgets.widgetsModule.widgets.appstime.AppsTimeViewModel
import widgets.widgetsModule.widgets.appstime.ui.AppsTimeView
import widgets.widgetsModule.widgets.celsius.CelsiusView
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel
import widgets.widgetsModule.widgets.clock.ClockView
import widgets.widgetsModule.widgets.clock.ClockViewModel
import widgets.widgetsModule.widgets.notes.NotesView
import widgets.widgetsModule.widgets.notes.NotesViewModel
import io.github.fletchmckee.liquid.LiquidState
import widgets.widgetsModule.data.models.WidgetSizes
import widgets.widgetsModule.data.models.dpSize
import widgets.widgetsModule.widgetsExtensions.ZEROMQ.Msg
import widgets.widgetsModule.widgetsExtensions.ZEROMQ.MsgType
import widgets.widgetsModule.widgetsExtensions.ZEROMQ.ZmqRouterService
import widgets.widgetsModule.widgetsExtensions.extensionVIew
import widgets.widgetsModule.widgetsExtensions.extensionWidgetContainer


@Composable
fun widgetsView(


    widgetsManager: WidgetsManager,
    liquidState: LiquidState,
    density: Density,
    availableMonitorArea: MonitorArea

){

    val baseWidgetScaledDimensions by widgetsManager.widgetBaseSizes.collectAsState()
    val enabledWidgets by widgetsManager.enabledViewModels.collectAsState()
    val backgroundType by widgetsManager.backgroundType.collectAsState()

    enabledWidgets.forEach { viewModel ->

        val type = viewModel.key

        key(type) {
            val viewModel = remember(type) {
                widgetsManager.getViewModel(type)
            }

            viewModel?.let {  viewModel ->
            baseWidgetScaledDimensions?.let { baseWidgetScaledDimensions ->
                widgetContainer(
                    widgetsManager,
                    viewModel,
                    liquidState,
                    baseWidgetScaledDimensions,
                    backgroundType,
                    density,
                    availableMonitorArea,
                    ) { capturedLayerState ->
                    when (type) {
                        WidgetsTypes.Celsius -> CelsiusView(viewModel as CelsiusViewModel, capturedLayerState)
                        WidgetsTypes.Clock -> ClockView(viewModel as ClockViewModel, capturedLayerState)
                        WidgetsTypes.Notes -> NotesView(viewModel as NotesViewModel, capturedLayerState)
                        WidgetsTypes.AppTimes -> AppsTimeView(viewModel as AppsTimeViewModel, capturedLayerState)

                    }
                }




            }



            }
    }
    }

    baseWidgetScaledDimensions?.let {
        extensionVIew(it, liquidState)

    }

}


