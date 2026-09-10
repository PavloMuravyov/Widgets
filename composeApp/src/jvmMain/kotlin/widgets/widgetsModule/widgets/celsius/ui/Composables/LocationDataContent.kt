package widgets.widgetsModule.widgets.celsius.ui.Composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import widgets.Theme.Colors
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel
import widgets.widgetsModule.widgets.celsius.ui.TextLabel

@Composable
fun LocationDataContent(
    modifier: Modifier,
    countryCodeSize: TextUnit,
    cityNameTextSize: TextUnit,
    celsiusViewModel: CelsiusViewModel,
) {

    val locationInfo by celsiusViewModel.locationInfo.collectAsState()

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        locationInfo?.let {

            TextLabel(
                countryCodeSize,
                it.countryCode,
                Colors.semiTransparentWhite
            )
            TextLabel(
                cityNameTextSize,
                it.city,
                Colors.solidWhite,
                maxLines = 2,
                textAlign = TextAlign.Start,
            )

        }
    }
}