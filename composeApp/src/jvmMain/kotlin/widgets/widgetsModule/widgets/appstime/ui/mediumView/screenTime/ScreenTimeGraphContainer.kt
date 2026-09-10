package widgets.widgetsModule.widgets.appstime.ui.mediumView.screenTime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import widgets.widgetsModule.widgets.appstime.AppsTimeViewModel

@Composable
fun ScreenTimeGraphContainer(
    appsTimeViewModel: AppsTimeViewModel,
    graphTimeSize: TextUnit,
    graphLabelSize: TextUnit,
    graphYAxisWidth: Dp) {

    val days by appsTimeViewModel.last7DaysScreenTime.collectAsState()
    val density = appsTimeViewModel.density.value
    ScreenTimeGraph(days,
        graphTimeSize,
        graphLabelSize,
        graphYAxisWidth,
        density,
        Modifier)

}