package widgets.widgetsModule.managers.SystemDataManager.model

enum class ColorScheme(val gsettingsValue: String) {
    DEFAULT("default"),
    PREFER_DARK("prefer-dark"),
    PREFER_LIGHT("prefer-light"),
    UNKNOWN("unknown");

    companion object {
        fun fromGSettings(value: String?): ColorScheme =
            entries.find { it.gsettingsValue == value } ?: UNKNOWN
    }
}


val ColorScheme.dimmingCoefficient: Float
    get() = when(this) {
        ColorScheme.DEFAULT -> 0.1f
        ColorScheme.PREFER_DARK -> 0.4f
        ColorScheme.PREFER_LIGHT -> 0.2f
        ColorScheme.UNKNOWN -> 0.1f
    }