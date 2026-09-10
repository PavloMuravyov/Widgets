package widgets.widgetsModule.widgetsManager.Utils.BlurSurface

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import org.jetbrains.skia.Bitmap

private val canvasDrawScope = CanvasDrawScope()

fun GraphicsLayer.toSkiaBitmapSampled(sampleEvery: Int = 10): Bitmap {
    val w = size.width
    val h = size.height
    val sampledW = maxOf(1, w / sampleEvery)
    val sampledH = maxOf(1, h / sampleEvery)

    val bitmap = Bitmap()
    bitmap.allocN32Pixels(sampledW, sampledH, false)

    canvasDrawScope.draw(
        density = Density(1f),
        layoutDirection = LayoutDirection.Ltr,
        canvas = Canvas(bitmap.asComposeImageBitmap()),
        size = Size(sampledW.toFloat(), sampledH.toFloat())
    ) {
        drawLayer(this@toSkiaBitmapSampled)
    }


    return bitmap
}