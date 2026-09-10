package widgets.widgetsModule.widgets.notes

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import widgets.widgetsModule.data.models.WidgetSizes

@Immutable
data class NotesDimens (

    val baseInfoTextSize: TextUnit = TextUnit.Unspecified,
    val deleteIconHeight: Dp = Dp.Unspecified,
    val controlPanelIconHeight: Dp = Dp.Unspecified,
    val largeInfoTextSize: TextUnit = TextUnit.Unspecified,
    val contentProportion: Float = Float.NaN,



    ) { companion object {
    fun calculate(
        maxWidth: Dp,
        maxHeight: Dp,
        widgetSize: WidgetSizes,
    ): NotesDimens = when (widgetSize) {

        WidgetSizes.Small -> NotesDimens(
            baseInfoTextSize = (maxHeight * 0.11f).value.sp,
            deleteIconHeight = (maxHeight * 0.12f).value.dp,
            largeInfoTextSize = (maxHeight * 0.14f).value.sp,
            contentProportion = 4f,
            controlPanelIconHeight = (maxHeight * 0.14f).value.dp

            )

        WidgetSizes.Medium -> NotesDimens(
            baseInfoTextSize = (maxHeight * 0.12f).value.sp,
            deleteIconHeight = (maxHeight * 0.12f).value.dp,
            largeInfoTextSize = (maxHeight * 0.14f).value.sp,
            contentProportion = 3f,
            controlPanelIconHeight = (maxHeight * 0.16f).value.dp

        )

        WidgetSizes.Large -> NotesDimens(
            baseInfoTextSize = (maxHeight * 0.08f).value.sp,
            deleteIconHeight = (maxHeight * 0.08f).value.dp,
            largeInfoTextSize = (maxHeight * 0.1f).value.sp,
            contentProportion = 6f,
            controlPanelIconHeight = (maxHeight * 0.12f).value.dp
        )
    }
}
}