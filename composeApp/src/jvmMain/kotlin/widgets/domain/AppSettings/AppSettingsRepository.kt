package widgets.domain.AppSettings

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.time.Duration.Companion.milliseconds

class AppSettingsRepository (
    private val json: Json,
    private val configFile: File = defaultConfigFile()
)  {

    private val scope = CoroutineScope( SupervisorJob()  )

    /**
     *
     * RunBlocking works only while app starting. NO OTHER CALLING
     * */
    private val _appSettings = MutableStateFlow<AppSettings>(
        runBlocking(Dispatchers.IO) { load() }
    )
    val appSettings: StateFlow<AppSettings> = _appSettings.asStateFlow()

    fun update(transform: (AppSettings) -> AppSettings) {
        scope.launch {
            _appSettings.update(transform)
        }
    }

    init {
        scope.launch {
            appSettings
                .drop(1)
                .collect { persist(it) }
        }
    }

    private suspend fun persist(settings: AppSettings) {
        withContext(Dispatchers.IO) {
            try {
                val tmp = File(configFile.parent, "${configFile.name}.tmp")
                tmp.writeText(json.encodeToString(settings))
                Files.move(
                    tmp.toPath(),
                    configFile.toPath(),
                    StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING
                )
            } catch (e: Exception) {
                System.err.println("Failed to save settings: ${e.message}")
            }
        }
    }


    private suspend fun load(): AppSettings = withContext(Dispatchers.IO) {
        try {
            if (configFile.exists()) json.decodeFromString(configFile.readText())
            else AppSettings()
        } catch (e: Exception) {
            AppSettings()
        }
    }


    fun onClose(){
        runBlocking(Dispatchers.IO) { persist(_appSettings.value) }
        _appSettings.value = AppSettings()
        scope.cancel()
    }

    companion object {
        fun defaultConfigFile(): File {
            val configDir = File(System.getenv("XDG_CONFIG_HOME")
                ?: "${System.getProperty("user.home")}/.config", "Widgets")
            configDir.mkdirs()
            return File(configDir, "settings.json")
        }
    }

}