package widgets.widgetsModule.managers.AdaptiveResolutionManager

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import widgets.widgetsModule.data.models.BaseWidgetScaledDimensions
import widgets.widgetsModule.data.models.DesignEtalon
import widgets.widgetsModule.data.models.ScalingStrategy

import kotlin.math.roundToInt

class CalculateScaledDimensions (

    private val etalon: DesignEtalon = DesignEtalon(),
) {

    operator fun invoke(strategy: ScalingStrategy,
                        currentWidth: Dp,
                        currentHeight: Dp,) : BaseWidgetScaledDimensions {

        return  when (strategy) {
            ScalingStrategy.HEIGHT_CONSTRAINED -> {
                calculateDimensionsFromHeight(currentHeight)
            }
            ScalingStrategy.WIDTH_CONSTRAINED -> {
                calculateDimensionsFromWidth(currentWidth)
            }
        }
    }

    private fun calculateDimensionsFromHeight(currentHeight: Dp): BaseWidgetScaledDimensions {
        val scale = currentHeight.value / etalon.windowHeight

        val height = (etalon.baseWidgetSize.height * scale).roundToDp()
        val width = (height * etalon.widgetAspectRatio).roundToDp()
        val spacing = calculateSpacing(currentHeight.value, height.value, etalon.yStepsCount).dp

        return BaseWidgetScaledDimensions(width, height, spacing)
    }


    //FOR VERTICAL ORIENTATION
    private fun calculateDimensionsFromWidth(currentWidth: Dp): BaseWidgetScaledDimensions {

        val etalonProportion = etalon.windowHeight / etalon.baseWidgetSize.height.value
        val widthNew = currentWidth.value / etalonProportion
        val height = (widthNew.dp / etalon.widgetAspectRatio).roundToDp()
        val spacing = calculateSpacing(currentWidth.value, widthNew, etalon.yStepsCount).dp
        return BaseWidgetScaledDimensions(widthNew.dp, height, spacing)
    }



    private fun calculateSpacing(totalSize: Float, widgetSize: Float, count: Int): Int {
        return ((totalSize - widgetSize * count) / (count + 1)).toInt()
    }

    private fun Dp.roundToDp(): Dp = this.value.roundToInt().dp
}