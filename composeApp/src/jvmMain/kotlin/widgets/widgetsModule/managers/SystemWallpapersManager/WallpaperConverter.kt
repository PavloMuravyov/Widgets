package widgets.widgetsModule.managers.SystemWallpapersManager

import widgets.domain.JNA.useCases.PictureOption
import widgets.widgetsModule.managers.SystemDataManager.model.ColorScheme
import org.im4java.core.ConvertCmd
import org.im4java.core.IMOperation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.im4java.process.Pipe
import org.jetbrains.skia.Rect
import org.jetbrains.skia.Surface
import org.jetbrains.skia.makeFromFileName
import java.io.ByteArrayOutputStream
import java.io.File
import org.jetbrains.skia.Image as SkiaImage




suspend fun resizeAndCropWallpaper(
    inputPath: String,
    screenWidth: Int,
    screenHeight: Int,
    wingpanelHeight: Int,
    pictureOption: PictureOption,
): Result<SkiaImage> = withContext(Dispatchers.IO) {

    runCatching {

        val tempFile = kotlin.io.path.createTempFile(
            suffix = ".webp"
        ).toFile()

        val op = IMOperation().apply {

            addImage(inputPath)

            when (pictureOption) {

                PictureOption.ZOOM -> {
                    resize(screenWidth, screenHeight, "^")
                    gravity("Center")
                    extent(screenWidth, screenHeight)
                }

                PictureOption.SCALED -> {
                    resize(screenWidth, screenHeight)
                    gravity("Center")
                    extent(screenWidth, screenHeight)
                }

                PictureOption.CENTERED -> {
                    gravity("Center")
                    extent(screenWidth, screenHeight)
                }

                else -> {   }
            }
            addRawArgs("-crop", "${screenWidth}x${screenHeight}+0+${wingpanelHeight}")
            addRawArgs("+repage")
            quality(20.0)

            addImage("webp:${tempFile.absolutePath}")
        }

        ConvertCmd().run(op)


        val data = org.jetbrains.skia.Data.makeFromFileName(
            tempFile.absolutePath
        )

        val image = SkiaImage.makeFromEncoded(data.bytes)

        data.close()
        tempFile.delete()

        image
    }
}

