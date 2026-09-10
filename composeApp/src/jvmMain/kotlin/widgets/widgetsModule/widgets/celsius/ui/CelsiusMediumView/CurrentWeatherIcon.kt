package widgets.widgetsModule.widgets.celsius.ui.CelsiusMediumView

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel
import widgets.widgetsModule.widgets.celsius.ui.Composables.ConditionIcon

@Composable
fun CelsiusMediumScope.CurrentWeatherIcon(celsiusViewModel: CelsiusViewModel, dimens: widgets.widgetsModule.widgets.celsius.ui.CelsiusDimens, modifier: Modifier = Modifier) {

    val condition by celsiusViewModel.currentCondition.collectAsState()
    Box(modifier
        .fillMaxHeight()
        , contentAlignment = Alignment.Center) {
        condition?.let {

            ConditionIcon(it.code, dimens.conditionIconSizeMedium)
        }
    }
}
