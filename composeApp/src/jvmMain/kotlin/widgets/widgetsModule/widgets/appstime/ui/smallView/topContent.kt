package widgets.widgetsModule.widgets.appstime.ui.smallView

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import widgets.Theme.Colors
import widgets.widgetsModule.widgets.appstime.AppsTimeViewModel
import widgets.widgetsModule.widgets.appstime.domain.model.ScreenTimeDay
import widgets.widgetsModule.widgets.appstime.ui.AppsTimesDimens
import widgets.widgetsModule.widgets.appstime.ui.utils.composables.TimeText
import kotlinx.coroutines.flow.StateFlow

@Composable
fun topContent(modifier: Modifier, appsTimeViewModel: AppsTimeViewModel) {


    BoxWithConstraints(modifier = modifier
        .fillMaxWidth()
        .background(Colors.transparentBlack)
        .padding(4.dp),
        contentAlignment = Alignment.Center) {

        val dimens = remember(maxWidth, maxHeight) {
            AppsTimesDimens.calculate(maxWidth, maxHeight, appsTimeViewModel.widgetSize.value)
        }

        val todayScreenTime = appsTimeViewModel.todayScreenTime

        ScreenTimeText(screenTime = todayScreenTime, textSize = dimens.timeCounterSize)


    }

}


@Composable
private fun ScreenTimeText(screenTime: StateFlow<ScreenTimeDay?>, textSize: TextUnit) {

    val todayTime by screenTime.collectAsState()

    TimeText(todayTime, textSize = textSize)


}