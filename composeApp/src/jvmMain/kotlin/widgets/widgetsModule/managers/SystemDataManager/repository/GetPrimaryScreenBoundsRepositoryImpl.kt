package widgets.widgetsModule.managers.SystemDataManager.repository

import java.awt.GraphicsEnvironment
import java.awt.Rectangle

class GetPrimaryScreenBoundsRepositoryImpl : GetPrimaryScreenBoundsRepository {

    override fun getPrimaryScreenBounds(): Rectangle? {
        return GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .screenDevices
            .firstOrNull()
            ?.defaultConfiguration
            ?.bounds
    }
}