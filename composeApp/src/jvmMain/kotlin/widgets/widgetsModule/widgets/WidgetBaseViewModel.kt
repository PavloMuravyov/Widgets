package widgets.widgetsModule.widgets

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import widgets.domain.AppSettings.AppSettings
import widgets.domain.AppSettings.AppSettingsRepository
import widgets.domain.DensityProvider.DensityProvider
import widgets.widgetsModule.data.models.BaseWidgetScaledDimensions
import widgets.widgetsModule.data.models.Widget
import widgets.widgetsModule.data.models.WidgetMovingDirection
import widgets.widgetsModule.data.models.WidgetPosition
import widgets.widgetsModule.data.models.WidgetSizes
import widgets.widgetsModule.data.models.WidgetsTypes
import widgets.widgetsModule.managers.GridPositioningManager.GridPositioningManager
import widgets.widgetsModule.managers.SystemDataManager.SystemDataManager
import widgets.widgetsModule.managers.SystemDataManager.model.ColorScheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import org.koin.mp.KoinPlatform
import kotlin.collections.emptyList
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds


abstract class WidgetBaseViewModel(
    private val widgetType: WidgetsTypes,
    private val appSettingsRepository: AppSettingsRepository,
    private val gridPositioningManager: GridPositioningManager,
    private val densityProvider: DensityProvider,
    private val systemDataManager: SystemDataManager? = null,

    ) : ViewModel() {

    val density: StateFlow<Density> = densityProvider.density

    private fun defaultWidget() = Widget(
        widgetType,
        widgetSize = WidgetSizes.Medium,
        widgetData = if (widgetType == WidgetsTypes.AppTimes) "" else null
    )

    private fun AppSettings.findWidget() =
        widgets.find { it.widgetType == widgetType } ?: defaultWidget()

    val widgetSettings: StateFlow<Widget?> = appSettingsRepository.appSettings
        .map { it.findWidget() }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    init {
        val exists = appSettingsRepository.appSettings.value
            .widgets.any { it.widgetType == widgetType }

        if (!exists) {
            appSettingsRepository.update { settings ->
                settings.copy(
                    widgets = settings.widgets + defaultWidget()
                )
            }
        }


    }


    val widgetSize: StateFlow<WidgetSizes> = widgetSettings
        .map {
            it?.widgetSize ?: WidgetSizes.Medium
        }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), WidgetSizes.Medium)


    val position: StateFlow<WidgetPosition?> = widgetSettings
        .map { it?.widgetPosition }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val widgetData: StateFlow<String?> = widgetSettings
        .map { it?.widgetData }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)


    private fun updateWidget(transform: (Widget) -> Widget) {
        appSettingsRepository.update { settings ->
            settings.copy(
                widgets = settings.widgets.map { widget ->
                    if (widget.widgetType == widgetType) transform(widget)
                    else widget
                }
            )
        }
    }

    private val _draggingState = MutableStateFlow<Boolean>(false)
    val draggingState: StateFlow<Boolean> = _draggingState


    fun updateDraggingState(state: Boolean) {
        viewModelScope.launch {
            if (!state) delay(300.milliseconds)
            _draggingState.value = state
        }
    }

    open val availableSizes: List<WidgetSizes> = emptyList()


    private val _dragAnimationEnd = MutableStateFlow<Long>(0L)
    val dragAnimationEnd: StateFlow<Long> = _dragAnimationEnd
    private val _contentOverlayState = MutableStateFlow<Boolean>(false)
    val contentOverlayState: StateFlow<Boolean> = _contentOverlayState

    fun updateContentOverlayState(state: Boolean) {
        _contentOverlayState.value = state
    }


    fun updateDragAnimationEndState(state: Long) {
        _dragAnimationEnd.value = state
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    val needRecalculateOverlay: StateFlow<Long> =
        flowOf(systemDataManager)
            .filterNotNull()
            .flatMapLatest { manager ->
                combine(
                    manager.wallpaperUpdated,
                    manager.systemColorScheme,
                    dragAnimationEnd,
                ) { wallpaperUpdated, colorScheme, dragEnd ->
                    if (wallpaperUpdated) {
                        System.currentTimeMillis()
                    } else 0L
                }
            }
            .distinctUntilChanged()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0L)


    val widgetContainerZIndex: StateFlow<Float> = _draggingState
        .map { isDragging -> if (isDragging) 1f else 0f }
        .transform { value ->
            if (value == 0f) delay(300.milliseconds)
            emit(value)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0f
        )


    protected fun updateSize(size: WidgetSizes) = updateWidget { it.copy(widgetSize = size) }
    protected fun updateWidgetData(newWidgetData: String?) = updateWidget { it.copy(widgetData = newWidgetData) }
    fun updatePosition(
        newPosition: WidgetPosition,
        baseCellDimensions: BaseWidgetScaledDimensions,
        constraints: Constraints,
        occupiedCells: List<Pair<WidgetSizes, WidgetPosition?>> = emptyList(),
        widgetMovingDirection: WidgetMovingDirection = WidgetMovingDirection(),
        expectedWidgetSize: WidgetSizes? = null
    ) {


            val widgetSettingsValue = widgetSettings.value


            widgetSettingsValue?.let {
                val newWidgetCell = gridPositioningManager.getWidgetCell(
                    widgetSettingsValue,
                    baseCellDimensions,
                    constraints,
                    newPosition,
                    occupiedCells,
                    widgetMovingDirection,
                    expectedWidgetSize
                )


                updateWidget { current ->
                    current.copy(widgetPosition = newWidgetCell)
                }

        }
    }


    fun dispose() {
        onCleared()
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }


}