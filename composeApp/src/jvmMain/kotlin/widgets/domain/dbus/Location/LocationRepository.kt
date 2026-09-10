package widgets.domain.dbus.Location

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface LocationRepository {

    val locationState: Flow<LocationData?>
    fun close()

}