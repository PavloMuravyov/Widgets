package widgets.domain.dbus.Location

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class LocationRepositoryImpl (
    private val geoClueLocationSource: GeoClueLocationSource,
    ) : LocationRepository {


    override val locationState: Flow<LocationData?> =
        geoClueLocationSource.locationFlow()
            .distinctUntilChanged()

    override fun close() {
        geoClueLocationSource.close()
    }


}