package widgets.widgetsModule.data.models

import kotlinx.serialization.Serializable

@Serializable
data class WidgetPosition (
    val xPosition: Int? = null,
    val yPosition: Int? = null,
    val rowPosition: Int? = null,
    val columnPosition: Int? = null,
    val containerConstraints: WindowConstraints
){
    companion object {
        fun default(constraints: WindowConstraints) = WidgetPosition(
            xPosition = 0,
            yPosition = 0,
            rowPosition = 0,
            columnPosition = 0,
            containerConstraints = constraints
        )
    }
}



/**
 * Used for adaptive widget positioning while changing widgets window size
 * */
@Serializable
data class WindowConstraints(
    val width: Int? = null,
    val height: Int? = null,
    val lastUpdateTime: Int = System.currentTimeMillis().toInt(),
)
