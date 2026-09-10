package widgets.widgetsModule.widgets.clock.Composables.mechanicalClock

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import widgets.Theme.Colors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.withContext
import org.jetbrains.skia.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes


@OptIn(FlowPreview::class)
@Composable
fun DrawClockDecorations() {
    var cachedImage by remember { mutableStateOf<Image?>(null) }
    var targetSize by remember { mutableStateOf(IntSize.Zero) }

    LaunchedEffect(Unit) {
        snapshotFlow { targetSize }
            .debounce(50.milliseconds)
            .collectLatest { size ->
                if (size.width < 1 || size.height < 1) return@collectLatest
                val newImage = withContext(Dispatchers.Default) {
                    renderClockFaceToSkiaImage(size.width, size.height)
                }
                val old = cachedImage
                cachedImage = newImage
                old?.close()
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { size -> targetSize = IntSize(size.width, size.height) }
            .drawWithContent {
                drawContent()
                cachedImage?.let { skiaImage ->
                    drawIntoCanvas { canvas -> canvas.nativeCanvas.drawImage(skiaImage, 0f, 0f) }
                }
            }
    )

    DisposableEffect(Unit) {
        onDispose { cachedImage?.close() }
    }
}


private fun renderClockFaceToSkiaImage(width: Int, height: Int): org.jetbrains.skia.Image {

    val surface = Surface.makeRasterN32Premul(width, height)
    return try {
        val canvas = surface.canvas
        val cx = width / 2f
        val cy = height / 2f
        val radius = minOf(width, height) * 0.45f

        drawClockFaceSkia(canvas, cx, cy, radius)
        drawMinuteMarksSkia(canvas, cx, cy, radius)
        drawHourNumbersSkia(canvas, cx, cy, radius)
        surface.makeImageSnapshot()
    } finally {
        surface.close()
    }
}

private fun drawClockFaceSkia(canvas: org.jetbrains.skia.Canvas, cx: Float, cy: Float, radius: Float) {
    val paint = org.jetbrains.skia.Paint().apply {
        color = Colors.semiTransparentWhite.toArgb()
        mode = PaintMode.STROKE
        strokeWidth = 2.2f
        isAntiAlias = true
    }
    canvas.drawCircle(cx, cy, radius, paint)
}

private fun drawMinuteMarksSkia(canvas: org.jetbrains.skia.Canvas, cx: Float, cy: Float, radius: Float) {
    val paint = org.jetbrains.skia.Paint().apply {
        color = Colors.semiTransparentWhite.toArgb()
        isAntiAlias = true
        strokeCap = PaintStrokeCap.ROUND
    }

    for (i in 0 until 60) {
        val angle = Math.toRadians((i * 6 - 90).toDouble())
        val isHour = i % 5 == 0
        val outerR = radius * 0.99f
        val innerR = if (isHour) radius * 0.96f else radius * 0.97f
        paint.strokeWidth = if (isHour) 3.0f else 2.0f

        canvas.drawLine(
            cx + outerR * cos(angle).toFloat(),
            cy + outerR * sin(angle).toFloat(),
            cx + innerR * cos(angle).toFloat(),
            cy + innerR * sin(angle).toFloat(),
            paint
        )
    }
}

private fun drawHourNumbersSkia(canvas: org.jetbrains.skia.Canvas, cx: Float, cy: Float, radius: Float) {
    val numberRadius = radius * 0.8f
    val font = Font(null, radius * 0.22f).apply {
        isSubpixel = true
    }
    val paint = org.jetbrains.skia.Paint().apply {
        color = Colors.solidWhite.toArgb()
        isAntiAlias = true
    }

    for (h in 1..12) {
        val angle = Math.toRadians((h * 30 - 90).toDouble())
        val nx = cx + numberRadius * cos(angle).toFloat()
        val ny = cy + numberRadius * sin(angle).toFloat()

        val text = h.toString()
        val textLine = TextLine.make(text, font)


        val tx = nx - textLine.width / 2f
        val ty = ny - (textLine.ascent + textLine.descent) / 2f

        canvas.drawTextLine(textLine, tx, ty, paint)
    }
}