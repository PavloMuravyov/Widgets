package widgets.DI

import widgets.domain.dbus.DisplayConfig.MonitorsChangeSignal
import widgets.domain.dbus.Location.GeoClueLocationSource
import widgets.domain.dbus.Location.LocationRepository
import widgets.domain.dbus.Location.LocationRepositoryImpl
import widgets.domain.dbus.Network.InternetConnectivityMonitor
import widgets.domain.dbus.ShowDesktopMenuService

import widgets.domain.dbus.SleepWakeListener
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.connections.impl.DBusConnectionBuilder
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.dsl.onClose

val dbusModule = module {




    single(named("DBusSession")) {
        DBusConnectionBuilder.forSessionBus().build()
    }.onClose {
        it -> it?.close()
    }

    single(named("DBusSystem")) {
        DBusConnectionBuilder.forSystemBus().build()
    }.onClose {
            it -> it?.close()
    }

    single { MonitorsChangeSignal(get(named("DBusSession"))).apply { start() } }




    single { ShowDesktopMenuService(get(named("DBusSession"))) }

    single { SleepWakeListener(get(named("DBusSystem"))) }


    single { GeoClueLocationSource() }
        .onClose { it?.close()
    }

    single <LocationRepository> { LocationRepositoryImpl(get()) }
        .onClose { it?.close()
        }


    single { InternetConnectivityMonitor(get(named("DBusSystem"))) }

}