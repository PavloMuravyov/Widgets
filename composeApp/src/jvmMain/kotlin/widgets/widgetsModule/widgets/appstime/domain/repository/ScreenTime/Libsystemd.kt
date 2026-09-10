package widgets.widgetsModule.widgets.appstime.domain.repository.ScreenTime

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.NativeLong
import com.sun.jna.Pointer
import com.sun.jna.ptr.LongByReference
import com.sun.jna.ptr.NativeLongByReference
import com.sun.jna.ptr.PointerByReference
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

interface Libsystemd : Library {
    fun sd_journal_open(ret: PointerByReference, flags: Int): Int
    fun sd_journal_close(j: Pointer)
    fun sd_journal_add_match(j: Pointer, data: String, size: NativeLong): Int
    fun sd_journal_seek_realtime_usec(j: Pointer, usec: Long): Int
    fun sd_journal_next(j: Pointer): Int
    fun sd_journal_get_data(j: Pointer, field: String, data: PointerByReference, length: NativeLongByReference): Int
    fun sd_journal_get_realtime_usec(j: Pointer, usec: LongByReference): Int
    fun sd_journal_previous(journal: Pointer) : Int
    fun sd_journal_seek_head(journal: Pointer) : Int

    companion object {
        val INSTANCE: Libsystemd = Native.load("systemd", Libsystemd::class.java)
        const val SD_JOURNAL_LOCAL_ONLY = 1
    }
}






