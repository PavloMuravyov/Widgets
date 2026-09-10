package widgets.domain.JNA

// GSettingsMonitor.kt — абстрактний базовий клас
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import com.sun.jna.*


abstract class GSettingsMonitor<T>(
    private val systemContext: SystemContext,
    private val schema: String,
    private val key: String
) {

    protected val gio get() = systemContext.gio
    protected val glib get() = systemContext.glib
    private val gobject get() = systemContext.gobject
    protected val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    protected val settings: Pointer = gio.g_settings_new(schema)

    private val _state = MutableStateFlow<T?>(null)
    val state: StateFlow<T?> = _state

    private val callback = object : SettingsChangedCallback {
        override fun invoke(settingsPtr: Pointer, changedKey: String, userData: Pointer?) {
            if (changedKey == key) {
                val value = readValue(settingsPtr)
                scope.launch {
                    _state.emit(value)
                }
            }
        }
    }

    private var handlerId: Long = 0L

    fun start() {
        handlerId =  gobject.g_signal_connect_data(
            settings,
            "changed",
            callback,
            null,
            null,
            0
        )

        // initial value
        scope.launch {
            _state.emit(readValue(settings))
        }
    }

    fun stop() {
        if (handlerId != 0L) {
            gobject.g_signal_handler_disconnect(settings, handlerId)
            handlerId = 0L
        }
        scope.cancel()
        gobject.g_object_unref(settings)
    }

    protected abstract fun readValue(settings: Pointer): T

    protected fun readString(settingsPtr: Pointer, key: String): String {
        val ptr = gio.g_settings_get_string(settingsPtr, key)
        return try {
            ptr.getString(0)

        } finally {
            glib.g_free(ptr)
        }
    }
}