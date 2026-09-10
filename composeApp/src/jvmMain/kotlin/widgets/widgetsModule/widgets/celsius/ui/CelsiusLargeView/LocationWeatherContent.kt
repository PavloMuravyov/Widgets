package widgets.widgetsModule.widgets.celsius.ui.CelsiusLargeView

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import widgets.Theme.Paddings
import widgets.widgetsModule.data.models.roundedShape
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel
import widgets.widgetsModule.widgets.celsius.ui.CelsiusDimens
import widgets.widgetsModule.widgets.celsius.ui.Composables.TemperatureContent
import widgets.widgetsModule.widgetsManager.Utils.BlurSurface.blurFromLayer
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.CapturedLayerState

@Composable
fun CelsiusLargeScope.LocationWeatherContent(celsiusViewModel: CelsiusViewModel, modifier: Modifier = Modifier, dimens: CelsiusDimens, capturedLayerState: CapturedLayerState) {
        Column(
            modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            LocationContent(celsiusViewModel, Modifier.weight(1.5f), dimens, capturedLayerState)

            TemperatureContent(
                dimens.temperatureLabelTextSizeLarge,
                celsiusViewModel,
                Modifier.weight(3f),
                Alignment.BottomStart
            )
        }


}