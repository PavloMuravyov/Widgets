package widgets.domain.JNA

class GLibMainLoopService(
    private val glib: GLib
) {

    @Volatile private var started = false

    fun start() {
        if (started) return
        started = true

        Thread {
            val loop = glib.g_main_loop_new(null, false)
            glib.g_main_loop_run(loop)
        }.apply {
            isDaemon = true
            start()
        }
    }
}