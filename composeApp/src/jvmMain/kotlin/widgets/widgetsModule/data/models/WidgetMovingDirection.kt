package widgets.widgetsModule.data.models

import androidx.compose.runtime.MutableState

data class WidgetMovingDirection(
    val right: Directions = Directions.STILL,
    val down: Directions = Directions.STILL,
)

enum class Directions {
    POSITIVE, NEGATIVE, STILL
}


data class MutableMovementDirection(
    val horizontal: MutableState<Directions>,
    val vertical: MutableState<Directions>
)
