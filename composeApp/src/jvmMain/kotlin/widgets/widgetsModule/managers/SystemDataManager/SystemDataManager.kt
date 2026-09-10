package widgets.widgetsModule.managers.SystemDataManager

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.dp
import widgets.widgetsModule.managers.SystemWallpapersManager.resizeAndCropWallpaper
import widgets.domain.JNA.useCases.ColorSchemeMonitor
import widgets.domain.JNA.GLibMainLoopService
import widgets.domain.JNA.useCases.PictureOption
import widgets.domain.JNA.useCases.WallpaperMonitor
import widgets.domain.JNA.useCases.WallpaperMonitorMode
import widgets.domain.SocketManager.SocketManager
import widgets.domain.dbus.DisplayConfig.MonitorsChangeSignal
import widgets.domain.dbus.Location.LocationRepository
import widgets.domain.dbus.Network.InternetConnectivityMonitor
import widgets.domain.dbus.SleepWakeListener
import widgets.domain.launchDetachedScript
import widgets.domain.prepareRestartScript
import widgets.restartApplication
import widgets.widgetsModule.managers.SystemWallpapersManager.WallpaperHolder
import widgets.widgetsModule.managers.SystemDataManager.model.ColorScheme
import widgets.widgetsModule.managers.SystemDataManager.model.MonitorArea
import widgets.widgetsModule.managers.SystemDataManager.repository.GetPrimaryScreenBoundsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.skia.Image
import java.awt.Rectangle
import kotlin.system.exitProcess
import kotlin.time.Duration.Companion.milliseconds

class SystemDataManager(
    private val getPrimaryScreenBoundsRepository: GetPrimaryScreenBoundsRepository,
    private val wallpaperMonitor: WallpaperMonitor,
    private val themeMonitor: ColorSchemeMonitor,
    private val wallpaperMonitorMode: WallpaperMonitorMode,
    private val JNAloop: GLibMainLoopService,
    private val wakeListener: SleepWakeListener,
    private val locationRepository: LocationRepository,
    private val connectivityMonitor: InternetConnectivityMonitor,
    private val displayConfigChanges: MonitorsChangeSignal,


    ) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _primaryScreenBounds = MutableStateFlow<Rectangle?>(null)
    val primaryScreenBounds: StateFlow<Rectangle?> = _primaryScreenBounds
    private val _systemColorScheme = MutableStateFlow<ColorScheme>(ColorScheme.UNKNOWN)
    val systemColorScheme: StateFlow<ColorScheme> = _systemColorScheme

    private val _availableMonitorArea = MutableStateFlow<MonitorArea?>(null)
    val availableMonitorArea: StateFlow<MonitorArea?> = _availableMonitorArea


    val locationState = locationRepository.locationState
    val connectivityState = connectivityMonitor.connectivityFlow().stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )


    private val _wallpaperUpdated = MutableStateFlow(false)
    val wallpaperUpdated: StateFlow<Boolean> = _wallpaperUpdated

    var systemWallpaper: Image? = null

    fun updateAvailableMonitorArea(monitorArea: MonitorArea) {
        _availableMonitorArea.value = monitorArea
    }


    val wakeUpStatus: StateFlow<Boolean> = wakeListener.sleepState
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )


    init {
        JNAloop.start()
        wallpaperMonitor.start()
        themeMonitor.start()
        wallpaperMonitorMode.start()

        _primaryScreenBounds.value = getPrimaryScreenBoundsRepository.getPrimaryScreenBounds()

        scope.launch {
            displayConfigChanges
                .changedAt
                .collect{
                    if (it!=null) {
                        restartApplication()
                    }
            }
        }

        scope.launch {
            themeMonitor.state.collect {
                _systemColorScheme.value = ColorScheme.fromGSettings(it)
            }
        }

        scope.launch {
            combine(
                wallpaperMonitor.state,
                wallpaperMonitorMode.state,
                availableMonitorArea
            ) { uri, gsettingsMode, availableArea -> Triple(uri, gsettingsMode, availableArea) }
                .distinctUntilChanged()
                .collectLatest { (uri, gsettingsMode, availableArea) ->

                    if (uri != null && gsettingsMode != null && availableMonitorArea.value != null) {


                            val mode = PictureOption.entries.firstOrNull { gsettingsMode == it.gsettingsValue }
                                ?: PictureOption.OTHER


                        if(mode == PictureOption.OTHER) {

                            _wallpaperUpdated.value = false
                            systemWallpaper?.close()
                            systemWallpaper = null
                            delay(16.milliseconds)
                            _wallpaperUpdated.value = true
                        }

                        else {
                            val result = resizeAndCropWallpaper(
                                uri,
                                primaryScreenBounds.value?.width ?: 1920,
                                primaryScreenBounds.value?.height ?: 1080,
                                availableMonitorArea.value?.wingpanelHeight?.toInt() ?: 0,
                                mode
                            )
                            result.onSuccess { newImage ->
                                _wallpaperUpdated.value = false
                                systemWallpaper?.close()
                                systemWallpaper = newImage
                                delay(16.milliseconds)
                                _wallpaperUpdated.value = true
                            }
                        }
                    }
                }
        }
    }


    fun onClose() {
        wallpaperMonitor.stop()
        themeMonitor.stop()
        wallpaperMonitorMode.stop()
        locationRepository.close()

        scope.cancel()
    }
}
