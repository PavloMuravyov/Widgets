package widgets.widgetsModule.widgetsManager.Utils.BlurSurface

import org.jetbrains.skia.Bitmap

fun Bitmap.needsDarkOverlay(
    threshold: Float = 0.3f
): Boolean {
    val w = width
    val h = height
    if (w == 0 || h == 0) return false


    val pixmap = peekPixels() ?: return false


    var luminanceSum = 0.0
    var count = 0

    for (y in 0 until h ) {
        for (x in 0 until w) {
            val argb = pixmap.getColor(x, y)
            val r = ((argb shr 16) and 0xFF) / 255f
            val g = ((argb shr 8) and 0xFF) / 255f
            val b = (argb and 0xFF) / 255f

            luminanceSum += 0.2126 * r + 0.7152 * g + 0.0722 * b
            count++
        }
    }

    val result = if (count > 0) (luminanceSum / count) > threshold else false

    return result
}