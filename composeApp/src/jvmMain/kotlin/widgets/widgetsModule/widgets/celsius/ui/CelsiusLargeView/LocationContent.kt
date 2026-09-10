package widgets.widgetsModule.widgets.celsius.ui.CelsiusLargeView

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.House
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import widgets.Theme.Colors
import widgets.Theme.Paddings
import widgets.widgetsModule.data.models.roundedShape
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel
import widgets.widgetsModule.widgets.celsius.ui.CelsiusDimens
import widgets.widgetsModule.widgets.celsius.ui.TextLabelWithTooltip
import widgets.widgetsModule.widgetsManager.Utils.BlurSurface.blurFromLayer
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.CapturedLayerState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun CelsiusLargeScope.LocationContent(
    celsiusViewModel: CelsiusViewModel, modifier: Modifier = Modifier, dimens: CelsiusDimens,
    capturedLayerState: CapturedLayerState
) {


    Box(modifier) {

        Box(Modifier.fillMaxSize()
            .clip(RoundedCornerShape(celsiusViewModel.widgetSize.value.roundedShape - Paddings.medium * 2))
            .blurFromLayer(capturedLayerState, true)) {

        }

        Row(
            Modifier
                .fillMaxSize()
                .padding(Paddings.xtraSmall),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Paddings.small)
        ) {

            val location by celsiusViewModel.locationInfo.collectAsState()

            location?.let {
                Icon(Icons.Default.House, contentDescription = null, tint = Colors.solidWhite)

                TextLabelWithTooltip(
                    dimens.baseInfoTextSizeLarge,
                    "${it.city}, ${it.country}",
                    dimens.baseInfoTextSizeLarge,
                    "${it.city}, ${it.countryCode}",
                    Colors.solidWhite,
                )
            }
        }
    }

}
