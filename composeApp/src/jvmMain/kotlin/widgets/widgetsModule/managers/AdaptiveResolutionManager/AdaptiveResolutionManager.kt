package widgets.widgetsModule.managers.AdaptiveResolutionManager

import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import widgets.widgetsModule.data.models.BaseWidgetScaledDimensions
import widgets.widgetsModule.data.models.DesignEtalon
import widgets.widgetsModule.data.models.ScalingStrategy

class AdaptiveResolutionManager {

    fun adaptSizes(currentConstraints: Constraints, density: Density) : BaseWidgetScaledDimensions{

        val currentWidth = with(density) { currentConstraints.maxWidth.toDp() }
        val currentHeight = with(density) { currentConstraints.maxHeight.toDp() }

        val currentRatio = currentWidth / currentHeight

        val strategy = ScalingStrategy.fromRatio(currentRatio)

        return CalculateScaledDimensions().invoke(strategy, currentWidth, currentHeight)

    }

}