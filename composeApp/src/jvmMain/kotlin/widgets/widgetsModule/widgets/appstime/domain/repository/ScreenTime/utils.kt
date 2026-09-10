package widgets.widgetsModule.widgets.appstime.domain.repository.ScreenTime

import com.sun.jna.Pointer
import com.sun.jna.ptr.LongByReference
import com.sun.jna.ptr.NativeLongByReference
import com.sun.jna.ptr.PointerByReference
import java.time.Instant

fun Instant.epochMillisToUsec(): Long = toEpochMilli() * 1000

fun readField(journal: Pointer, field: String): String? {
    val dataRef = PointerByReference()
    val lenRef = NativeLongByReference()
    val rc = Libsystemd.INSTANCE.sd_journal_get_data(journal, field, dataRef, lenRef)
    if (rc < 0) return null
    val length = lenRef.value.toInt()
    val bytes = dataRef.value.getByteArray(0, length)
    return String(bytes, Charsets.UTF_8).substringAfter('=', "")
}

fun readRealtime(journal: Pointer): Instant? {
    val usecRef = LongByReference()
    if (Libsystemd.INSTANCE.sd_journal_get_realtime_usec(journal, usecRef) < 0) return null
    return Instant.ofEpochMilli(usecRef.value / 1000)
}



