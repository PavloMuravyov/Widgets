package widgets.widgetsModule.widgetsManager

import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import widgets.domain.AppSettings.AppSettingsRepository
import widgets.domain.AutostartManager.AutostartManager
import widgets.domain.DensityProvider.DensityProvider
import widgets.domain.dbus.ShowDesktopMenuService
import widgets.domain.runHideScript
import widgets.widgetsModule.data.models.*
import widgets.widgetsModule.managers.AdaptiveResolutionManager.AdaptiveResolutionManager
import widgets.widgetsModule.managers.GridPositioningManager.GridPositioningManager
import widgets.widgetsModule.managers.SystemDataManager.SystemDataManager
import widgets.widgetsModule.widgets.WidgetBaseViewModel
import widgets.widgetsModule.widgets.appstime.AppsTimeViewModel
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel
import widgets.widgetsModule.widgets.clock.ClockViewModel
import widgets.widgetsModule.widgets.notes.NotesViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.jetbrains.compose.resources.getString
import org.koin.mp.KoinPlatform
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.windowName
import java.awt.Point
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class WidgetsManager(
    private val adaptiveResolutionManager: AdaptiveResolutionManager,
    private val gridPositioningManager: GridPositioningManager,
    private val appSettingsRepository: AppSettingsRepository,
    private val systemDataManager: SystemDataManager,
    private val showDesktopMenuService: ShowDesktopMenuService,
    private val densityProvider: DensityProvider


    ) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val density: StateFlow<Density> = densityProvider.density


    private val _widgetSizes = MutableStateFlow<BaseWidgetScaledDimensions?>(null)
    val widgetBaseSizes: StateFlow<BaseWidgetScaledDimensions?> = _widgetSizes

    private val availableArea = systemDataManager.availableMonitorArea

    private val appSettings = appSettingsRepository.appSettings

    val gridPositioningDelimiters = gridPositioningManager.gridDelimiters


    private val autostartManager = AutostartManager()
    val autostartState = autostartManager.autostartEnabled

    fun updateAutostartState(autostartEnabled: Boolean) {
        autostartManager.updateAutostartState(autostartEnabled)
    }



    val backgroundType: StateFlow<BackgroundTypes> = appSettings
        .map { it.backgroundType  ?:  BackgroundTypes.Liquid}
        .distinctUntilChanged()
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = appSettings.value.backgroundType ?: BackgroundTypes.Liquid
        )

    fun updateBackgroundType(newBackgroundType: BackgroundTypes?) {
        appSettingsRepository.update { it.copy(backgroundType = newBackgroundType) }
    }


    val blurOverlayMode: StateFlow<BlurOverlayModes> = appSettings
        .map { it.blurOverlayMode  ?: BlurOverlayModes.Auto}
        .distinctUntilChanged()
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = appSettings.value.blurOverlayMode ?: BlurOverlayModes.Auto
        )

    fun updateBlurOverlayMode(newBlurOverlayMode: BlurOverlayModes) {
        appSettingsRepository.update { it.copy(blurOverlayMode = newBlurOverlayMode) }
    }


    val enabledWidgetTypes: StateFlow<List<WidgetsTypes>> = appSettings
        .map { it.enabledWidgets }
        .distinctUntilChanged()
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = appSettings.value.enabledWidgets
        )



    private val _enabledViewModels = MutableStateFlow<Map<WidgetsTypes, WidgetBaseViewModel>>(emptyMap())
    val enabledViewModels: StateFlow<Map<WidgetsTypes, WidgetBaseViewModel>> = _enabledViewModels


    val occupiedCells: StateFlow<List<Pair<WidgetSizes, WidgetPosition?>>> =
        appSettingsRepository.appSettings
            .map { settings ->
                settings.widgets
                    .mapNotNull {
                        if(enabledWidgetTypes.value.contains(it.widgetType) && it.widgetSize != null) {
                               it.widgetSize to it.widgetPosition
                        } else null
                    }
            }
            .distinctUntilChanged()
            .stateIn(
                scope = scope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList()
            )

    private val _activeViewModelsList = MutableStateFlow<List<WidgetBaseViewModel>>(emptyList())



    private val _showConfigWindow = MutableStateFlow(false)
    val showConfigWindow: StateFlow<Boolean> = _showConfigWindow
    fun updateShowConfigWindow(showConfigWindow: Boolean) {
        _showConfigWindow.value = showConfigWindow
    }

    private val _selectedWidgetType = MutableStateFlow<WidgetsTypes>(WidgetsTypes.entries[0])
    val selectedWidgetType: StateFlow<WidgetsTypes> = _selectedWidgetType

    fun updateSelectedWidgetType(widgetType: WidgetsTypes) {
        _selectedWidgetType.value = widgetType
    }


    val lastChangedWidget = MutableStateFlow<WidgetsTypes?>(null)

    val isAnyWidgetDragging: StateFlow<Boolean> = _activeViewModelsList
        .flatMapLatest { viewModels ->
            if (viewModels.isEmpty()) {
                flowOf(false)
            } else {
                combine(viewModels.map {

                    it.draggingState
                }) {

                    states -> states.any { it }

                }
            }
        }
        .distinctUntilChanged()
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    @Volatile
    var constraints: Constraints? = null



    init {

        scope.launch {
            var processed = emptySet<WidgetsTypes>()

            _enabledViewModels.collect { enabled ->
                val toProcess = enabled.keys - processed

                toProcess.forEach { type ->
                    val widget = enabled.getValue(type)
                    val settings = widget.widgetSettings.first { it != null }

                    if (settings?.widgetPosition == null) {

                        println("${widget.widgetSettings.value?.widgetType}  position == ${widget.position.value}}")
                        constraints?.let { constraints ->
                            val windowConstraints = WindowConstraints(constraints.maxWidth, constraints.maxHeight)
                            val position = WidgetPosition.default(windowConstraints)
                            val baseCellDimensions = widgetBaseSizes.value
                            val occupiedCells = occupiedCells.value.filterNot { it.second == null }

                            if (baseCellDimensions != null) {
                                widget.updatePosition(position, baseCellDimensions, constraints, occupiedCells)
                            }
                        }
                    }
                }

                processed = enabled.keys
            }
        }


        scope.launch {
            enabledWidgetTypes.collect { it ->
                updateShowConfigWindow(it.isEmpty())
                cancel()
            }
        }

        scope.launch {
            runCatching {
                runHideScript(getString(Res.string.windowName))
            }.onFailure { e ->
                println("hide.sh error: ${e.message}")
            }

        }

        scope.launch {
            combine(
                availableArea,
                density
            ) { area, density -> area to density }

                .distinctUntilChanged()
                .collectLatest { (area, density) ->
                    if (area != null && density != null) {

                        constraints = Constraints(
                            area.areaSize.width.value.toInt(),
                            area.areaSize.width.value.toInt(),
                            area.areaSize.height.value.toInt(),
                            area.areaSize.height.value.toInt()
                        )

                        getBaseWidgetsScaledDimensions(
                            constraints!!,
                            density
                        )
                    }

                }
        }

        scope.launch {
            widgetBaseSizes.collect {
                val widgetSizes = widgetBaseSizes.value
                if (widgetSizes != null && constraints != null) {
                    gridPositioningManager.generateGrid(widgetSizes, constraints!!)
                }
            }
        }


        /**
         *
         * Updates widgets positions to current screen parameters
         * */
        scope.launch {
            gridPositioningDelimiters.collect {
                val constraints = constraints ?: return@collect
                val settings = appSettingsRepository.appSettings.value
                val updatedWidgets = settings.widgets.map { widget ->
                    val position = widget.widgetPosition
                    if (widget.widgetType in settings.enabledWidgets && position != null) {
                        val validated = gridPositioningManager.getPositionByCell(position, constraints)
                        widget.copy(widgetPosition = validated)
                    } else widget
                }
                if (updatedWidgets == settings.widgets) return@collect
                appSettingsRepository.update { it.copy(widgets = updatedWidgets) }

            }
        }




            scope.launch {
                enabledWidgetTypes
                    .collect { currentEnabled ->
                        _enabledViewModels.value.keys
                            .filter { it !in currentEnabled }
                            .forEach { type ->
                                _enabledViewModels.value.getValue(type).dispose()
                                _enabledViewModels.value = _enabledViewModels.value.filterNot { it.key == type }
                            }

                        currentEnabled
                            .filter { it !in _enabledViewModels.value  }
                            .forEach { type ->
                                _enabledViewModels.value += mapOf(type to createViewModel(type))
                            }
                        _activeViewModelsList.value = _enabledViewModels.value.values.toList()

                    }

        }


    }

    fun getViewModel(type: WidgetsTypes): WidgetBaseViewModel? = _enabledViewModels.value[type]
    private suspend fun createViewModel(type: WidgetsTypes): WidgetBaseViewModel = withContext(Dispatchers.Default) {

        val koin = KoinPlatform.getKoin()

        when (type) {
            WidgetsTypes.Celsius -> koin.get<CelsiusViewModel>()
            WidgetsTypes.Notes -> koin.get<NotesViewModel>()
            WidgetsTypes.Clock -> koin.get<ClockViewModel>()
            WidgetsTypes.AppTimes -> koin.get<AppsTimeViewModel>()
        }
    }





    fun updateWidgetEnabledState(widgetType: WidgetsTypes, enabled: Boolean) {

        lastChangedWidget.value = widgetType

        val current = appSettingsRepository.appSettings.value.enabledWidgets
        if (enabled && widgetType in current) return
        if (!enabled && widgetType !in current) return

        appSettingsRepository.update { settings ->
            settings.copy(
                enabledWidgets = if (enabled) {
                    settings.enabledWidgets + widgetType
                } else {
                    settings.enabledWidgets - widgetType
                }

                )
            }

    }


    private fun validatePosition(widgetType: WidgetsTypes, expectedWidgetSize: WidgetSizes? = null){

        if(widgetType in enabledWidgetTypes.value){
            val viewModel = getViewModel(widgetType)

            viewModel?.let { vM ->

                val position = vM.position.value
                val baseCellDims = widgetBaseSizes.value
                val constraints = constraints
                val occupiedCells = occupiedCells.value.filterNot { it.second == position }

                if (position != null && baseCellDims != null && constraints != null) {
                    vM.updatePosition(
                        newPosition = position,
                        baseCellDimensions = baseCellDims,
                        constraints = constraints,
                        occupiedCells = occupiedCells,
                        expectedWidgetSize = expectedWidgetSize
                    )
                }
            }
        }
    }



    val widgetSizes: StateFlow<Map<WidgetsTypes, WidgetSizes>> = appSettingsRepository.appSettings
        .map { settings ->
            settings.widgets.associate { widget -> widget.widgetType to (widget.widgetSize ?: WidgetSizes.Small)  }
        }
        .distinctUntilChanged()
        .stateIn(
            scope = scope  ,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

    fun updateWidgetSize(widgetType: WidgetsTypes, widgetSize: WidgetSizes) {

        lastChangedWidget.value = widgetType

        validatePosition(widgetType, widgetSize)

        appSettingsRepository.update { settings ->
            val existing = settings.widgets.find { it.widgetType == widgetType }
            val updatedWidgets = if (existing != null) {
                settings.widgets.map {
                    if (it.widgetType == widgetType) it.copy(widgetSize = widgetSize) else it
                }
            } else {
                settings.widgets + Widget(widgetType = widgetType, widgetSize = widgetSize)
            }

            settings.copy(widgets = updatedWidgets)
        }

    }


   fun showDesktopMenu(point: Point) {
       scope.launch {
               showDesktopMenuService.invoke(point)
       }
   }

    private fun getBaseWidgetsScaledDimensions(newSize: Constraints, density: Density) {
        scope.launch {
            _widgetSizes.value = adaptiveResolutionManager.adaptSizes(newSize, density)
        }.invokeOnCompletion { cause ->
            if (cause == null) println("Adaptive resolution manager completed successfully")
            else println("Failed: $cause")
        }
    }


    fun onClose() {
        _enabledViewModels.value.values.forEach { it.dispose() }
        _enabledViewModels.value = emptyMap()
        autostartManager.cleanup()
        scope.cancel()
        gridPositioningManager.onClose()
        appSettingsRepository.onClose()
    }


}