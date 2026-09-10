package widgets.widgetsModule.widgets.celsius.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.PlatformParagraphStyle
import androidx.compose.ui.text.PlatformSpanStyle
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextDecorationLineStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import widgets.Theme.Colors

@Composable
fun TextLabel(
    textSize: TextUnit, text: String, color: Color = Colors.solidWhite,
    maxLines: Int = 1, overflow: TextOverflow = TextOverflow.Ellipsis,
    textAlign: TextAlign = TextAlign.Center, fontWeight: FontWeight = FontWeight.Medium

) {
    Text(
        text, fontSize = textSize, color = color,
        maxLines = maxLines, overflow = overflow,
        textAlign = textAlign,
        fontWeight = fontWeight,

    )
}


@Composable
fun NumericText(
    textSize: TextUnit,
    text: String,
    density: Density,
    color: Color = Colors.solidWhite,
    textAlign: TextAlign = TextAlign.Center,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val style = TextStyle(
        fontSize = textSize,
        color = color,
    )
    val measured = remember(text, textSize, color) {
        textMeasurer.measure(text, style)
    }
    val canvasWidth = with(density) { measured.size.width.toDp() }
    val canvasHeight = with(density) { measured.size.height.toDp() }

    Canvas(modifier = modifier.size(canvasWidth, canvasHeight)) {
        val offsetX = when (textAlign) {
            TextAlign.Center -> (size.width - measured.size.width) / 2f
            TextAlign.End, TextAlign.Right -> size.width - measured.size.width
            else -> 0f
        }
        drawText(
            textLayoutResult = measured,
            topLeft = Offset(offsetX, 0f)
        )
    }
}

