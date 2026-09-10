package widgets.widgetsModule.widgets.celsius.ui.CelsiusMediumView

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import widgets.Theme.Colors
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel
import widgets.widgetsModule.widgets.celsius.ui.CelsiusDimens
import widgets.widgetsModule.widgets.celsius.ui.TextLabelWithTooltip


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CelsiusMediumScope.CurrentConditionTextContent(celsiusViewModel: CelsiusViewModel, dimens: CelsiusDimens, modifier: Modifier) {

    val condition by celsiusViewModel.currentCondition.collectAsState()

    Box(
        modifier
            .fillMaxSize(), contentAlignment = Alignment.BottomStart
    ) {

        condition?.let {
            TextLabelWithTooltip(
                dimens.baseInfoTextSizeMedium,
                tooltipText = it.text,
                textSize = dimens.baseInfoTextSizeMedium,
                text = it.text,
                textColor = Colors.solidWhite,
                tooltipMaxLines = Int.MAX_VALUE,
                textMaxLines = 1
            )
        }
    }
}