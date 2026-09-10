package widgets.widgetsModule.widgets.celsius.ui.CelsiusLargeView

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import widgets.Theme.Paddings
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel
import widgets.widgetsModule.widgets.celsius.ui.CelsiusDimens
import widgets.widgetsModule.widgets.celsius.ui.CelsiusMediumView.HoursForecast
import widgets.widgetsModule.widgets.notes.NoteContentType
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.CapturedLayerState
import org.jetbrains.compose.resources.stringResource
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.daily
import widgets.composeapp.generated.resources.degreeSymbol
import widgets.composeapp.generated.resources.hourly
import java.time.format.DateTimeFormatter

enum class ForecastType { Hourly, Daily }


@Composable
fun ForecastType.label(): String = when (this) {
    ForecastType.Hourly -> stringResource(Res.string.hourly)
    ForecastType.Daily  -> stringResource(Res.string.daily)
}


@Composable
fun CelsiusLargeScope.ForecastContent(
    celsiusViewModel: CelsiusViewModel,
    modifier: Modifier,
    dimens: CelsiusDimens,
    capturedLayerState: CapturedLayerState
) {

    var forecastType by remember { mutableStateOf(ForecastType.Hourly) }
    val onForecastTypeChanged = { newType: ForecastType -> forecastType = newType }


    Column(
        modifier.fillMaxSize()
            .padding(Paddings.medium)
    ) {

        ControlPanel(
            Modifier.weight(1f),
            forecastType,
            dimens.baseInfoTextSizeLarge,
            capturedLayerState,
            celsiusViewModel,
            onForecastTypeChanged
        )


        Forecast(Modifier.weight(4f), forecastType, celsiusViewModel, dimens)

    }
}


@Composable
private fun CelsiusLargeScope.Forecast(
    modifier: Modifier,
    forecastType:ForecastType,
    celsiusViewModel: CelsiusViewModel,
    dimens: CelsiusDimens,
) {



    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val degreeSymbol = stringResource(Res.string.degreeSymbol)


    Box(modifier.fillMaxSize()) {

        AnimatedContent(
            targetState = forecastType,
            modifier = Modifier.fillMaxSize(),
            transitionSpec = {
                if (targetState == ForecastType.Hourly) {
                    slideInHorizontally { -it } + fadeIn() togetherWith
                            slideOutHorizontally { it } + fadeOut()
                } else {
                    slideInHorizontally { it } + fadeIn() togetherWith
                            slideOutHorizontally { -it } + fadeOut()

                }
            },
            label = "NoteContentAnimation"
        ) { contentType ->
            when (contentType) {
                ForecastType.Hourly -> HoursForecast(celsiusViewModel, dimens, formatter)
                ForecastType.Daily -> DailyForecast(celsiusViewModel, dimens, degreeSymbol)
            }
        }
    }
}



