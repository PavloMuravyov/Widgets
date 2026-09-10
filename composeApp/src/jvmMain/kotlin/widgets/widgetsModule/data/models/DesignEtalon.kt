package widgets.widgetsModule.data.models

import androidx.compose.ui.unit.DpSize

data class DesignEtalon(
    val windowWidth: Int = 1904,
    val windowHeight: Int = 967,
    val spacing: Int = 4,
    val baseWidgetSize: DpSize = WidgetSizes.Small.dpSize,
    val yStepsCount: Int = 6
) {
    val widgetAspectRatio: Float = baseWidgetSize.width / baseWidgetSize.height

}
