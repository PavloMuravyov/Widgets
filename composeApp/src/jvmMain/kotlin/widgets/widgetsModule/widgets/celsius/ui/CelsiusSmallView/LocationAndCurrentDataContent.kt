package widgets.widgetsModule.widgets.celsius.ui.CelsiusSmallView

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import widgets.Theme.Colors
import widgets.Theme.Paddings
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel
import widgets.widgetsModule.widgets.celsius.ui.Composables.LocationDataContent
import widgets.widgetsModule.widgets.celsius.ui.Composables.TemperatureContent


@Composable
fun CelsiusSmallScope.LocationAndCurrentDataContent(
    modifier: Modifier,
    celsiusViewModel: CelsiusViewModel,
    dimens: widgets.widgetsModule.widgets.celsius.ui.CelsiusDimens,
) {
    Column(
        modifier.fillMaxSize()
            .padding(Paddings.medium),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start,

        ) {
        LocationDataContent(Modifier.weight(1f),
            dimens.smallInfoTextSizeSmall,
            dimens.baseInfoTextSizeSmall,
            celsiusViewModel)


        CurrentWeatherData(Modifier.weight(1.2f), dimens, celsiusViewModel)
    }


}



@Composable
private fun CelsiusSmallScope.CurrentWeatherData(
    modifier: Modifier,
    dimens: widgets.widgetsModule.widgets.celsius.ui.CelsiusDimens,
    celsiusViewModel: CelsiusViewModel
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Bottom
    ) {
        TemperatureContent(dimens.temperatureLabelTextSizeSmall, celsiusViewModel)
        ConditionContent(dimens.smallInfoTextSizeSmall, celsiusViewModel)
    }

}



@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CelsiusSmallScope.ConditionContent(textSize: TextUnit, celsiusViewModel: CelsiusViewModel) {
    val condition by celsiusViewModel.currentCondition.collectAsState()

    condition?.let { conditionValue ->
        TooltipArea(
            tooltip = {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Colors.semitransparentBlack,
                    elevation = 0.dp
                ) {
                    _root_ide_package_.widgets.widgetsModule.widgets.celsius.ui.TextLabel(
                        textSize,
                        conditionValue.text,
                        color = Colors.solidWhite,
                        maxLines = Int.MAX_VALUE
                    )
                }
            }
        ) {
            _root_ide_package_.widgets.widgetsModule.widgets.celsius.ui.TextLabel(
                textSize, conditionValue.text, color = Colors.solidWhite,
                maxLines = 1
            )
        }
    }

}