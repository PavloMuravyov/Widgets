package widgets.widgetsModule.widgets.widgetsWindow.Configurator

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import widgets.widgetsModule.data.models.WidgetSizes

@Immutable
class ConfigDimens (
    val baseInfoTextSize: TextUnit = TextUnit.Unspecified,
    val smallInfoTextSize: TextUnit = TextUnit.Unspecified,
    val showLabelTextSize: TextUnit = TextUnit.Unspecified,
    val dropDownMenuBoxHeight: Dp = Dp.Unspecified,
) {
    companion object {
        fun calculate(
            maxHeight: Dp,
        ): ConfigDimens =

            ConfigDimens(
                baseInfoTextSize =  (maxHeight * 0.07f).value.sp,
                smallInfoTextSize = (maxHeight * 0.05f).value.sp,
                showLabelTextSize = (maxHeight * 0.06f).value.sp,
                dropDownMenuBoxHeight = maxHeight * 0.1f,

            )

    }

}