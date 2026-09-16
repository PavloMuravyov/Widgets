package widgets.domain.dbus.Location

import com.github.bfsmith.geotimezone.TimeZoneLookup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.freedesktop.dbus.DBusPath
import org.freedesktop.dbus.connections.impl.DBusConnectionBuilder
import org.freedesktop.dbus.interfaces.DBusSigHandler
import org.freedesktop.dbus.interfaces.Properties
import org.freedesktop.dbus.types.UInt32
import java.io.Closeable
import kotlin.time.Duration.Companion.milliseconds


class GeoClueLocationSource(
    private val desktopId: String = "com.example.widgets-com.example.widgets",
    private val accuracy: UInt32 = UInt32(2),
    ) : Closeable {


    private val conn by lazy {
        DBusConnectionBuilder.forSystemBus().build()
    }
    private var geoClueClient: Client? = null
    private var geoClueClientProps: Properties? = null

    fun locationFlow(): Flow<LocationData> = flow {
        emit(getCurrentLocationWithRetry())

        callbackFlow<LocationData> {
            val handler = DBusSigHandler<Properties.PropertiesChanged> { signal ->
                if (!signal.path.contains("GeoClue2")) return@DBusSigHandler
                if (!signal.propertiesChanged.containsKey("Location")) return@DBusSigHandler

                if (isVpnActive()) {
                    println(">>> Location update ignored: VPN is active")
                    return@DBusSigHandler
                }

                val newLocPath = (signal.propertiesChanged["Location"]?.value as? DBusPath)
                    ?: return@DBusSigHandler
                runCatching {
                    val locProps = conn.getRemoteObject(
                        "org.freedesktop.GeoClue2",
                        newLocPath.path,
                        Properties::class.java
                    )

                    val lat = locProps.Get("org.freedesktop.GeoClue2.Location", "Latitude") as Double
                    val lon = locProps.Get("org.freedesktop.GeoClue2.Location", "Longitude") as Double
                    val accuracy = locProps.Get("org.freedesktop.GeoClue2.Location", "Accuracy") as Double
                    if (accuracy > 20_000.0) return@runCatching

                    val timeZone = TimeZoneLookup().getTimeZone(lat, lon).result
                    trySend(LocationData(lat.round2(), lon.round2(), timeZone))
                }.onFailure {
                    println(">>> Error reading location: ${it.message}")
                }
            }
            conn.addSigHandler(Properties.PropertiesChanged::class.java, handler)
            awaitClose {
                conn.removeSigHandler(Properties.PropertiesChanged::class.java, handler)
            }
        }.collect { emit(it) }

    }.flowOn(Dispatchers.IO)


    private val clientMutex = Mutex()

    private suspend fun getOrCreateClient(): Pair<Client, Properties>  = clientMutex.withLock {
        geoClueClient?.let { client ->
            geoClueClientProps?.let { props ->
                return client to props
            }
        }
        val manager = conn.getRemoteObject("org.freedesktop.GeoClue2",
            "/org/freedesktop/GeoClue2/Manager", Manager::class.java)
        val clientPath = manager.GetClient().path
        val client = conn.getRemoteObject("org.freedesktop.GeoClue2", clientPath, Client::class.java)
        val props = conn.getRemoteObject("org.freedesktop.GeoClue2", clientPath, Properties::class.java)

        props.Set("org.freedesktop.GeoClue2.Client", "DesktopId", desktopId)
        props.Set("org.freedesktop.GeoClue2.Client", "RequestedAccuracyLevel", accuracy)
        client.Start()

        geoClueClient = client
        geoClueClientProps = props
        return client to props
    }

    private suspend fun getCurrentLocationWithRetry(
        maxAttempts: Int = 20,
        delayMs: Long = 1000,
    ): LocationData {
        var lastError: Throwable? = null
        repeat(maxAttempts) { attempt ->
            try {
                return getCurrentLocation()
            } catch (e: Exception) {
                lastError = e
                delay(delayMs.milliseconds)
            }
        }
        throw lastError ?: IllegalStateException("Failed to get GeoClue location")
    }

    suspend fun getCurrentLocation(): LocationData = withContext(Dispatchers.IO) {
        val (_, props) = getOrCreateClient()
        val locPath = props.Get("org.freedesktop.GeoClue2.Client", "Location") as DBusPath

        val locProps = conn.getRemoteObject(
            "org.freedesktop.GeoClue2",
            locPath.path,
            Properties::class.java
        )

        val lat = locProps.Get("org.freedesktop.GeoClue2.Location", "Latitude") as Double
        val lon = locProps.Get("org.freedesktop.GeoClue2.Location", "Longitude") as Double


        val timeZone = TimeZoneLookup().getTimeZone(lat,lon).result



        LocationData(
            latitude = lat.round2(),
            longitude = lon.round2(),
            timeZoneId = timeZone
        )

    }

    private fun isVpnActive(): Boolean = runCatching {
        val nm = conn.getRemoteObject(
            "org.freedesktop.NetworkManager",
            "/org/freedesktop/NetworkManager",
            Properties::class.java
        )
        @Suppress("UNCHECKED_CAST")
        val activeConnections = nm.Get(
            "org.freedesktop.NetworkManager", "ActiveConnections"
        ) as List<DBusPath>

        activeConnections.any { path ->
            val activeProps = conn.getRemoteObject(
                "org.freedesktop.NetworkManager",
                path.path,
                Properties::class.java
            )
            activeProps.Get("org.freedesktop.NetworkManager.Connection.Active", "Vpn") as Boolean
        }
    }.getOrDefault(false)


    override fun close() {
        geoClueClient?.Stop()
        geoClueClient = null
        geoClueClientProps = null
        conn.close()
    }
}

fun Double.round2() = Math.round(this * 100) / 100.0

