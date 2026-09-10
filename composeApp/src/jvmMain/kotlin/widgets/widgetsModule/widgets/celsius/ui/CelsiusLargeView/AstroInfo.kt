package widgets.widgetsModule.widgets.celsius.ui.CelsiusLargeView

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import widgets.Theme.Colors
import widgets.Theme.Paddings
import widgets.widgetsModule.widgets.appstime.ui.utils.utils.toFormattedTime
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel
import widgets.widgetsModule.widgets.celsius.ui.CelsiusDimens
import widgets.widgetsModule.widgets.celsius.ui.NumericText
import widgets.widgetsModule.widgets.celsius.ui.TextLabel
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.day_length
import widgets.composeapp.generated.resources.night_length
import widgets.composeapp.generated.resources.sunrise
import widgets.composeapp.generated.resources.sunset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@Composable
fun CelsiusLargeScope.AstroInfo(
    celsiusViewModel: CelsiusViewModel,
    modifier: Modifier,
    dimens: CelsiusDimens,
) {
    val astroData by celsiusViewModel.astroData.collectAsState()

    val density = celsiusViewModel.density.value

    astroData ?: return

    val data = astroData!!

    val sunriseIcon = Res.drawable.sunrise
    val sunsetIcon = Res.drawable.sunset

    val lengthLabel = stringResource(
        if (data.isDay) Res.string.day_length else Res.string.night_length
    )

    val primaryIcon   = if (data.isDay) sunriseIcon else sunsetIcon
    val secondaryIcon = if (data.isDay) sunsetIcon  else sunriseIcon

    val primaryTime   = if (data.isDay) data.sunrise else data.sunset
    val secondaryTime = if (data.isDay) data.sunset  else data.sunrise

    val rowAlignment  = if (data.isDay) Alignment.Bottom else Alignment.CenterVertically

    Row(
        modifier
            .fillMaxSize()
            .padding(horizontal = Paddings.medium, vertical = Paddings.small),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        AstroTimeRow(
            icon = primaryIcon,
            time = primaryTime?.toHHmm().toString(),
            iconAtStart = true,
            verticalAlignment = rowAlignment,
            textSize = dimens.baseInfoTextSizeLarge,
            density = density,
        )

        Column(
            Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            TextLabel(dimens.baseInfoTextSizeLarge, "$lengthLabel:")
            val formattedLength = if (data.isDay) data.length.toFormattedTime() else data.length.toFormattedTime()
            NumericText(dimens.baseInfoTextSizeLarge,  formattedLength, density = density, )
        }

        AstroTimeRow(
            icon = secondaryIcon,
            time = secondaryTime?.toHHmm().toString(),
            iconAtStart = false,
            verticalAlignment = rowAlignment,
            textSize = dimens.baseInfoTextSizeLarge,
            density = density,
        )
    }
}

@Composable
private fun CelsiusLargeScope.AstroTimeRow(
    icon: DrawableResource,
    time: String,
    iconAtStart: Boolean,
    verticalAlignment: Alignment.Vertical,
    textSize: TextUnit,
    density: Density
) {
    Row(
        Modifier.fillMaxHeight(),
        verticalAlignment = verticalAlignment,
    ) {
        if (iconAtStart) {
            AstroIcon(icon)
            Spacer(Modifier.width(Paddings.medium))
            NumericText(textSize, time, density = density)
        } else {
            NumericText(textSize, time, density = density)
            Spacer(Modifier.width(Paddings.medium))
            AstroIcon(icon)
        }
    }
}

@Composable
private fun AstroIcon(icon: DrawableResource) {
    Image(
        painter = painterResource(icon),
        contentDescription = null,
        colorFilter = ColorFilter.tint(Colors.solidWhite),
        contentScale = ContentScale.FillHeight,
    )
}


fun ZonedDateTime.toHHmm(): String =
    format(DateTimeFormatter.ofPattern("HH:mm"))