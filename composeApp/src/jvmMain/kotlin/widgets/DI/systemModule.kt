package widgets.DI

import widgets.domain.JNA.useCases.ColorSchemeMonitor
import widgets.domain.JNA.GLib
import widgets.domain.JNA.GLibMainLoopService
import widgets.domain.JNA.GObject
import widgets.domain.JNA.Gio
import widgets.domain.JNA.GtkLibrary
import widgets.domain.JNA.SystemContext
import widgets.domain.JNA.useCases.WallpaperMonitor
import widgets.domain.JNA.useCases.WallpaperMonitorMode
import widgets.domain.TimeService.TimeService
import widgets.domain.TimeService.TimeServiceImpl
import widgets.widgetsModule.widgets.appstime.domain.repository.DesktopEntryReader
import widgets.widgetsModule.widgets.appstime.domain.repository.DesktopEntryReaderImpl
import widgets.widgetsModule.widgets.appstime.domain.repository.GalaDbusListener
import widgets.widgetsModule.widgets.appstime.domain.repository.GalaDbusListenerImpl
import widgets.widgetsModule.widgets.appstime.domain.repository.IconResolver
import widgets.widgetsModule.widgets.appstime.domain.repository.IconResolverImpl
import com.sun.jna.Native
import com.sun.jna.Pointer
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.dsl.onClose


val systemModule = module {

    single<Gio> {
        Native.load("gio-2.0", Gio::class.java)
    }

    single<GObject> {
        Native.load("gobject-2.0", GObject::class.java)
    }

    single<GLib> {
        Native.load("glib-2.0", GLib::class.java)
    }




    single<GtkLibrary> {
        Native.load("gtk-3", GtkLibrary::class.java).also { lib ->
            lib.gtk_init(0, null)
        }
    }
    single<Pointer> {
        get<GtkLibrary>().gtk_icon_theme_get_default()
    }
    single<IconResolver> { IconResolverImpl(get<GtkLibrary>(), get<Pointer>()) }
    single <DesktopEntryReader> { DesktopEntryReaderImpl(get<IconResolver>()) }
    single <GalaDbusListener> {
        GalaDbusListenerImpl(
            get<DesktopEntryReader>(),
            get(named("DBusSession"))
        )
    }



    single {
        SystemContext(
            gio = get(),
            gobject = get(),
            glib = get(),
        )
    }

    single { GLibMainLoopService(get()) }

    single { WallpaperMonitor(get()) }.onClose {
        it?.stop()
    }
    single { ColorSchemeMonitor(get()) }.onClose {
        it?.stop()
    }

    single { WallpaperMonitorMode(get()) }.onClose {
        it?.stop()
    }

    single<TimeService> { TimeServiceImpl() }.onClose {
        it?.onClose()
    }

}