package widgets.widgetsModule.widgets.appstime.domain.repository

import java.io.File

interface DesktopEntryReader {

    fun read(appId: String): Pair<String, String>

    fun findDesktopFile(appId: String): File?

}