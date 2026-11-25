package widgets.widgetsModule.widgets.photos.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.AwtWindow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isPrimary
import androidx.compose.ui.input.pointer.isPrimaryPressed
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.isTertiaryPressed
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.mediaQuery
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.liquefiable
import io.github.fletchmckee.liquid.liquid
import io.github.fletchmckee.liquid.rememberLiquidState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import widgets.Theme.Colors
import widgets.Theme.Paddings
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.file_picker_empty_error
import widgets.composeapp.generated.resources.file_picker_error
import widgets.composeapp.generated.resources.file_picker_hint
import widgets.widgetsModule.data.models.WidgetSizes
import widgets.widgetsModule.data.models.roundedShape
import widgets.widgetsModule.widgets.photos.PhotosDimens
import widgets.widgetsModule.widgets.photos.PhotosViewModel
import widgets.widgetsModule.widgets.widgetsWindow.Configurator.ui.extentions.animatedGradientBorder
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun PhotosView(photosViewModel: PhotosViewModel) {

    val liquidState = rememberLiquidState()

    val showSettings = remember { mutableStateOf(false) }


    Box(
        Modifier
            .fillMaxSize().padding(Paddings.small),
        contentAlignment = Alignment.Center
    ) {

        val currentUrl by photosViewModel.selectedURL.collectAsState()


        currentUrl?.let { it ->
            PhotoView(it, liquidState) {
                showSettings.value  = it
            }
        }


        val currentDir by photosViewModel.photosDirectory.collectAsState()


        if (showSettings.value || (currentUrl == null && currentDir == null)) {

            PhotosSettings(liquidState, photosViewModel)

        }
    }

}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun PhotoView(currentUrl: String, liquidState: LiquidState, onFocusedChange: (Boolean) -> Unit) {


    val roundedShape = WidgetSizes.Large.roundedShape.value.dp - Paddings.small

    AnimatedContent(
        targetState = currentUrl,
        transitionSpec = {
            (slideInHorizontally(
                animationSpec = tween(1500),
                initialOffsetX = { fullWidth -> fullWidth }
            ) + fadeIn(tween(1500)))
                .togetherWith(
                    slideOutHorizontally(
                        animationSpec = tween(1500),
                        targetOffsetX = { fullWidth -> -fullWidth }
                    ) + fadeOut(tween(1500))
                )
        },
        label = "photoSwitch",


    ) { url ->

        println("RECOMPOSE")

        Box(
            Modifier
                .fillMaxSize()
                .onPointerEvent(PointerEventType.Press) { event ->
                    if (event.buttons.isTertiaryPressed) {
                        onFocusedChange(true)
                    }
                }
                .onPointerEvent(PointerEventType.Press){ event ->
                    if (event.buttons.isPrimaryPressed){
                        onFocusedChange(false)
                    }
                }
        ) {
            AsyncImage(
                model = url,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(roundedShape)
                    )
                    .liquefiable(liquidState)
                ,
                contentDescription = currentUrl,
                contentScale = ContentScale.Crop,
                filterQuality = FilterQuality.High,
            )

        }
    }
}


@Composable
private fun PhotosSettings(liquidState: LiquidState, viewModel: PhotosViewModel) {
    BoxWithConstraints(
        Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        val dimens = remember (constraints.maxHeight) {
            PhotosDimens.getDimens(maxHeight, maxHeight)
        }

        Column(Modifier.fillMaxHeight(0.6f)
            .fillMaxWidth(0.8f)
            .liquid(liquidState){
                frost = 8.dp
                shape = RoundedCornerShape(WidgetSizes.Large.roundedShape.value.dp - Paddings.medium)
                refraction = 0.6f
                curve = 0.0f
                edge = 0.02f
                saturation = 1.0f
                dispersion = 0.1f
                tint = Color.Black.copy(alpha = 0.4f)
            }
        ) {

            DirChanger(Modifier.weight(1.2f), viewModel, dimens)

            IntervalChanger(Modifier.weight(1f), viewModel, liquidState, dimens)

        }

    }
}

