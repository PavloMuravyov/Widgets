package widgets.widgetsModule.widgets.celsius.ui.CelsiusLargeView

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import widgets.Theme.Paddings
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel
import widgets.widgetsModule.widgets.celsius.ui.CelsiusDimens
import widgets.widgetsModule.widgets.celsius.ui.Composables.ConditionIcon
import widgets.widgetsModule.widgets.celsius.ui.Composables.WindArrow
import widgets.widgetsModule.widgets.celsius.ui.TextLabel
import widgets.widgetsModule.widgets.celsius.ui.TextLabelWithTooltip
import org.jetbrains.compose.resources.stringResource
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.wind_speed
import widgets.composeapp.generated.resources.wind_speed_unit


@Composable
fun CelsiusLargeScope.ConditionWindContent(
    celsiusViewModel: CelsiusViewModel,
    modifier: Modifier = Modifier,
    dimens: CelsiusDimens
) {

    Column(modifier.fillMaxSize()) {
        WindConditionIcons(celsiusViewModel, Modifier.weight(1f), dimens)
        WindConditionValues(celsiusViewModel, Modifier.weight(1f), dimens)
    }

}

@Composable
private fun CelsiusLargeScope.WindConditionIcons(
    celsiusViewModel: CelsiusViewModel,
    modifier: Modifier = Modifier,
    dimens: widgets.widgetsModule.widgets.celsius.ui.CelsiusDimens
) {

    Row(
        modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {

        WindIconContent(celsiusViewModel, Modifier.fillMaxHeight().aspectRatio(1f))
        Spacer(Modifier.width(Paddings.medium))
        ConditionIconContent(celsiusViewModel, Modifier.fillMaxHeight().aspectRatio(1f), dimens)
    }

}


@Composable
private fun CelsiusLargeScope.WindConditionValues(
    celsiusViewModel: CelsiusViewModel,
    modifier: Modifier,
    dimens: CelsiusDimens
) {

    val windSpeedLabel = stringResource(Res.string.wind_speed)
    val windSpeedUnit = stringResource(Res.string.wind_speed_unit)
    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {

        WindLabel(celsiusViewModel, windSpeedLabel, windSpeedUnit, dimens.infoTextSizeLarge)
        ConditionLabel(celsiusViewModel, dimens.infoTextSizeLarge)
    }


}


@Composable
private fun CelsiusLargeScope.WindIconContent(celsiusViewModel: CelsiusViewModel, modifier: Modifier) {

    val windData by celsiusViewModel.windData.collectAsState()

    Box(
        modifier,
        contentAlignment = Alignment.Center
    ) {
        windData?.let { data ->
            WindArrow(data)
        }
    }
}


@Composable
private fun CelsiusLargeScope.ConditionIconContent(
    celsiusViewModel: CelsiusViewModel,
    modifier: Modifier,
    dimens: CelsiusDimens
) {

    val condition by celsiusViewModel.currentCondition.collectAsState()
    Box(
        modifier,
        contentAlignment = Alignment.Center
    ) {
        condition?.let { condition ->

            ConditionIcon(condition.code, dimens.conditionIconSizeLarge)
        }
    }
}


@Composable
private fun CelsiusLargeScope.ConditionLabel(celsiusViewModel: CelsiusViewModel, textSize: TextUnit) {

    val condition by celsiusViewModel.currentCondition.collectAsState()

    condition?.let { condition ->

        TextLabelWithTooltip(
            textSize,
            condition.text,
            textSize,
            condition.text,
            textMaxLines = 1
        )
    }
}

@Composable
private fun CelsiusLargeScope.WindLabel(
    celsiusViewModel: CelsiusViewModel,
    windSpeedLabel: String,
    windSpeedUnit: String,
    textSize: TextUnit
) {

    val windData by celsiusViewModel.windData.collectAsState()

    windData?.let { data ->

        TextLabel(
            textSize,
            "$windSpeedLabel: "
                    + data.windSpeed.toString()
                    + " $windSpeedUnit"
        )

    }

}
