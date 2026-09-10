package widgets.widgetsModule.data.models

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable

@Serializable
enum class WidgetSizes{
    Small,
    Medium,
    Large
}


//USED ONLY FOR DESIGN ETALON
val WidgetSizes.dpSize: DpSize
    get() = when(this) {
        WidgetSizes.Small ->  DpSize(182.dp, 154.dp)
        WidgetSizes.Medium -> DpSize(368.dp, 154.dp)
        WidgetSizes.Large -> DpSize(368.dp, 312.dp)
    }

val WidgetSizes.roundedShape : Dp
    get () = 25.dp


fun WidgetSizes.scaledSize (baseWidgetSize: BaseWidgetScaledDimensions) : DpSize{
    return when(this) {
        WidgetSizes.Small -> DpSize(baseWidgetSize.width, baseWidgetSize.height)
        WidgetSizes.Medium -> DpSize((baseWidgetSize.width.value * 2 + baseWidgetSize.spacing.value ).dp, baseWidgetSize.height)
        WidgetSizes.Large -> DpSize(
            (baseWidgetSize.width.value * 2 + baseWidgetSize.spacing.value ).dp,
            (baseWidgetSize.height.value * 2 + baseWidgetSize.spacing.value ).dp)
    }
}


//USED FOR VISUALISATION IN CONFIG WINDOW
fun WidgetSizes.getAbsoluteSize(baseSize: DpSize, widgetSize: WidgetSizes): DpSize = when (widgetSize) {
    WidgetSizes.Small -> baseSize
    WidgetSizes.Medium -> DpSize(baseSize.width * 2, baseSize.height)
    WidgetSizes.Large -> DpSize(baseSize.width * 2, baseSize.height * 2)
}




//For socket communication with extensions widgets
val WidgetSizes.toByteValue : Byte
    get() = when (this) {
        WidgetSizes.Small -> 1
        WidgetSizes.Medium -> 2
        WidgetSizes.Large -> 3
    }

