package widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.switcher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import widgets.Theme.Colors
import widgets.Theme.Paddings
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.extentions.animatedIndicatorShadowOffset
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.extentions.animatedSwitchScale


@Composable
fun SwitcherIndicators(
    isChecked: Boolean,
    density: Density
) {

    var height by remember { mutableStateOf(0.dp) }

    Box(
        Modifier.fillMaxHeight(0.5f)
            .fillMaxWidth(0.8f)
            .padding(Paddings.small)
            .onSizeChanged { size ->
                height = with(density) { size.height.toDp() }
            }
    ) {

        SwitcherIndicator(
            Modifier
                .align(Alignment.CenterStart)
                .size(height)
                .animatedSwitchScale(isChecked, enabledIndicator = false),
            isChecked
        )

        SwitcherIndicator(
            Modifier
                .align(Alignment.CenterEnd)
                .size(height)
                .animatedSwitchScale(isChecked, enabledIndicator = true),
            isChecked
        )
    }
}


@Composable
private fun SwitcherIndicator(
    modifier: Modifier,
    isChecked: Boolean
) {
    IndicatorShadow(modifier, isChecked)

    Box(
        modifier
            .clip(CircleShape)
            .background(Colors.solidWhite)
    )
}


@Composable
private fun IndicatorShadow(modifier: Modifier, isChecked: Boolean) {
    Box(
        modifier
            .animatedIndicatorShadowOffset(isChecked)
            .clip(CircleShape)
            .background(Colors.transparentBlackShadow)
    )
}
