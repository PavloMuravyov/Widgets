package widgets.widgetsModule.widgetsManager

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import widgets.widgetsModule.managers.SystemDataManager.SystemDataManager
import widgets.widgetsModule.managers.SystemDataManager.model.MonitorArea
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ConfigWindow
import widgets.widgetsModule.widgets.widgetsWindow.DesktopReplacer.DesktopReplacer
import io.github.fletchmckee.liquid.rememberLiquidState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.icon
import widgets.composeapp.generated.resources.windowName

@Composable
fun widgetsWindow(
    systemDataManager: SystemDataManager, widgetsManager: WidgetsManager, exitApplication: () -> Unit
) {

    val mainScreenBounds by systemDataManager.primaryScreenBounds.collectAsState()
    val windowState = rememberWindowState(
        placement = WindowPlacement.Maximized,
        position = WindowPosition.Absolute(mainScreenBounds?.x?.dp ?: 0.dp, mainScreenBounds?.y?.dp ?: 0.dp)
    )

    Window(
        onCloseRequest = { exitApplication() },
        undecorated = true,
        visible = true,
        transparent = true,
        state = windowState,
        icon = painterResource(Res.drawable.icon),

        title = stringResource(Res.string.windowName)
    ) {




        LaunchedEffect(windowState.size) {
            if (window.isActive) {
                systemDataManager.updateAvailableMonitorArea(
                    MonitorArea(
                        areaSize = windowState.size,
                        wingpanelHeight = windowState.position.y.value,
                    )
                )


                println("window y  ${window.y}")
                /**
                 *
                 * USED TO SAVE OTHER APPS FOCUS WHEN CLICKED TO DESKTOP (WIDGET WINDOW)
                 * */
            //    window.focusableWindowState = false

            }
        }

        widgetWindowContent(widgetsManager, systemDataManager)
    }
}


@Composable
fun widgetWindowContent(
    widgetsManager: WidgetsManager,
    systemDataManager: SystemDataManager,
){

    val baseWidgetScaledDimensions by widgetsManager.widgetBaseSizes.collectAsState()
    val density = widgetsManager.density.value
    val liquidState = rememberLiquidState()
    val availableArea by systemDataManager.availableMonitorArea.collectAsState()


    DesktopReplacer(
        systemDataManager,
        liquidState,
        onMouse3Click = { it ->
            widgetsManager.showDesktopMenu(it)
        }
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(baseWidgetScaledDimensions?.spacing ?: 0.dp)

            ) {

        availableArea?.let { availableMonitorArea ->

                widgetsView(
                    widgetsManager, liquidState, density, availableMonitorArea
                )


        }
       baseWidgetScaledDimensions?.let { baseWidgetScaledDimensions ->
            ConfigWindow(
                widgetsManager,
                baseWidgetScaledDimensions,
                liquidState,
                density,
                DpSize(maxWidth, maxHeight),)
        }


    }
}
