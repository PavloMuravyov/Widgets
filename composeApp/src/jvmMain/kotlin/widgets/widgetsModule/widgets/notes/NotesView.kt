package widgets.widgetsModule.widgets.notes

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import widgets.Theme.Colors
import widgets.Theme.Paddings
import widgets.widgetsModule.data.models.WidgetSizes
import widgets.widgetsModule.data.models.roundedShape
import widgets.widgetsModule.widgets.notes.data.Note
import widgets.widgetsModule.widgets.notes.ui.ControlPanel
import widgets.widgetsModule.widgets.notes.ui.ControlPanelIcon
import widgets.widgetsModule.widgets.notes.ui.ScaleIndication
import widgets.widgetsModule.widgetsManager.Utils.BlurSurface.blurFromLayer
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.CapturedLayerState
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.note_hint
import widgets.composeapp.generated.resources.save
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun NotesView(notesViewModel: NotesViewModel, capturedLayerState: CapturedLayerState) {


    Box(Modifier.fillMaxSize()) {

        val widgetSize by notesViewModel.widgetSize.collectAsState()

        when (widgetSize) {
            WidgetSizes.Small -> NoteContent(notesViewModel, capturedLayerState)
            WidgetSizes.Medium -> {NoteContent(notesViewModel, capturedLayerState)}
            WidgetSizes.Large -> {NoteContent(notesViewModel, capturedLayerState)}
        }
    }
}


@Composable
fun NoteContent(notesViewModel: NotesViewModel, capturedLayerState: CapturedLayerState){
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
            .padding(Paddings.medium),
        contentAlignment = Alignment.Center
    ) {

        val dimens = remember(maxWidth, maxHeight) {
            NotesDimens.calculate(maxWidth, maxHeight, notesViewModel.widgetSize.value)
        }


        Column() {


            val currentNoteContentType by notesViewModel.contentType.collectAsState()
            val onContentTypeChanged = { newType: NoteContentType -> notesViewModel.setContentType(newType) }


            ControlPanel(Modifier.weight(1f), dimens.controlPanelIconHeight, onContentTypeChanged)
            NoteContent(
                Modifier.weight(dimens.contentProportion),
                notesViewModel,
                dimens,
                capturedLayerState,
                currentNoteContentType,
                onContentTypeChanged
            )
        }


    }
}



@Composable
fun NoteContent(
    modifier: Modifier,
    notesViewModel: NotesViewModel,
    dimens: NotesDimens,
    capturedLayerState: CapturedLayerState,
    currentNoteContentType: NoteContentType,
    onContentTypeChanged: (NoteContentType) -> Unit
) {
    Box(modifier.fillMaxSize()) {


        Box(
            Modifier.fillMaxSize()
                .clip(RoundedCornerShape(notesViewModel.widgetSize.value.roundedShape - Paddings.medium))
                .blurFromLayer(capturedLayerState, needBlurEffect = true, needDarkOverlay = true)
        ) {

        }
        AnimatedContent(
            targetState = currentNoteContentType,
            modifier = Modifier.fillMaxSize(),
            transitionSpec = {
                if (targetState == NoteContentType.Created) {
                    slideInHorizontally { -it } + fadeIn() togetherWith
                            slideOutHorizontally { it } + fadeOut()
                } else {
                    slideInHorizontally { it } + fadeIn() togetherWith
                            slideOutHorizontally { -it } + fadeOut()

                }
            },
            label = "NoteContentAnimation"
        ) { contentType ->

            when (contentType) {
                NoteContentType.New -> NewNoteCreator(notesViewModel, dimens, onContentTypeChanged)
                NoteContentType.Created -> NotesListView(notesViewModel, dimens)
            }

        }
    }

}


