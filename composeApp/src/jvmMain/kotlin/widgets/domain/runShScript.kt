package widgets.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.getResourceUri
import org.jetbrains.compose.resources.getString
import widgets.composeapp.generated.resources.Res
import java.io.File
import java.nio.file.Files
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

private const val HIDE_SCRIPT_PATH = "files/hide.sh"
private const val RESTART_SCRIPT_PATH = "files/restart.sh"

suspend fun runHideScript(windowName: String) {

    val tempScript = Files.createTempFile("hide_", ".sh").toFile()
    try {
        Res.readBytes(HIDE_SCRIPT_PATH).inputStream().use { input ->
            tempScript.outputStream().use { out ->

                input.copyTo(out)
            }
        }

        tempScript.setExecutable(true)

        val process = ProcessBuilder("bash", tempScript.absolutePath, windowName)
            .redirectErrorStream(true)
            .start()

        val finished = process.waitFor(30, TimeUnit.SECONDS)
        if (!finished) {
            process.destroyForcibly()
            error("hide.sh out of timeout")
        }

    } finally {
        tempScript.delete()
    }

}



suspend fun prepareRestartScript(): File {
    val tempScript = Files.createTempFile("restart_", ".sh").toFile()
    Res.readBytes(RESTART_SCRIPT_PATH).inputStream().use { input ->
        tempScript.outputStream().use { out ->
            input.copyTo(out)
        }
    }
    tempScript.setExecutable(true)
    return tempScript
}

fun launchDetachedScript(script: File): Result<Process?> {
  return  runCatching {
        ProcessBuilder(
            "bash", "-c",
            "setsid bash ${script.absolutePath} < /dev/null > /tmp/restart.log 2>&1 &"
        )
            .redirectInput(ProcessBuilder.Redirect.from(File("/dev/null")))
            .redirectOutput(ProcessBuilder.Redirect.to(File("/dev/null")))
            .redirectErrorStream(true)
            .start()
    }
}