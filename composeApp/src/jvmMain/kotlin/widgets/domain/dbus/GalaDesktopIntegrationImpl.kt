package widgets.domain.dbus




class GalaDesktopIntegrationImpl : DesktopIntegration {

    companion object {
        const val BUS_NAME = "org.pantheon.gala"
        const val OBJECT_PATH = "/org/pantheon/gala/DesktopInterface"
    }
    override fun getObjectPath(): String = OBJECT_PATH
    override fun isRemote(): Boolean = false
    override fun GetRunningApplications(): List<Array<Any>> {
        throw UnsupportedOperationException()
    }

    override fun GetWindows(): List<Array<Any>> {
        throw UnsupportedOperationException()
    }
}