@Composable
fun NotesListView(
    notesViewModel: NotesViewModel,
    dimens: NotesDimens
) {

    val notesList by notesViewModel.notes.collectAsState()
    val deleteIcon = Icons.Default.Delete

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(Paddings.small),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        itemsIndexed(notesList, key = { _, note -> note.id }) { index, note ->
            NoteCard(
                note,
                dimens.baseInfoTextSize,
                dimens.deleteIconHeight,
                deleteIcon,
                onChange = { changedNote ->
                    notesViewModel.updateNote(changedNote)
                }, onDelete = { removedNote ->
                    notesViewModel.removeNote(removedNote)
                }
            )

            if (index != notesList.lastIndex) {
                Divider(
                    Modifier
                        .padding(vertical = Paddings.small)
                        .fillMaxWidth(),
                    thickness = 1.dp,
                    color = Colors.transparentWhite
                )
            }

        }
    }

}

@Composable
fun NoteCard(
    note: Note,
    textSize: TextUnit,
    deleteIconHeight: Dp,
    deleteIcon: ImageVector,
    onChange: (Note) -> Unit,
    onDelete: (Note) -> Unit
) {

    var textValue by remember { mutableStateOf(note.title) }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        BasicTextField(
            value = textValue,
            onValueChange = { textValue = it },
            textStyle = TextStyle(fontSize = textSize, color = Colors.solidWhite, textAlign = TextAlign.Start),
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        onChange(note.copy(title = textValue))
                    }
                },
            cursorBrush = SolidColor(Colors.solidWhite)
        )


        ControlPanelIcon(
            deleteIcon, deleteIconHeight, onClick = { onDelete(note) },
            Modifier
                .height(deleteIconHeight)
                .wrapContentWidth()
        )

        LaunchedEffect(textValue) {
            if (textValue == note.title) return@LaunchedEffect
            delay(2000.milliseconds)
            onChange(note.copy(title = textValue))
        }
    }
}


@Composable
fun NewNoteCreator(
    notesViewModel: NotesViewModel,
    dimens: NotesDimens,
    onContentTypeChanged: (NoteContentType) -> Unit
) {


    var textValue by remember { mutableStateOf("") }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Paddings.medium),
        horizontalAlignment = Alignment.Start,
    ) {
        BasicTextField(
            value = textValue,
            onValueChange = { textValue = it },
            Modifier.weight(1f),

            textStyle = TextStyle(
                fontSize = dimens.largeInfoTextSize,
                color = Colors.solidWhite,
                textAlign = TextAlign.Start
            ),
            cursorBrush = SolidColor(Colors.solidWhite),
            decorationBox = { innerTextField ->
                TextFieldTint(
                    innerTextField,
                    textValue,
                    dimens.baseInfoTextSize
                )
            }
        )


        AnimatedVisibility(
            visible = textValue.isNotEmpty(),
            enter = fadeIn(tween(300)) + slideInVertically(tween(300)) { it },
            exit = fadeOut(tween(200)) + slideOutVertically(tween(200)) { it },
        ) {


            SaveLabel(
                dimens.controlPanelIconHeight,
                dimens.baseInfoTextSize,
                onClick = {
                    notesViewModel.addNote(Note(title = textValue))
                    onContentTypeChanged(NoteContentType.Created)
                }
            )
        }
    }


}

@Composable
fun TextFieldTint(
    innerTextField: @Composable () -> Unit,
    textValue: String,
    textSize: TextUnit,
) {

    val noteHint = stringResource(Res.string.note_hint)
    Box (Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {


        if (textValue.isEmpty()) {
            Text(
                text = noteHint,
                style = TextStyle(
                    fontSize = textSize,
                    color = Colors.solidWhite.copy(alpha = 0.4f),
                    textAlign = TextAlign.Start
                )
            )
        }
        innerTextField()
    }
}


@Composable
fun SaveLabel(
    iconHeight: Dp,
    fontSize: TextUnit,
    onClick: () -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }

    val saveLabel = stringResource(Res.string.save)

    Row(
        Modifier.fillMaxWidth()
            .clickable(
                onClick = { onClick() },
                interactionSource = interactionSource,
                indication = ScaleIndication(targetScale = 1.1f, durationMillis = 75)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {


        Text(saveLabel, fontSize = fontSize, color = Colors.solidWhite)
        Icon(Icons.Default.Save, saveLabel, Modifier.height(iconHeight), tint = Colors.solidWhite)
    }
}



