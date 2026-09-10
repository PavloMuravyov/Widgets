package widgets.widgetsModule.widgets.celsius.ui.CelsiusMediumView

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import widgets.Theme.Colors
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel
import widgets.widgetsModule.widgets.celsius.ui.CelsiusDimens


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LocationContent(modifier: Modifier, dimens: CelsiusDimens, celsiusViewModel: CelsiusViewModel) {

    val location by celsiusViewModel.locationInfo.collectAsState()

    Box(
        modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ) {
        location?.let { loc ->
            TooltipArea(
                tooltip = {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Colors.semitransparentBlack,
                        elevation = 0.dp
                    ) {
                        _root_ide_package_.widgets.widgetsModule.widgets.celsius.ui.TextLabel(
                            dimens.baseInfoTextSizeMedium,
                            "${loc.city} , ${loc.country}",
                            color = Colors.solidWhite,
                            maxLines = Int.MAX_VALUE
                        )
                    }
                },
            ) {
                val location = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Colors.solidWhite)) {
                        append(
                            "${loc.city}, "
                        )
                    }
                    withStyle(style = SpanStyle(color = Colors.semiTransparentWhite)) {
                        append(
                            loc.countryCode
                        )
                    }
                }

                Text(
                    text = location, fontSize = dimens.baseInfoTextSizeMedium, textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

            }
        }
    }
}