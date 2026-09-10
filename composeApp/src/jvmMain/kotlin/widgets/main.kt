package widgets

import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.window.application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.memory.MemoryCache
import coil3.svg.SvgDecoder
import kotlinx.coroutines.flow.first
import org.jetbrains.compose.resources.Font
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.stopKoin
import org.koin.mp.KoinPlatform
import widgets.DI.dependencyInjections
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.Roboto_Regular
import widgets.domain.DensityProvider.DensityProvider
import widgets.domain.SocketManager.SocketManager
import widgets.domain.launchDetachedScript
import widgets.domain.prepareRestartScript
import widgets.widgetsModule.managers.SystemDataManager.SystemDataManager
import widgets.widgetsModule.widgetsManager.WidgetsManager
import widgets.widgetsModule.widgetsManager.widgetsWindow


fun main() {

    startKoin {
        printLogger()
        modules(dependencyInjections)
    }



    val systemDataManager = KoinPlatform.getKoin().get<SystemDataManager>()
    val widgetsManager = KoinPlatform.getKoin().get<WidgetsManager>()
    val socketManager = KoinPlatform.getKoin().get<SocketManager>()
    val densityProvider = KoinPlatform.getKoin().get<DensityProvider>()

    application {
        SingletonImageLoader.setSafe {
            ImageLoader.Builder(PlatformContext.INSTANCE)
                .components { add(SvgDecoder.Factory()) }
                .memoryCache {
                    MemoryCache.Builder()
                        .maxSizePercent(PlatformContext.INSTANCE, 0.05)
                        .build()
                }
                .diskCache(null)
                .build()
        }

        LaunchedEffect(Unit) {
            systemDataManager
                .wakeUpStatus
                .first{it}
                .let {
                    restartApplication()
                }
        }

        val robotoFontFamily = FontFamily(
            Font(resource = Res.font.Roboto_Regular, weight = FontWeight.Normal),
        )

        val myTypography = Typography(
            defaultFontFamily = robotoFontFamily
        )


        MaterialTheme (
           typography = myTypography
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides LocalTextStyle.current.copy(
                    lineHeight = LocalTextStyle.current.fontSize,
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.Both
                    )
                ),

            ) {
                densityProvider.update(LocalDensity.current)

                widgetsWindow(systemDataManager, widgetsManager, ::exitApplication)




            }
        }
        DisposableEffect(Unit) {
            onDispose {
                systemDataManager.onClose()
                widgetsManager.onClose()
                stopKoin()
            }
        }
    }
}


suspend fun restartApplication() {
    val file = prepareRestartScript()
    launchDetachedScript(file).onSuccess {
        Runtime.getRuntime().exit(0)
    }
}


