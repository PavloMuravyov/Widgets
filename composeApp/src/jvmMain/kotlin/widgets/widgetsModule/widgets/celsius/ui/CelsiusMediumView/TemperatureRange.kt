package widgets.widgetsModule.widgets.celsius.ui.CelsiusMediumView

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import widgets.Theme.Colors
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel
import widgets.widgetsModule.widgets.celsius.ui.CelsiusDimens


@Composable
fun CelsiusMediumScope.TemperatureRange (
    celsiusViewModel: CelsiusViewModel,
    dimens: CelsiusDimens,
    modifier: Modifier,
    maxTemp: String,
    minTemp: String,
    degreeSymbol: String
){

    val forecastToday by celsiusViewModel.forecastToday.collectAsState()

    Box(modifier
        .fillMaxSize(), contentAlignment = Alignment.CenterStart) {
        forecastToday?.let {

         val text = remember(forecastToday?.maxTemp, forecastToday?.minTemp)  {
             buildAnnotatedString{
                withStyle(SpanStyle(color = Colors.transparentWhite)) { append("$maxTemp ") }
                withStyle(SpanStyle(color = Colors.solidWhite)) { append("${it.maxTemp.toInt()}$degreeSymbol") }
                append("  ")
                withStyle(SpanStyle(color = Colors.transparentWhite)) { append("$minTemp ") }
                withStyle(SpanStyle(color = Colors.solidWhite)) { append("${it.minTemp.toInt()}$degreeSymbol") }
            }
         }
            Text(text, fontSize = dimens.baseInfoTextSizeMedium,)
        }
    }
}
