package widgets.widgetsModule.managers.SystemWallpapersManager

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.skia.Image as SkiaImage

import org.jetbrains.skia.Data
import org.jetbrains.skia.makeFromFileName
import java.io.File


class WallpaperHolder {
    @Volatile
    private var current: SkiaImage? = null

    fun update(new: SkiaImage): SkiaImage {
        val old = current
        current = new
        old?.close()
        return new
    }

    fun clear() {
        current?.close()
        current = null
    }
}

/*
class WallpaperHolder {
    @Volatile
    private var current: SkiaImage? = null

    fun load(bytes: ByteArray): SkiaImage {
        val old = current
        val new = SkiaImage.makeFromEncoded(bytes)
        current = new
        old?.close()  // ← явно закриваємо старий об'єкт
        return new
    }

    fun clear() {
        current?.close()
        current = null
    }
}*/
