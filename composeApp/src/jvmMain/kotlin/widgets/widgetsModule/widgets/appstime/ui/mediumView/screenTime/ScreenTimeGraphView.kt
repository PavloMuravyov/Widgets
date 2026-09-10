package widgets.widgetsModule.widgets.appstime.ui.mediumView.screenTime

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import widgets.widgetsModule.widgets.appstime.AppsTimeViewModel
import widgets.widgetsModule.widgets.appstime.ui.AppsTimesDimens
import widgets.widgetsModule.widgets.appstime.ui.mediumView.TodayTimeLabel


@Composable
fun ScreenTimeGraphView(modifier: Modifier, appsTimeViewModel: AppsTimeViewModel) {


    BoxWithConstraints(modifier.padding(
        top = 4.dp,
        start = 4.dp,
        end = 4.dp),
        contentAlignment = Alignment.BottomCenter) {

        val dimens = remember(maxWidth, maxHeight) {
            AppsTimesDimens.calculate(maxWidth, maxHeight, appsTimeViewModel.widgetSize.value)
        }

        ScreenTimeGraphContainer(
            appsTimeViewModel,
            dimens.graphTimeSize,
            dimens.graphLabelSize,
            dimens.graphYAxisWidth
        )

        TodayTimeLabel(
            appsTimeViewModel,
            dimens.topTimeLabelTextBig,
            dimens.topTimeLabelTextSmall
        )
    }



}