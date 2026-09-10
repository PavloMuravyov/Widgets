package widgets.DI

import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import widgets.domain.SocketManager.SocketManager
import widgets.widgetsModule.managers.AdaptiveResolutionManager.AdaptiveResolutionManager
import widgets.widgetsModule.managers.GridPositioningManager.GridPositioningManager
import widgets.widgetsModule.managers.SystemDataManager.SystemDataManager
import widgets.widgetsModule.widgetsManager.WidgetsManager
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.dsl.onClose

val managersModule = module {


    single { AdaptiveResolutionManager() }


    single { WidgetsManager(
        adaptiveResolutionManager = get(),
        gridPositioningManager = get(),
        appSettingsRepository = get(),
        systemDataManager = get(),
        showDesktopMenuService = get(),
        densityProvider = get()
    )
    }.onClose {
        it?.onClose()
    }


    single { SystemDataManager(
        getPrimaryScreenBoundsRepository = get(),
        wallpaperMonitor = get(),
        themeMonitor = get(),
        wallpaperMonitorMode = get(),
        JNAloop = get(),
        wakeListener = get(),
        locationRepository = get(),
        connectivityMonitor = get(),
        displayConfigChanges = get(),
    ) }
        .onClose {
            it?.onClose()
        }

    single { GridPositioningManager()}
        .onClose {
            it?.onClose()
        }


    single { SocketManager(
        widgetsManager = get()
    ) }.onClose {
        it?.stopServer()
    }
}