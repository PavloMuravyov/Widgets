package widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.switcher

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import widgets.Theme.Colors
import widgets.Theme.Paddings
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.extentions.animatedGradientBorder
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.extentions.animatedThumbColor
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.extentions.animatedThumbScale


@Composable
fun SwitcherThumb(
    isChecked: Boolean,
    isCheckedChanged: (Boolean) -> Unit,

){

    Box(
        Modifier

            .fillMaxHeight(0.5f)
            .fillMaxWidth(0.8f)
            .animatedThumbScale(
                isChecked,
            )
            .clickable(
                onClick = {  isCheckedChanged(!isChecked) },
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
            .clip(CircleShape)
            .animatedGradientBorder(
                isChecked,
            )
            .animatedThumbColor(isChecked)
            .shadow(elevation = Paddings.small, shape = CircleShape, ambientColor =
                if (isChecked) Colors.switcherEnabled else Colors.switcherDisabled)

    )
}
