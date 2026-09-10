package widgets.DI

import widgets.domain.AppSettings.AppSettingsRepository
import widgets.domain.DensityProvider.DensityProvider
import widgets.domain.dbus.DisplayConfig.MonitorsChangeSignal
import widgets.widgetsModule.managers.SystemDataManager.repository.GetPrimaryScreenBoundsRepository
import widgets.widgetsModule.managers.SystemDataManager.repository.GetPrimaryScreenBoundsRepositoryImpl
import widgets.widgetsModule.widgets.appstime.domain.repository.ScreenTime.ScreenTimeArchiveProvider
import widgets.widgetsModule.widgets.celsius.domain.api.HereGeocodingApi
import widgets.widgetsModule.widgets.celsius.domain.api.WeatherApi
import widgets.widgetsModule.widgets.celsius.domain.model.LocalizedLocation
import widgets.widgetsModule.widgets.celsius.domain.repository.AstroDataRepository
import widgets.widgetsModule.widgets.celsius.domain.repository.AstroDataRepositoryImpl
import widgets.widgetsModule.widgets.celsius.domain.repository.LocalizedLocationRepository
import widgets.widgetsModule.widgets.celsius.domain.repository.LocalizedLocationRepositoryImpl
import widgets.widgetsModule.widgets.celsius.domain.repository.WeatherRepository
import widgets.widgetsModule.widgets.celsius.domain.repository.WeatherRepositoryImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoriesModule = module {

    /**
     * Returns system defined primary screen bounds.
     * */
    single <GetPrimaryScreenBoundsRepository> { GetPrimaryScreenBoundsRepositoryImpl() }

    single { DensityProvider() }


    single(named("username")) { System.getProperty("user.name") }

    single  { AppSettingsRepository(get()) }
    single {  Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }}
    single(named("compactJson")){  Json {
        prettyPrint = false
        ignoreUnknownKeys = true
        encodeDefaults = true
    }}


    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(get<Json>())
            }
        }
    }
    single { WeatherApi(get()) }


    single <WeatherRepository> { WeatherRepositoryImpl(get(), get()) }


    single { HereGeocodingApi(get()) }

    single <LocalizedLocationRepository> { LocalizedLocationRepositoryImpl(get()) }

    single <AstroDataRepository> { AstroDataRepositoryImpl()}

    single { ScreenTimeArchiveProvider() }
}