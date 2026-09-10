package widgets.widgetsModule.widgets.appstime.ui.mediumView

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import widgets.Theme.Paddings
import widgets.widgetsModule.widgets.appstime.AppsTimeViewModel
import widgets.widgetsModule.widgets.appstime.ui.mediumView.appsTimes.AppsTimeTableView
import widgets.widgetsModule.widgets.appstime.ui.mediumView.screenTime.ScreenTimeGraphView
import widgets.widgetsModule.widgetsManager.BlurSurface
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.CapturedLayerState

@Composable
fun AppsTimeMediumView(appsTimeViewModel: AppsTimeViewModel, capturedLayerState: CapturedLayerState) {


        Row(
            Modifier.fillMaxSize()
                .padding(Paddings.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {


            BlurSurface(Modifier.weight(1f), capturedLayerState, appsTimeViewModel) {

                ScreenTimeGraphView(Modifier.fillMaxSize(), appsTimeViewModel)

            }

            Spacer(Modifier.width(Paddings.medium))

            BlurSurface(Modifier.weight(1f), capturedLayerState, appsTimeViewModel){
                AppsTimeTableView(Modifier.fillMaxSize()/*.weight(1f)*/, appsTimeViewModel)
            }
        }


}

