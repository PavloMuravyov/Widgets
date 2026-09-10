package widgets.widgetsModule.widgets.appstime.domain.repository.ScreenTime

import widgets.widgetsModule.widgets.appstime.domain.model.DailyMinutes
import widgets.widgetsModule.widgets.appstime.domain.model.ScreenTimeArchive
import widgets.widgetsModule.widgets.appstime.domain.model.TodayScreenTime
import com.sun.jna.NativeLong
import com.sun.jna.Pointer
import com.sun.jna.ptr.PointerByReference
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


private val SLEEP_ENTER_REGEX = Regex("""Performing sleep operation|Entering sleep state""")
private val SLEEP_EXIT_REGEX = Regex("""System returned from sleep""")
private val SLEEP_FAILED_REGEX = Regex("""Failed to put system to sleep""")

class ScreenTimeArchiveProvider() {

    fun getArchive(zone: ZoneId, now: Instant = Instant.now()) : ScreenTimeArchive{


        val today = now.atZone(zone).toLocalDate()
        val windowStart = today.minusDays(7).atStartOfDay(zone).toInstant()

        val bootIntervals =  readBootIntervals(windowStart, now)
        val bootMinutesByDay = intervalsToMinutesByDay(bootIntervals, zone)

        val sleepIntervals = readSleepIntervals(windowStart, now)
        val sleepMinutesByDay = intervalsToMinutesByDay(sleepIntervals, zone)



        fun screenMinutes(date: LocalDate): Long =
            maxOf(0L, (bootMinutesByDay[date] ?: 0L) - (sleepMinutesByDay[date] ?: 0L))


        val todayScreenTime = TodayScreenTime(today, screenMinutes(today))

        val archive = buildList {
            bootMinutesByDay.forEach { (date, _) ->
                if (date != today) {
                    add(DailyMinutes(date, screenMinutes(date)))
                }
            }
        }

        return ScreenTimeArchive(archive, todayScreenTime)
    }

}


private inline fun <T> withJournal(
    match: String,
    seek: (Pointer) -> Unit,
    block: (Pointer) -> T
): T {
    val journalRef = PointerByReference()
    check(Libsystemd.INSTANCE.sd_journal_open(journalRef, Libsystemd.SD_JOURNAL_LOCAL_ONLY) == 0)
    val journal = journalRef.value
    try {
        Libsystemd.INSTANCE.sd_journal_add_match(journal, match, NativeLong(0))
        seek(journal)
        return block(journal)
    } finally {
        Libsystemd.INSTANCE.sd_journal_close(journal)
    }
}

private fun sinceSeek(since: Instant): (Pointer) -> Unit =
    { journal -> Libsystemd.INSTANCE.sd_journal_seek_realtime_usec(journal, since.epochMillisToUsec()) }

private fun headSeek(): (Pointer) -> Unit =
    { journal -> Libsystemd.INSTANCE.sd_journal_seek_head(journal) }



private const val BOUNDARY_SLEEP_ID = "__boundary__"


private fun readSleepIntervals(since: Instant, now: Instant): List<SleepInterval> =
    withJournal("_COMM=systemd-sleep", sinceSeek(since)) { journal ->
        var asleepAtBoundary = false
        while (Libsystemd.INSTANCE.sd_journal_previous(journal) > 0) {
            val message = readField(journal, "MESSAGE") ?: continue
            when {
                SLEEP_ENTER_REGEX.containsMatchIn(message) -> { asleepAtBoundary = true; break }
                SLEEP_EXIT_REGEX.containsMatchIn(message) -> break
                SLEEP_FAILED_REGEX.containsMatchIn(message) -> break
            }
        }

        // повертаємо курсор на `since` для forward-проходу в тому ж handle
        Libsystemd.INSTANCE.sd_journal_seek_realtime_usec(journal, since.epochMillisToUsec())

        val activeSleeps = mutableMapOf<String, Instant>()
        if (asleepAtBoundary) activeSleeps[BOUNDARY_SLEEP_ID] = since
        val result = mutableListOf<SleepInterval>()

        while (Libsystemd.INSTANCE.sd_journal_next(journal) > 0) {
            val message = readField(journal, "MESSAGE") ?: continue
            val timestamp = readRealtime(journal) ?: continue
            val id = readField(journal, "_SYSTEMD_INVOCATION_ID")
                ?: readField(journal, "_PID")
                ?: continue

            when {
                SLEEP_ENTER_REGEX.containsMatchIn(message) ->
                    activeSleeps[id] = timestamp

                SLEEP_FAILED_REGEX.containsMatchIn(message) -> {
                    activeSleeps.remove(id)
                }

                SLEEP_EXIT_REGEX.containsMatchIn(message) -> {
                    val start = activeSleeps.remove(id)
                        ?: activeSleeps.remove(BOUNDARY_SLEEP_ID)
                        ?: continue
                    result += SleepInterval(id, start, timestamp)
                }
            }
        }

        activeSleeps.forEach { (id, start) -> result += SleepInterval(id, start, now) }
        result
}

private fun readBootIntervals(since: Instant, now: Instant): List<BootInterval> =
    withJournal("_PID=1", headSeek()) { journal ->
        val minByBoot = mutableMapOf<String, Instant>()
        val maxByBoot = mutableMapOf<String, Instant>()
        val order = mutableListOf<String>()

        while (Libsystemd.INSTANCE.sd_journal_next(journal) > 0) {
            val bootId = readField(journal, "_BOOT_ID") ?: continue
            val timestamp = readRealtime(journal) ?: continue

            if (bootId !in minByBoot) {
                minByBoot[bootId] = timestamp
                order += bootId
            }
            if (timestamp < minByBoot.getValue(bootId)) minByBoot[bootId] = timestamp
            if (timestamp > (maxByBoot[bootId] ?: Instant.MIN)) maxByBoot[bootId] = timestamp
        }

        if (order.isEmpty()) return@withJournal emptyList()

        val allIntervals = order
            .map { id -> BootInterval(id, minByBoot.getValue(id), maxByBoot.getValue(id)) }
            .sortedBy { it.start }

        val relevant = allIntervals.filter { it.end >= since }
        if (relevant.isEmpty()) return@withJournal emptyList()

        val result = relevant.toMutableList()
        result[0] = result[0].copy(start = maxOf(result[0].start, since))
        val lastIdx = result.lastIndex
        result[lastIdx] = result[lastIdx].copy(end = now)
        result
    }


private fun intervalsToMinutesByDay(
    bootIntervals: List<TimeInterval>,
    zone: ZoneId
): Map<LocalDate, Long> {
    val result = mutableMapOf<LocalDate, Long>()
    bootIntervals.forEach { boot ->
        var cursor = boot.start
        while (cursor < boot.end) {
            val zonedCursor = cursor.atZone(zone)
            val nextMidnight = zonedCursor.toLocalDate().plusDays(1).atStartOfDay(zone).toInstant()
            val segmentEnd = if (nextMidnight < boot.end) nextMidnight else boot.end
            val minutes = Duration.between(cursor, segmentEnd).toMinutes()
            result.merge(zonedCursor.toLocalDate(), minutes, Long::plus)
            cursor = segmentEnd
        }
    }
    return result
}

