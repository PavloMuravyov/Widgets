package widgets.domain.JNA

import com.sun.jna.*

interface Gio : Library {
    fun g_settings_new(schema: String): Pointer
    fun g_settings_get_string(settings: Pointer, key: String): Pointer


}

interface GObject : Library {
    fun g_signal_connect_data(
        instance: Pointer,
        detailed_signal: String,
        c_handler: Callback,
        data: Pointer?,
        destroy_data: Pointer?,
        connect_flags: Int
    ): Long
    fun g_signal_handler_disconnect(instance: Pointer, handlerId: Long) // ← додати

    fun g_object_unref(obj: Pointer)

}

interface GLib : Library {
    fun g_free(ptr: Pointer)
    fun g_main_loop_new(context: Pointer?, is_running: Boolean): Pointer
    fun g_main_loop_run(loop: Pointer)

    fun g_main_loop_unref(loop: Pointer)

}


interface SettingsChangedCallback : Callback {
    fun invoke(settings: Pointer, key: String, userData: Pointer?)
}


interface GtkLibrary : Library {
    fun gtk_init(argc: Int, argv: Pointer?)
    fun gtk_icon_theme_get_default(): Pointer
    fun gtk_icon_theme_lookup_icon(
        icon_theme: Pointer,
        icon_name: String,
        size: Int,
        flags: Int
    ): Pointer?
    fun gtk_icon_info_get_filename(icon_info: Pointer): String?
    fun g_object_unref(obj: Pointer)
}
