package widgets.DI

import widgets.widgetsModule.widgets.appstime.AppsTimeViewModel
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel
import widgets.widgetsModule.widgets.clock.ClockViewModel
import widgets.widgetsModule.widgets.notes.NotesViewModel
import widgets.widgetsModule.widgets.widgetsWindow.WidgetsWindowViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


val viewModelsModule = module {
    viewModel { CelsiusViewModel(get(), get(), get(), get(), get(), get () )}
    viewModel { NotesViewModel(get(), get() ,get (),get(named("compactJson"))) }
    viewModel { ClockViewModel(get(), get(), get(), get(), get ()) }
    viewModel { AppsTimeViewModel(get(), get(),get(named("compactJson")), get(),get(), get(),  get (), get()) }
    viewModel { WidgetsWindowViewModel(get(), get()) }

}