@Composable
fun IntervalChanger(modifier: Modifier, viewModel: PhotosViewModel, liquidState: LiquidState, dimens: PhotosDimens) {

    Column(
        modifier.padding(vertical = Paddings.medium, horizontal = Paddings.large),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Text("Photos changing interval:", color = Colors.solidWhite, fontSize = dimens.baseSettingsFontSize)

        PhotosSecondsCounter(Modifier.fillMaxWidth().height(dimens.buttonHeight).fillMaxHeight(0.7f), viewModel ,
            dimens.buttonTextSize)

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DirChanger(modifier: Modifier, viewModel: PhotosViewModel, dimens: PhotosDimens) {

    val directory by viewModel.photosDirectory.collectAsState()
    var showPicker by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    var isFocused by remember { mutableStateOf(false) }

        Column(
            modifier.padding(vertical = Paddings.medium, horizontal = Paddings.large),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Text("Photos directory:", color = Colors.solidWhite, fontSize = dimens.baseSettingsFontSize)
            Text("$directory", color = Colors.semiTransparentWhite, fontSize = dimens.smallSettingsFontSize)

            Row(
                Modifier.fillMaxWidth()
                    .height(dimens.buttonHeight)
                    .clip( RoundedCornerShape(WidgetSizes.Large.roundedShape.value.dp - (2 * Paddings.medium.value).dp))
                    .animatedGradientBorder(
                        false, 1.dp,
                        shape = RoundedCornerShape(WidgetSizes.Large.roundedShape.value.dp - (2 * Paddings.medium.value).dp)
                    )
                    .background(if(isFocused) Colors.semiTransparentWhite else Color.Transparent)

                    .padding(Paddings.small)
                    .clickable (
                        onClick = {
                            showPicker = true
                        },
                        interactionSource = interactionSource,
                        indication = null
                    )
                    .onPointerEvent(PointerEventType.Enter) {
                            isFocused = true

                    }
                    .onPointerEvent(PointerEventType.Exit) {
                        isFocused = false
                    }
                ,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {


                Icon(
                    Icons.Outlined.Folder,
                    contentDescription = null,
                    tint = Colors.solidWhite,
                )
                Spacer(Modifier.width(Paddings.medium))
                Text("Change directory", color = Colors.solidWhite, fontSize = dimens.buttonTextSize)

            }
        }

    if(showPicker){
        AddUrlView(
            onAddUrlChange = {viewModel.updatePhotosData(newDir = it)},
            onShowPickerChanged = {showPicker = it},
        )
    }


}



@Composable
private fun PhotosSecondsCounter(
    modifier: Modifier,
    viewModel: PhotosViewModel,
    textSize: TextUnit,

) {

    val seconds by viewModel.changeTimer.collectAsState()
    var secondsCounter by remember { mutableStateOf(seconds) }
    var textValue by remember(secondsCounter){ mutableStateOf(secondsCounter.toString()) }


    LaunchedEffect(secondsCounter){
        delay(500.milliseconds)
        viewModel.updatePhotosData(newTimer = secondsCounter)
    }


    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        RepeatingCounterIcon(Icons.Outlined.Remove) { secondsCounter-- }

        Box(
            Modifier
                .fillMaxHeight()
                .aspectRatio(2f),
            contentAlignment = Alignment.Center
        ) {

            BasicTextField(
                value = textValue,
                onValueChange = {newValue ->
                    val value  = newValue.filter { it.isDigit() }.take(3)
                    secondsCounter = if(value.isNotEmpty()) {
                        value.toInt()
                    } else {
                        1
                    }

                },
                modifier = Modifier.fillMaxSize(),
                textStyle = TextStyle(
                    color = Colors.solidWhite,
                    textAlign = TextAlign.Center,
                    fontSize = textSize
                ),
                singleLine = true,
                cursorBrush = SolidColor(Colors.semiTransparentWhite),
                decorationBox = { innerTextField ->

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        innerTextField()
                    }
                }
            )
        }


        RepeatingCounterIcon(Icons.Outlined.Add) { secondsCounter++ }
    }
}

@Composable
private fun RepeatingCounterIcon(
    icon: ImageVector,
    onStep: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    LaunchedEffect(isPressed) {
        if (isPressed) {
            onStep()
            delay(400.milliseconds)
            var intervalMs = 150L
            while (isActive) {
                onStep()
                delay(intervalMs.milliseconds)
                intervalMs = (intervalMs * 0.92f).toLong().coerceAtLeast(40L) // прискорення
            }
        }
    }

    Icon(
        icon,
        contentDescription = null,
        tint = Colors.solidWhite,
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f)
            .animatedGradientBorder(
                false,
                1.dp,
                shape = RoundedCornerShape(WidgetSizes.Medium.roundedShape.value.dp - (2 * Paddings.medium.value).dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {}
            )
    )
}


@Composable
private fun AddUrlView(onAddUrlChange: (String?) -> Unit, onShowPickerChanged: (Boolean) -> Unit) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()



    FilePicker { path, errorMessage ->
        onShowPickerChanged (false)
        if (path != null) {
            onAddUrlChange(path.absolutePath)
        }
        errorMessage?.let { msg ->
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = msg,
                    )
            }
        }
    }


    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier
            .clickable(
                onClick = { snackbarHostState.currentSnackbarData?.dismiss() }
            )
    )
}


@Composable
private fun FilePicker(
    onResult: (File?, errorMessage: String?) -> Unit
) {
    val filePickerHint = stringResource(Res.string.file_picker_hint)
    val filePickerError = stringResource(Res.string.file_picker_error)
    val filePickerEmptyError = stringResource(Res.string.file_picker_empty_error)

    AwtWindow(
        create = {
            object : FileDialog(null as Frame?, filePickerHint, LOAD) {
                override fun setVisible(value: Boolean) {
                    super.setVisible(value)
                    if (!value) return

                    // file == null означає, що користувач натиснув "Скасувати"
                    if (file == null) {
                        onResult(null, null)
                        return
                    }

                    val chosenDir = directory?.let { File(it) }

                    when {
                        chosenDir == null || !chosenDir.isDirectory ->
                            onResult(null, filePickerError)

                        chosenDir.listFiles().isNullOrEmpty() ->
                            onResult(null, filePickerEmptyError)

                        else -> onResult(chosenDir, null)
                    }
                }
            }.apply {
                directory = "/"
            }
        },
        dispose = FileDialog::dispose
    )
}




