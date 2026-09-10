package widgets.widgetsModule.widgets.celsius.ui.CelsiusLargeView

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import widgets.Theme.Colors
import widgets.Theme.Paddings
import widgets.widgetsModule.data.models.roundedShape
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel
import widgets.widgetsModule.widgets.celsius.ui.TextLabel
import widgets.widgetsModule.widgetsManager.Utils.BlurSurface.blurFromLayer
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.CapturedLayerState

@Composable
fun CelsiusLargeScope.ControlPanel(
    modifier: Modifier,
    forecastType: ForecastType,
    textSize: TextUnit,
    capturedLayerState: CapturedLayerState,
    celsiusViewModel: CelsiusViewModel,
    onForecastTypeChanged: (ForecastType) -> Unit
) {


    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier
            .width(IntrinsicSize.Max),
        contentAlignment = Alignment.CenterStart
    ) {

        Box(
            Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(celsiusViewModel.widgetSize.value.roundedShape - Paddings.medium * 2))
                .blurFromLayer(capturedLayerState, needBlurEffect = true, needDarkOverlay = true)
        )


        Row(
            Modifier, verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {


            ForecastSelectorCard(ForecastType.Hourly, forecastType, interactionSource, textSize) {
                onForecastTypeChanged(it)
            }

            ForecastSelectorCard(ForecastType.Daily, forecastType, interactionSource, textSize) {
                onForecastTypeChanged(it)
            }

        }
    }


}

@Composable
private fun CelsiusLargeScope.ForecastSelectorCard(
    type: ForecastType,
    forecastType: ForecastType,
    interactionSource: MutableInteractionSource,
    textSize: TextUnit,
    onClick: (ForecastType) -> Unit
) {


    Column(
        Modifier.fillMaxHeight()
            .wrapContentWidth()
            .width(IntrinsicSize.Max)
            .padding(horizontal = Paddings.medium)
            .clickable(
                onClick = { onClick(type) },
                interactionSource = interactionSource,
                indication = null
            ),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        val isSelected = type == forecastType
        val textColor = if (isSelected) Colors.solidWhite else Colors.transparentWhite
        val textWeight = if (isSelected) FontWeight.Normal else FontWeight.Light
        val underlineColor = if (isSelected) Colors.semiTransparentWhite else Color.Transparent

        TextLabel(textSize, type.label(), color = textColor, fontWeight = textWeight)

        Spacer(Modifier.fillMaxWidth().height(1.dp).background(underlineColor))

    }


}