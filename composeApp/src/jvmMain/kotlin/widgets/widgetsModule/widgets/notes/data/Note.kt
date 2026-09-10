package widgets.widgetsModule.widgets.notes.data

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Note(
    val title: String,
    val id: String = UUID.randomUUID().toString(),
    val createdAt: Long? = System.currentTimeMillis(),
)



