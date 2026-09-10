package widgets.widgetsModule.widgets.appstime.domain.repository

import widgets.widgetsModule.widgets.appstime.domain.model.AppsTimeWidgetData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class AppsTimeStorageRepository (
    private val json: Json,
    private val scope: CoroutineScope,
    private val dataFile: File = defaultDataFile(),
) {
    val tmp = File(dataFile.parent, "${dataFile.name}.tmp")

    private val _appsTimeData = MutableStateFlow<AppsTimeWidgetData>(
        runBlocking(Dispatchers.IO) { load() })

    val appsTimeData: StateFlow<AppsTimeWidgetData> = _appsTimeData.asStateFlow()

    fun update(transform: (AppsTimeWidgetData) -> AppsTimeWidgetData) {
        scope.launch {
            _appsTimeData.update(transform)
        }
    }

    init {

        scope.launch {
            _appsTimeData
                .drop(1)
                .collect { persist(it) }
        }
    }

    private suspend fun persist(data: AppsTimeWidgetData) = withContext(Dispatchers.IO) {
        try {
            tmp.writeText(json.encodeToString(data))
            Files.move(
                tmp.toPath(),
                dataFile.toPath(),
                StandardCopyOption.ATOMIC_MOVE,
                StandardCopyOption.REPLACE_EXISTING
            )
        } catch (e: Exception) {
            System.err.println("Failed to save apps time data: ${e.message}")
        }
    }

    private fun load(): AppsTimeWidgetData  {
      return  runCatching {
            if (dataFile.exists()) json.decodeFromString<AppsTimeWidgetData>(dataFile.readText())
            else AppsTimeWidgetData()
        }.onFailure { e ->
            System.err.println("Failed to load apps time data: ${e.message}")
        }.getOrDefault(AppsTimeWidgetData())
    }

    fun onClose() {
        runBlocking(Dispatchers.IO) { persist(_appsTimeData.value) }
    }



    companion object {
        fun defaultDataFile(): File {
            val configDir = File(System.getenv("XDG_CONFIG_HOME")
                ?: "${System.getProperty("user.home")}/.config", "Widgets")
            configDir.mkdirs()
            return File(configDir, "appstimes.json")
        }
    }
}