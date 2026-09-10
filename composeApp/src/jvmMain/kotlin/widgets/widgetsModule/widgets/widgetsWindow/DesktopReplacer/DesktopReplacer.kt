package widgets.widgetsModule.widgets.widgetsWindow.DesktopReplacer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.onPointerEvent
import widgets.Theme.Colors
import widgets.widgetsModule.data.models.BackgroundTypes
import widgets.widgetsModule.managers.SystemDataManager.SystemDataManager
import widgets.widgetsModule.managers.SystemDataManager.model.ColorScheme
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.liquefiable
import org.jetbrains.skia.Image
import org.jetbrains.skia.Paint
import org.jetbrains.skia.Rect
import java.awt.MouseInfo
import java.awt.Point

@OptIn(ExperimentalComposeUiApi::class)

@Composable
fun DesktopReplacer(
    systemDataManager: SystemDataManager,
    liquidState: LiquidState,
    onMouse3Click: (Point) -> Unit

) {

    val wallpaperUpdatedState by systemDataManager.wallpaperUpdated.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
            .onPointerEvent(PointerEventType.Press) { event ->
                val buttons = event.buttons
                if (buttons.isSecondaryPressed) {
                    val pos = getMouseAbsolutePosition()
                    onMouse3Click(pos)
                }
            }

    ) {

        key(wallpaperUpdatedState) {

            if (wallpaperUpdatedState) {

                val wallpaper = systemDataManager.systemWallpaper


                wallpaper.let { wallpaper ->
                            LiquidWallpaperContent(
                                wallpaper = wallpaper,
                                systemDataManager,
                                liquidState
                            )
                }
            }
        }
    }
}


private fun getMouseAbsolutePosition(): Point {
    return MouseInfo.getPointerInfo().location
}


@Composable
private fun LiquidWallpaperContent(
    wallpaper: Image?,
    systemDataManager: SystemDataManager,
    liquidState: LiquidState,
) {


    val paint = remember { Paint() }
    val darkOverlayPaint = remember { Paint().apply { color = 0x73000000 } }
    val emptyWallpaperOverlayPaint = remember { Paint().apply { color = Colors.transparentBlackOverlays.toArgb() } }
    val wallpaperLayer = rememberGraphicsLayer()


    WallpaperCanvas(
        wallpaper = wallpaper,
        systemDataManager = systemDataManager,
        liquidState = liquidState,
        paint = paint,
        darkOverlayPaint = darkOverlayPaint,
        wallpaperLayer = wallpaperLayer,
        emptyWallpaperOverlayPaint
    )

    DisposableEffect(Unit) {
        onDispose {
            darkOverlayPaint.close()
            paint.close()


        }
    }
}

@Composable
private fun WallpaperCanvas(
    wallpaper: Image?,
    systemDataManager: SystemDataManager,
    liquidState: LiquidState,
    paint: Paint,
    darkOverlayPaint: Paint,
    wallpaperLayer: GraphicsLayer,
    emptyWallpaperOverlayPaint: Paint,
) {

    val systemColorScheme by systemDataManager.systemColorScheme.collectAsState()

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { alpha = 0.0f }
            .liquefiable(liquidState)
            .drawWithContent {
                wallpaperLayer.record { this@drawWithContent.drawContent() }
                drawContent()
            }
    ) {

        if (wallpaper != null) {

            drawIntoCanvas {
                it.nativeCanvas.drawImage(
                    image = wallpaper,
                    left = 0f,
                    top = 0f,
                    paint = paint,
                )
                if (systemColorScheme == ColorScheme.PREFER_DARK) {
                    it.nativeCanvas.drawRect(Rect.makeWH(size.width, size.height), darkOverlayPaint)
                }
            }
        } else {
            drawIntoCanvas {
                it.nativeCanvas.drawRect(Rect.makeWH(size.width, size.height), emptyWallpaperOverlayPaint)
            }

        }


    }
}
