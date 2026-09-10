package widgets.widgetsModule.widgets.celsius.ui.CelsiusMediumView

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import widgets.Theme.Colors
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel
import widgets.widgetsModule.widgets.celsius.ui.TextLabel
import widgets.widgetsModule.widgets.celsius.ui.CelsiusDimens
import widgets.widgetsModule.widgets.celsius.ui.Composables.WindArrow
import org.jetbrains.compose.resources.stringResource
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.wind_speed_unit


@Composable
fun CelsiusMediumScope.WindContent(celsiusViewModel: CelsiusViewModel, dimens: CelsiusDimens, modifier: Modifier) {


    val windSpeedUnit = stringResource(Res.string.wind_speed_unit)

    Box(
        modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        val windData by celsiusViewModel.windData.collectAsState()
        windData?.let { data ->

            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {

               TextLabel(
                    dimens.baseInfoTextSizeMedium,
                    data.windSpeed.toString()
                            + " "
                            + windSpeedUnit,
                    color = Colors.solidWhite
                )


                WindArrow(data)
            }
        }

    }

}