package widgets.widgetsModule.widgets.appstime.ui.mediumView.appsTimes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import widgets.Theme.Colors
import widgets.widgetsModule.widgets.appstime.domain.model.AppData
import widgets.widgetsModule.widgets.appstime.ui.utils.composables.appIconView
import widgets.widgetsModule.widgets.appstime.ui.utils.utils.toFormattedTime
import java.io.File


@Composable
fun AppsTimeTable(
    appsTime: List<Pair<AppData, Int>>,
    textTimeSize: TextUnit,
    cardWidth: Dp,
    cardHeight: Dp,
    iconHeight: Dp
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
          0.dp
        )
    ) {


        items(
            items = appsTime,
            key = { it.first.appID }
        ) { item ->

                AppsMinimalCard(
                    item,
                    textTimeSize,
                    Modifier.width(cardWidth).height(cardHeight),
                    iconHeight
                )
            }

    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AppsMinimalCard(
    appTime: Pair<AppData, Int?>,
    textTimeSize: TextUnit,
    modifier: Modifier,
    iconHeight: Dp
) {

    Row(
            modifier ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            appIconView(Modifier.weight(1f), appTime.first.appIconUrl, padding = 0.dp, iconHeight)
            Box(
                Modifier.weight(1.5f)
                    .padding(start = 2.dp, top = 2.dp, end = 2.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    appTime.second?.toFormattedTime(multiline = true) ?: "",
                    fontSize = textTimeSize,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Colors.semiTransparentWhite,
                )
            }

    }
}
