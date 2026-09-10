package widgets.widgetsModule.widgets.appstime.domain.model

data class RunningApplication(
    val appId: String,
    val appName: String,
    val iconPath: String,
){
    override fun equals(other: Any?) = other is RunningApplication && appId == other.appId
    override fun hashCode() = appId.hashCode()
}
