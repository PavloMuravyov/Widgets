package widgets.widgetsModule.widgets.celsius.domain.repository

import widgets.domain.dbus.Location.LocationData
import widgets.widgetsModule.widgets.celsius.domain.model.AstroData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface AstroDataRepository {
    fun observeAstroData(locationData: LocationData): Flow<AstroData>

}