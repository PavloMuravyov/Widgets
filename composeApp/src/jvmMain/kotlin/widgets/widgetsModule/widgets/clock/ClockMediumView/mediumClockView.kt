package widgets.widgetsModule.widgets.clock.ClockMediumView

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import widgets.Theme.Colors
import widgets.Theme.Paddings
import widgets.domain.TimeService.localizedDayNameFull
import widgets.domain.TimeService.localizedMonth
import widgets.widgetsModule.widgets.clock.ClockViewModel
import widgets.widgetsModule.widgets.clock.Composables.mechanicalClock.MechanicalClock
import widgets.widgetsModule.widgetsManager.BlurSurface
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.CapturedLayerState


@Composable
fun MediumClockView(viewModel: ClockViewModel, capturedLayerState: CapturedLayerState) {
    Row(
        Modifier.fillMaxSize()
            .padding(Paddings.medium),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {



            BlurSurface(Modifier.weight(1f), capturedLayerState, viewModel){
                MechanicalClock(viewModel, Modifier.fillMaxSize())

            }

        Spacer(Modifier.width(Paddings.medium))


        BlurSurface(Modifier.weight(1f), capturedLayerState, viewModel){
            DateContent(viewModel, Modifier.fillMaxSize())
        }
    }
}

@Composable
fun DateContent(viewModel: ClockViewModel, modifier: Modifier = Modifier) {
    val date by viewModel.dayInfo.collectAsState()

    date?.let { date ->


        DateContentLayout(
            dayNum = date.dayNum ,
            dayName =  date.localizedDayNameFull() ,
            month =  date.localizedMonth() ,
            modifier = modifier
        )
    }
}


@Composable
private fun DateContentLayout(
    dayNum:  String,
    dayName:  String,
    month:  String,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier.padding(
            Paddings.medium
        )
    ) {
        val dateSize = remember(maxHeight) { (maxHeight * 0.44f).value.sp }
        val daySize = remember(maxHeight) { (maxHeight * 0.18f).value.sp }
        val monthSize = remember(maxHeight) { (maxHeight * 0.14f).value.sp }

        val lineHeightStyle = remember {
            LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.Both
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                Modifier.weight(1f),
                contentAlignment = Alignment.TopEnd
            ) {

                DayNumText(
                    dayNum = dayNum,
                    dateSize = dateSize,
                    lineHeightStyle = lineHeightStyle
                )
            }

            Column(
                Modifier.weight(1f),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                DayNameText(dayName = dayName, daySize = daySize)
                MonthText(month = month, monthSize = monthSize)
            }
        }
    }
}
@Composable
private fun DayNumText(
    dayNum: String,
    dateSize: TextUnit,
    lineHeightStyle: LineHeightStyle
) {
    val style = remember(dateSize, lineHeightStyle) {
        TextStyle(
            lineHeight = dateSize,
            lineHeightStyle = lineHeightStyle
        )
    }
    Text(
        text = dayNum,
        color = Colors.solidWhite,
        fontSize = dateSize,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Default,
        style = style
    )
}

@Composable
private fun DayNameText(dayName:String, daySize: TextUnit) {
    Text(
        text = dayName,
        color = Colors.solidWhite,
        fontSize = daySize,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
private fun MonthText(month:  String, monthSize: TextUnit) {
    Text(
        text = month,
        color = Colors.semiTransparentWhite,
        fontSize = monthSize,

    )
}