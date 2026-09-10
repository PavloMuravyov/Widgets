package widgets.widgetsModule.widgets.celsius.ui.Composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.Dp
import widgets.Theme.Colors
import widgets.widgetsModule.widgets.celsius.domain.model.WeatherIconMapper
import org.jetbrains.compose.resources.painterResource

@Composable
fun ConditionIcon(conditionCode: Int, iconSize: Dp) {
    val icon = remember(conditionCode) {
        WeatherIconMapper.getIcon(conditionCode, true)
    }
    Image(
        painter = painterResource(icon),
        contentDescription = null,
        modifier = Modifier.height(iconSize),
        colorFilter = ColorFilter.tint(Colors.solidWhite)
    )
}