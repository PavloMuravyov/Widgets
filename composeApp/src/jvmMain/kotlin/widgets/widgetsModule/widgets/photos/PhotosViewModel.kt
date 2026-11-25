package widgets.widgetsModule.widgets.photos

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import widgets.domain.AppSettings.AppSettingsRepository
import widgets.domain.DensityProvider.DensityProvider
import widgets.widgetsModule.data.models.WidgetsTypes
import widgets.widgetsModule.managers.GridPositioningManager.GridPositioningManager
import widgets.widgetsModule.managers.SystemDataManager.SystemDataManager
import widgets.widgetsModule.widgets.WidgetBaseViewModel
import java.io.File
import java.nio.file.*
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds


@Serializable
data class PhotosData(
    val photosDirectory: String,
    val changeTimer: Int,
)
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class PhotosViewModel(
    systemDataManager: SystemDataManager,
    appSettingsRepository: AppSettingsRepository,
    gridPositioningManager: GridPositioningManager,
    densityProvider: DensityProvider,
    private val json: Json
    ) : WidgetBaseViewModel(
    WidgetsTypes.Photos,
    appSettingsRepository,
    gridPositioningManager,
    densityProvider,
    systemDataManager
) {


    private val _photosDataFlow = widgetData
        .map { rawJson ->
            if (rawJson.isNullOrBlank()) {
                null
            } else {
                try {
                    json.decodeFromString<PhotosData>(rawJson)
                } catch (e: Exception) {
                    null
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _photosDirectory = _photosDataFlow
        .map { it?.photosDirectory }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val photosDirectory: StateFlow<String?> = _photosDirectory


    private val _changeTimer: StateFlow<Int> = _photosDataFlow
        .map { it?.changeTimer ?: 30 }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 30)

    val changeTimer : StateFlow<Int> = _changeTimer


    private val _photosUrls = _photosDirectory
        .flatMapLatest { dir ->
            if (dir == null) flowOf(null)
            else {
                val path = Paths.get(dir)
                merge(flowOf(Unit), directoryChangesFlow(path))
                    .debounce(300.milliseconds)
                    .map { withContext(Dispatchers.IO) { loadPhotos(dir) } }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)



    private val _selectedURL = MutableStateFlow<String?>(null)
    val selectedURL: StateFlow<String?> = _selectedURL

    private var lastKnownIndex: Int = -1
    private var slideshowJob: Job? = null

    init {
        viewModelScope.launch {
            _photosDirectory.collect {
                _selectedURL.value = null
                lastKnownIndex = -1
            }
        }
        viewModelScope.launch {
            _photosUrls.collect { list ->
                if (_selectedURL.value == null && !list.isNullOrEmpty()) {
                    lastKnownIndex = 0
                    _selectedURL.value = list[0]
                }
            }
        }
        startSlideshow()
    }

    private fun startSlideshow() {
        slideshowJob?.cancel()
        slideshowJob = viewModelScope.launch {
            while (isActive) {
                val timer = _photosDataFlow.value?.changeTimer ?: 30
                delay(timer.seconds)
                advanceToNextPhoto()
            }
        }
    }

    private fun advanceToNextPhoto() {
        val list = _photosUrls.value.orEmpty()

        if (list.isEmpty()) {
            lastKnownIndex = -1
            _selectedURL.value = null
            return
        }

        val currentUrl = _selectedURL.value
        val foundIndex = currentUrl?.let { list.indexOf(it) } ?: -1

        val nextIndex = when {
            foundIndex >= 0 -> (foundIndex + 1) % list.size

            lastKnownIndex >= 0 -> lastKnownIndex.coerceIn(0, list.size - 1)

            else -> 0
        }

        lastKnownIndex = nextIndex
        _selectedURL.value = list[nextIndex]
    }




    fun updatePhotosData(newDir: String? = null, newTimer: Int? = null) {

        val current = _photosDataFlow.value ?: PhotosData(photosDirectory = "", changeTimer = 30)

        val newData = current.copy(
            photosDirectory = newDir ?: current.photosDirectory,
            changeTimer = newTimer ?: current.changeTimer
        )

        if (newData == current) return

        updateWidgetData(json.encodeToString(newData))
    }


   private fun loadPhotos(photosDirectory: String): List<String> {
        val directory = File(photosDirectory)
        val allowedExtensions = setOf("jpg", "jpeg", "png", "webp", "gif", "bmp")
        if (!directory.exists() || !directory.isDirectory) {
            return emptyList()
        }

        return directory.walkTopDown()
            .filter { it.isFile }
            .filter { file ->
                file.extension.lowercase() in allowedExtensions
            }
            .map { it.toURI().toURL().toString() }
            .toList()
    }



   private fun directoryChangesFlow(path: Path): Flow<Unit> = callbackFlow {
        val watchService = FileSystems.getDefault().newWatchService()
        val watchedDirs = mutableMapOf<Path, WatchKey>()

        fun registerAll(root: Path) {
            if (!Files.isDirectory(root)) return
            Files.walk(root).use { stream ->
                stream.filter { Files.isDirectory(it) }.forEach { dir ->
                    val key = dir.register(
                        watchService,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_MODIFY
                    )
                    watchedDirs[dir] = key
                }
            }
        }

        registerAll(path)

        val job = launch(Dispatchers.IO) {
            while (isActive) {
                val key = try {
                    watchService.take()
                } catch (e: ClosedWatchServiceException) {
                    break
                }

                key.pollEvents().forEach { event ->
                    if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                        val changed = (key.watchable() as Path).resolve(event.context() as Path)
                        if (Files.isDirectory(changed)) registerAll(changed)
                    }
                }
                key.reset()
                trySend(Unit)
            }
        }

        awaitClose {
            job.cancel()
            watchService.close()
        }
    }
        .flowOn(Dispatchers.IO)


    override fun onCleared() {
        super.onCleared()
    }
}