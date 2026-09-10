package widgets.widgetsModule.managers.SystemDataManager.repository

import java.awt.Rectangle

interface GetPrimaryScreenBoundsRepository {

    fun getPrimaryScreenBounds(): Rectangle?
}