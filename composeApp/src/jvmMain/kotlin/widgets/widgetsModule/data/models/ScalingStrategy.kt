package widgets.widgetsModule.data.models

enum class ScalingStrategy {
    WIDTH_CONSTRAINED,    //Vertical grid
    HEIGHT_CONSTRAINED;   //Horizontal grid

    companion object {
        fun fromRatio(currentRatio: Float): ScalingStrategy =
            when {
                currentRatio < 1.0f -> WIDTH_CONSTRAINED
                else -> HEIGHT_CONSTRAINED

            }
    }

}

