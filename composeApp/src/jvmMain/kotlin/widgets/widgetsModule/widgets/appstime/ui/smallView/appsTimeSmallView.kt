package widgets.widgetsModule.widgets.appstime.ui.smallView

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import widgets.Theme.Colors
import widgets.Theme.Paddings
import widgets.widgetsModule.data.models.roundedShape
import widgets.widgetsModule.widgets.appstime.AppsTimeViewModel
import widgets.widgetsModule.widgets.widgetsWindow.Backgrounds.buildContainerBlurParams
import widgets.widgetsModule.widgetsManager.BlurSurface
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.CapturedLayerState
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.liquefiable
import io.github.fletchmckee.liquid.liquid


@Composable
fun appsTimeSmallView(viewModel: AppsTimeViewModel, localLiquidState: LiquidState, capturedLayerState: CapturedLayerState) {

    val topContentHeightRatio = 0.25f


        Box(
            Modifier.fillMaxSize()
                .padding(
                    Paddings.medium
                ),
            contentAlignment = Alignment.TopEnd
        ) {

            BlurSurface(Modifier.fillMaxSize(), capturedLayerState, viewModel) {
                bottomContent(
                    Modifier
                        .fillMaxSize()
                        .zIndex(0f)
                        .drawWithContent {
                            clipRect(
                                top = size.height * topContentHeightRatio,
                                bottom = size.height
                            ) {
                                this@drawWithContent.drawContent()
                            }
                        }
                        .liquefiable(localLiquidState),
                    viewModel,

                    )

            }
            topContent(
                Modifier
                    .fillMaxHeight(topContentHeightRatio)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(viewModel.widgetSize.value.roundedShape -  Paddings.medium))
                    .border(
                        width = 1.dp, color = Colors.transparentWhite, shape = RoundedCornerShape(
                            viewModel.widgetSize.value.roundedShape -  Paddings.medium
                        )
                    )
                    .liquid(
                        liquidState = localLiquidState, buildContainerBlurParams(
                            viewModel.widgetSize.value.roundedShape -  Paddings.medium
                        )
                    ),
                viewModel,
            )
        }

}