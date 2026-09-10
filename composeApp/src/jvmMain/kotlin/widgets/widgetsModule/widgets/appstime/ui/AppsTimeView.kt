package widgets.widgetsModule.widgets.appstime.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import widgets.widgetsModule.data.models.WidgetSizes
import widgets.widgetsModule.widgets.appstime.AppsTimeViewModel
import widgets.widgetsModule.widgets.appstime.ui.mediumView.AppsTimeMediumView
import widgets.widgetsModule.widgets.appstime.ui.smallView.appsTimeSmallView
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.CapturedLayerState
import io.github.fletchmckee.liquid.rememberLiquidState

@Composable
fun AppsTimeView(appsTimeViewModel: AppsTimeViewModel, capturedLayerState: CapturedLayerState) {

    val widgetSize by appsTimeViewModel.widgetSize.collectAsState()
    val localLiquidState = rememberLiquidState()

    when (widgetSize) {


        WidgetSizes.Small -> {
            appsTimeSmallView(appsTimeViewModel, localLiquidState, capturedLayerState)
        }

        WidgetSizes.Medium -> {
            AppsTimeMediumView(appsTimeViewModel, capturedLayerState)
        }

        WidgetSizes.Large -> {

        }
    }
}




@Composable
fun AppsTimeLargeView(){

}
