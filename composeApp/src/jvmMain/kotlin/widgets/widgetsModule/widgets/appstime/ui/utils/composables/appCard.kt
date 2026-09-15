package widgets.widgetsModule.widgets.appstime.ui.utils.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import widgets.Theme.Colors
import widgets.widgetsModule.widgets.appstime.domain.model.AppData
import widgets.widgetsModule.widgets.appstime.domain.model.ScreenTimeDay


@Composable
fun AppCard(appTime: Pair<AppData, ScreenTimeDay?>, textSize: TextUnit, textTimeSize: TextUnit, modifier: Modifier) {


    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){


        appIconView(modifier.weight(1f), appTime.first.appIconUrl)

        appTimeView(Modifier.weight(2.5f), appTime.first.appName, textSize, textTimeSize, appTime.second )
    }
}


@Composable
fun appIconView(
    modifier: Modifier,
    appIconUrl: String,
    padding: Dp = 4.dp,
    iconSize: Dp = 24.dp,
) {
    Box(modifier.padding(padding), contentAlignment = Alignment.Center) {
        AsyncImage(
            model = appIconUrl,
            contentDescription = appIconUrl,
            modifier = Modifier.height(iconSize),
            filterQuality = FilterQuality.Low,
        )
    }
}


@Composable
private fun appTimeView(modifier: Modifier, appName: String, textSize: TextUnit, textTimeSize: TextUnit, appTimeTotal: ScreenTimeDay?) {
    Column (modifier
        .fillMaxHeight(),
        verticalArrangement = Arrangement.Center) {

        appNameView(appName, textSize)

        TimeText(appTimeTotal, textSize = textTimeSize)

    }
}

@Composable
private fun appNameView(appName: String, textSize: TextUnit) {
    Text(
        appName, color = Colors.solidWhite, fontSize = textSize,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
        )

}
