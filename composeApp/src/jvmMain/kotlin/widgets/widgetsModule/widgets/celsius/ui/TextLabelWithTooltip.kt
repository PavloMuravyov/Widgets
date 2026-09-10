package widgets.widgetsModule.widgets.celsius.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import widgets.Theme.Colors

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TextLabelWithTooltip(
    tooltipTextSize: TextUnit,
    tooltipText: String,
    textSize: TextUnit,
    text: String,
    textColor: Color = Colors.solidWhite,
    tooltipMaxLines: Int = Int.MAX_VALUE,
    textMaxLines: Int? = null,
){
    TooltipArea(
        tooltip = {
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = Colors.semitransparentBlack,
                elevation = 0.dp
            ) {
               TextLabel(
                    tooltipTextSize,
                    tooltipText,
                    color = Colors.solidWhite,
                    maxLines = tooltipMaxLines
                )
            }
        },
    ) {
        TextLabel(
            textSize,
            text,
            textColor,
            textMaxLines ?: 1
        )
    }
}