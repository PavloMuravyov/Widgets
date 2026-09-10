package widgets.widgetsModule.widgets.appstime.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import widgets.widgetsModule.data.models.WidgetSizes

@Immutable
data class AppsTimesDimens(

    //Medium
    val graphTimeSize: TextUnit = TextUnit.Unspecified,
    val graphLabelSize: TextUnit = TextUnit.Unspecified,
    val graphYAxisWidth: Dp = Dp.Unspecified,
    val timeTableTextSize: TextUnit = TextUnit.Unspecified,
    val timeTableTimeSize: TextUnit = TextUnit.Unspecified,
    val timeTableCardWidth: Dp = Dp.Unspecified,
    val timeTableCardHeight: Dp = Dp.Unspecified,
    val timeTableIconSize: Dp = Dp.Unspecified,
    val topTimeLabelTextBig: TextUnit = TextUnit.Unspecified,
    val topTimeLabelTextSmall: TextUnit = TextUnit.Unspecified,

    // Small size
    val textSize: TextUnit = TextUnit.Unspecified,
    val textTimeSize: TextUnit = TextUnit.Unspecified,
    val cardHeight: Dp = Dp.Unspecified,
    val spacerHeight: Dp = Dp.Unspecified,
    val timeCounterSize: TextUnit = TextUnit.Unspecified,

    //Large size
) {
    companion object {
        fun calculate(
            maxWidth: Dp,
            maxHeight: Dp,
            widgetSize: WidgetSizes,
        ): AppsTimesDimens = when (widgetSize) {

            WidgetSizes.Small -> AppsTimesDimens(
                textSize = (maxHeight * 0.12f).value.sp,
                textTimeSize = (maxHeight * 0.1f).value.sp,
                cardHeight = (maxHeight * 0.3f).value.dp,
                spacerHeight = (maxHeight * 0.25f).value.dp,
                timeCounterSize = (maxHeight * 0.9f).value.sp

            )

            WidgetSizes.Medium -> AppsTimesDimens(
                graphTimeSize = (maxHeight * 0.08f).value.sp,
                graphLabelSize = (maxHeight * 0.08f).value.sp,
                graphYAxisWidth = (maxWidth * 0.18f).value.dp,
                timeTableTextSize = (maxHeight * 0.09f).value.sp,
                timeTableTimeSize = (maxHeight * 0.1f).value.sp,
                timeTableCardWidth = (maxWidth * 0.5f).value.dp,
                timeTableCardHeight = (maxHeight * 0.4f).value.dp,
                timeTableIconSize = (maxHeight * 0.4f).value.dp,
                topTimeLabelTextBig = (maxHeight * 0.11f).value.sp,
                topTimeLabelTextSmall = (maxHeight * 0.09f).value.sp,
            )

            WidgetSizes.Large -> AppsTimesDimens(

            )
        }
    }
}