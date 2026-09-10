package widgets.widgetsModule.widgets.celsius.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import widgets.widgetsModule.data.models.WidgetSizes

@Immutable
data class CelsiusDimens(

    //SMALL
    val baseInfoTextSizeSmall: TextUnit = TextUnit.Unspecified,
    val smallInfoTextSizeSmall: TextUnit = TextUnit.Unspecified,
    val timeInfoTextSizeSmall: TextUnit = TextUnit.Unspecified,
    val temperatureLabelTextSizeSmall: TextUnit = TextUnit.Unspecified,
    val conditionIconSize : Dp = Dp.Unspecified,
    val forecastCardHeight: Dp = Dp.Unspecified,
    val forecastIconSize: Dp = Dp.Unspecified,


    //Medium
    val baseInfoTextSizeMedium: TextUnit = TextUnit.Unspecified,
    val smallInfoTextSizeMedium: TextUnit = TextUnit.Unspecified,
    val temperatureLabelTextSizeMedium: TextUnit = TextUnit.Unspecified,
    val conditionIconSizeMedium : Dp = Dp.Unspecified,
    val windArrowSizeMedium: Dp = Dp.Unspecified,
    val forecastCardMediumWidth: Dp = Dp.Unspecified,
    val forecastCardIconSizeMedium: Dp = Dp.Unspecified,
    //Large
    val baseInfoTextSizeLarge: TextUnit = TextUnit.Unspecified,
    val infoTextSizeLarge: TextUnit = TextUnit.Unspecified,
    val temperatureLabelTextSizeLarge: TextUnit = TextUnit.Unspecified,
    val conditionIconSizeLarge : Dp = Dp.Unspecified,
    val windArrowSizeLarge: Dp = Dp.Unspecified,
    val forecastCardLargeHeight: Dp = Dp.Unspecified,
    val forecastCardIconSizeLarge: Dp = Dp.Unspecified,
    val forecastCardTextSizeLarge: TextUnit = TextUnit.Unspecified,
    val forecastCardLargeWidth: Dp = Dp.Unspecified,
    val forecastLineIndicatorHeight: Dp = Dp.Unspecified,

    ) { companion object {
        fun calculate(
            maxWidth: Dp,
            maxHeight: Dp,
            widgetSize: WidgetSizes,
        ): CelsiusDimens = when (widgetSize) {

            WidgetSizes.Small -> CelsiusDimens(
                baseInfoTextSizeSmall = (maxHeight * 0.1f).value.sp,
                smallInfoTextSizeSmall = (maxHeight * 0.08f).value.sp,
                timeInfoTextSizeSmall = (maxHeight * 0.06f).value.sp,
                temperatureLabelTextSizeSmall = (maxHeight * 0.25f).value.sp,
                conditionIconSize = (maxHeight * 0.4f).value.dp,
                forecastCardHeight = (maxHeight * 0.2f).value.dp,
                forecastIconSize = (maxHeight * 0.15f).value.dp,

                )

            WidgetSizes.Medium -> CelsiusDimens(
                baseInfoTextSizeMedium = (maxHeight * 0.08f).value.sp,
                smallInfoTextSizeMedium = (maxHeight * 0.07f).value.sp,
                temperatureLabelTextSizeMedium = (maxHeight * 0.27f).value.sp,
                conditionIconSizeMedium = (maxHeight * 0.7f).value.dp,
                windArrowSizeMedium = ((maxHeight * 0.2f).value.dp),
                forecastCardMediumWidth = (maxWidth * 0.15f).value.dp,
                forecastCardIconSizeMedium = ((maxHeight * 0.15f).value.dp),
            )

            WidgetSizes.Large -> CelsiusDimens(
                baseInfoTextSizeLarge = (maxHeight * 0.0425f).value.sp,
                infoTextSizeLarge = (maxHeight * 0.0425f).value.sp,
                temperatureLabelTextSizeLarge = (maxHeight * 0.2f).value.sp,
                conditionIconSizeLarge = (maxHeight * 0.12f).value.dp,
                windArrowSizeLarge = ((maxHeight * 0.1f).value.dp),
                forecastCardLargeHeight =  (maxHeight * 0.1f).value.dp,
                forecastCardIconSizeLarge = ((maxHeight * 0.1f).value.dp),
                forecastCardTextSizeLarge = (maxWidth * 0.04f).value.sp,
                forecastCardLargeWidth = ((maxWidth * 0.14f).value.dp),
                forecastLineIndicatorHeight =  (maxHeight * 0.025f).value.dp,
                )
        }
    }
}