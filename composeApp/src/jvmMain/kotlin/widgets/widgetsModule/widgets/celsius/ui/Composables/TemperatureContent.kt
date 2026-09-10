package widgets.widgetsModule.widgets.celsius.ui.Composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.degreeSymbol
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel
import widgets.widgetsModule.widgets.celsius.ui.NumericText

@Composable
fun TemperatureContent(textSize: TextUnit, celsiusViewModel: CelsiusViewModel, modifier: Modifier = Modifier,
                       alignment: Alignment = Alignment.Center) {
    val degreeSymbol = stringResource(Res.string.degreeSymbol)
    val temperature by celsiusViewModel.temperature.collectAsState()
    val density = celsiusViewModel.density.value
    Box(modifier
        .fillMaxSize(),
        contentAlignment = alignment) {
        temperature?.let {
            val label = remember(it.current) { "${it.current.toInt()}$degreeSymbol" }

             val size = if (label.length > 3) {
                textSize.value * 0.8f
            } else textSize.value

            NumericText(size.sp, label, density)
        }
    }
}