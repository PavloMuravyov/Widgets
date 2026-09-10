package widgets.domain.SocketManager

import widgets.widgetsModule.widgetsManager.WidgetsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.File
import java.net.StandardProtocolFamily
import java.net.UnixDomainSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousCloseException
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import kotlin.system.exitProcess

class SocketManager (
    private val widgetsManager: WidgetsManager,
    private val socketPath: String = "/tmp/ElementaryWidgets.sock",

    ) {

    @Volatile
    private var serverChannel: ServerSocketChannel? = null
    private val address = UnixDomainSocketAddress.of(socketPath)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        scope.launch {
            if(isInstanceRunning()){
                exitProcess(0)
            } else {
                startServer{
                    widgetsManager.updateShowConfigWindow(true)
                }
            }
        }
    }


    fun startServer(onShowUIRequest: () -> Unit) {
        stopServer()
        File(socketPath).delete()
        val server = ServerSocketChannel.open(StandardProtocolFamily.UNIX)
        server.bind(address)

        serverChannel = server

        Runtime.getRuntime().addShutdownHook(Thread { stopServer() })

        scope.launch(Dispatchers.IO) {
            try {
                while (isActive) {
                    val client = server.accept()
                    handleClientConnection(client, onShowUIRequest)
                }
            } catch (e: AsynchronousCloseException) {
                println("Server socket closed intentionally")
            } catch (e: Exception) {
                if (isActive) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun handleClientConnection(
        client: SocketChannel,
        onShowConfigWindowRequest: () -> Unit
    ) {
        scope.launch(Dispatchers.IO) {
            client.use {
                val buffer = ByteBuffer.allocate(BUFFER_SIZE)
                val bytesRead = it.read(buffer)
                if (bytesRead > 0) {
                    val command = String(buffer.array(), 0, bytesRead).trim()
                    if (command == COMMAND_SHOW_UI) {
                        withContext(Dispatchers.IO) {
                            onShowConfigWindowRequest()
                        }
                    }
                }
            }
        }
    }

    fun isInstanceRunning(): Boolean {
        return try {
            SocketChannel.open(StandardProtocolFamily.UNIX).use { client ->
                client.connect(address)
                client.write(ByteBuffer.wrap(COMMAND_SHOW_UI.toByteArray()))
            }
            true
        } catch (ex: IOException) {
            false
        }
    }


    companion object {

        /** Command sent from secondary instance to request UI display */
        private const val COMMAND_SHOW_UI = "showUi"

        /** Buffer size for reading socket commands */
        private const val BUFFER_SIZE = 256
    }


    fun stopServer() {
        serverChannel?.close()
        File(socketPath).delete()
    }



}