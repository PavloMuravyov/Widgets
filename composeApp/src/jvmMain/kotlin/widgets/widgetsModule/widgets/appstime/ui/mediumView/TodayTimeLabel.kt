package widgets.widgetsModule.widgets.appstime.ui.mediumView

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import widgets.Theme.Colors
import widgets.widgetsModule.widgets.appstime.AppsTimeViewModel
import widgets.widgetsModule.widgets.appstime.ui.utils.utils.toFormattedTime
import org.jetbrains.compose.resources.stringResource
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.today

@Composable
fun TodayTimeLabel(appsTimeViewModel: AppsTimeViewModel, topTimeLabelTextBig: TextUnit, topTimeLabelTextSmall: TextUnit) {

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopEnd
    ) {

        val todayLabel = stringResource(Res.string.today)
        Column(
            Modifier.fillMaxWidth(0.8f)
                .fillMaxHeight(0.25f)
                .padding(end = 4.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            val todayTime by appsTimeViewModel.todayScreenTime.collectAsState()

            Text(
                todayLabel,
                color = Colors.semiTransparentWhite,
                fontSize = topTimeLabelTextSmall
            )
            Text(
                todayTime?.totalMinutes?.toFormattedTime() ?: "0",
                color = Colors.semiTransparentWhite,
                fontSize = topTimeLabelTextBig
            )
        }
    }
}