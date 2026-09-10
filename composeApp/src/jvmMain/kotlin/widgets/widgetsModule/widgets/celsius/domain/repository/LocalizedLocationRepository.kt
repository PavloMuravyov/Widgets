package widgets.widgetsModule.widgets.celsius.domain.repository

import widgets.domain.dbus.Location.LocationData
import widgets.widgetsModule.widgets.celsius.domain.model.LocalizedLocation

interface LocalizedLocationRepository {

    suspend fun getLocation(location: LocationData): LocalizedLocation
}