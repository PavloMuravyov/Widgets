package widgets.widgetsModule.widgets.widgetsWindow

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import widgets.domain.dbus.ShowDesktopMenuService
import widgets.widgetsModule.managers.SystemDataManager.SystemDataManager
import widgets.widgetsModule.managers.SystemDataManager.model.MonitorArea
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject
import org.koin.mp.KoinPlatform
import java.awt.Point
import kotlin.getValue
import kotlin.time.Duration.Companion.milliseconds

class WidgetsWindowViewModel (
    private val showDesktopMenuService: ShowDesktopMenuService,
    private val systemDataManager: SystemDataManager
) : ViewModel() {



 /*   val availableArea = systemDataManager.availableMonitorArea


    fun showDesktopMenu(point: Point) {

        viewModelScope.launch (Dispatchers.Default) {

            availableArea.value?.let { area ->

                showDesktopMenuService.invoke(
                    Point(200, 100),
                    area
                )
            }
        }
    }*/





}


