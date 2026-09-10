package widgets.domain.AutostartManager

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption


class AutostartManager {

    private val scope = CoroutineScope(
        Dispatchers.IO +
                SupervisorJob() +
                CoroutineExceptionHandler { _, exception ->
                    exception.printStackTrace()
                }
    )

    private val desktopFileName = "widgets-widgets.desktop"
    private val autostartDir = File(System.getProperty("user.home"), ".config/autostart")
    private val desktopFile = File(autostartDir, desktopFileName)

    private val _autostartEnabled = MutableStateFlow(false)
    val autostartEnabled: StateFlow<Boolean> = _autostartEnabled.asStateFlow()



    init {
        scope.launch {
            _autostartEnabled.value = desktopFile.exists()
        }
    }


    private fun enableAutostart() {
        val source = File("/usr/share/applications/$desktopFileName")
        if (!autostartDir.exists()) {
            autostartDir.mkdirs()
        }
        Files.copy(
            source.toPath(),
            desktopFile.toPath(),
            StandardCopyOption.REPLACE_EXISTING
        )
    }

    private fun disableAutostart() {
        if (desktopFile.exists()) {
            desktopFile.delete()
        }
    }



    fun updateAutostartState(enabled: Boolean) {
        scope.launch {
            try {
                if (enabled) {
                    enableAutostart()
                } else {
                    disableAutostart()
                }
                _autostartEnabled.value = desktopFile.exists()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun cleanup() {
        scope.cancel()
    }
}