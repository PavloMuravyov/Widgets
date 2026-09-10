package widgets.widgetsModule.widgets.celsius.ui.Composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.TextUnit
import widgets.Theme.Colors
import widgets.widgetsModule.widgets.celsius.CelsiusViewModel

@Composable
fun WindArrow(data: CelsiusViewModel.WindData? = null, windDegree: Int? = null) {

    val degree = data?.windDegrees ?: windDegree

    degree?.let {
    Icon(
        Icons.Default.Navigation,
        contentDescription = "Wind icon",
        Modifier
            .fillMaxSize(0.8f)
            .graphicsLayer {
                rotationZ = (degree + 180).toFloat()
            },
        tint = Colors.solidWhite,

        )
    }
}