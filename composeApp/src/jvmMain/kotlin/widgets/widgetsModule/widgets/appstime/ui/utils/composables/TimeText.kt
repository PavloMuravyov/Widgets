package widgets.widgetsModule.widgets.appstime.ui.utils.composables

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import widgets.Theme.Colors
import widgets.widgetsModule.widgets.appstime.domain.model.ScreenTimeDay
import widgets.widgetsModule.widgets.appstime.ui.utils.utils.toFormattedTime

@Composable
fun TimeText(todayTime: ScreenTimeDay?, textSize: TextUnit){
    Text(
        (todayTime?.totalMinutes ?: 0).toFormattedTime(),
        color = Colors.semiTransparentWhite,
        fontSize = textSize,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,)
}