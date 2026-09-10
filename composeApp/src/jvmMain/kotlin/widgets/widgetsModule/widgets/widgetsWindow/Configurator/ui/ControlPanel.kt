package widgets.widgetsModule.widgets.widgetsWindow.Configurator

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import widgets.Theme.Colors
import widgets.Theme.Paddings
import widgets.widgetsModule.widgetsManager.WidgetsManager
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.liquid
import org.jetbrains.compose.resources.stringResource
import widgets.composeapp.generated.resources.Res
import widgets.composeapp.generated.resources.widgets_settings
import kotlin.system.exitProcess


@Composable
fun ControlPanel(
    modifier: Modifier = Modifier,
    liquidState: LiquidState,
    widgetsManager: WidgetsManager,
    dimens: ConfigDimens,
    onCloseClick: () -> Unit) {

    val isAnyWidgetEnabled by widgetsManager.enabledWidgetTypes.collectAsState()

    val settingsLabel = stringResource(Res.string.widgets_settings)

    Box(
        modifier
            .fillMaxWidth()
    ) {

        Box(Modifier.fillMaxHeight().aspectRatio(1f), contentAlignment = Alignment.Center) {
            Box(
                Modifier.fillMaxHeight(0.5f).aspectRatio(1f)
                    .clickable(onClick = {

                        onCloseClick()

                        if(isAnyWidgetEnabled.isEmpty()){
                            exitProcess(0)
                        }

                    })
                    .clip(CircleShape)
                    .liquid(liquidState) {
                        frost = 4.dp
                        refraction = 0.2f
                        shape = CircleShape
                        curve = 0.6f
                        saturation = 1.0f
                        edge = 0.02f
                        dispersion = 0.5f

                    }
                    .padding(Paddings.xtraSmall),
                contentAlignment = Alignment.Center) {

                Icon(Icons.Default.Close, contentDescription = "Close", tint = Colors.solidWhite)
            }
        }

        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(settingsLabel, fontSize = dimens.showLabelTextSize, color = Colors.solidWhite)
        }

    }

}