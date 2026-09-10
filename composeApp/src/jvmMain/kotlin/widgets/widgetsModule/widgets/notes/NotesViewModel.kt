package widgets.widgetsModule.widgets.notes

import androidx.lifecycle.viewModelScope
import widgets.domain.AppSettings.AppSettingsRepository
import widgets.domain.DensityProvider.DensityProvider
import widgets.widgetsModule.data.models.WidgetsTypes
import widgets.widgetsModule.managers.GridPositioningManager.GridPositioningManager
import widgets.widgetsModule.widgets.WidgetBaseViewModel
import widgets.widgetsModule.widgets.notes.data.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.json.Json



enum class NoteContentType { New, Created }

class NotesViewModel(
    appSettingsRepository: AppSettingsRepository,
    gridPositioningManager: GridPositioningManager,
    densityProvider: DensityProvider,
    private val json: Json,
) : WidgetBaseViewModel(

    WidgetsTypes.Notes,
    appSettingsRepository,
    gridPositioningManager,
    densityProvider,
) {

   private val data = widgetData


    val notes: StateFlow<List<Note>> = data
        .map { raw ->
            raw?.let {
                json.decodeFromString<List<Note>>(it)
                    .sortedByDescending { note -> note.createdAt }
            } ?: emptyList()
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    private val _contentType = MutableStateFlow(NoteContentType.Created)
    val contentType: StateFlow<NoteContentType> = _contentType

    fun setContentType(type: NoteContentType) {
        _contentType.value = type
    }

    init {
        notes
            .map { it.isEmpty() }
            .distinctUntilChanged()
            .onEach { isEmpty ->
                setContentType(if (isEmpty) NoteContentType.New else NoteContentType.Created)
            }
            .launchIn(viewModelScope)
    }



    fun addNote(note: Note) {
        val newData = notes.value + note
        updateWidgetData(json.encodeToString(newData))
    }

    fun removeNote(note: Note) {
        val newData = notes.value.filterNot { it.id == note.id }
        updateWidgetData(json.encodeToString(newData))
    }

    fun updateNote(note: Note) {
        val newData =  notes.value.map {
            if (it.id == note.id) it.copy(title = note.title) else it
        }
        updateWidgetData(json.encodeToString(newData))
    }





}