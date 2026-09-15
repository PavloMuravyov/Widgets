package widgets.widgetsModule.widgets.appstime.ui.smallView

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import widgets.widgetsModule.widgets.appstime.AppsTimeViewModel
import widgets.widgetsModule.widgets.appstime.domain.model.AppData
import widgets.widgetsModule.widgets.appstime.domain.model.ScreenTimeDay
import widgets.widgetsModule.widgets.appstime.ui.AppsTimesDimens
import widgets.widgetsModule.widgets.appstime.ui.utils.composables.AppCard

@Composable
fun bottomContent(modifier: Modifier, appsTimeViewModel: AppsTimeViewModel) {


    BoxWithConstraints(modifier = modifier) {


        val dimens = remember(maxWidth, maxHeight) {
            AppsTimesDimens.calculate(maxWidth, maxHeight, appsTimeViewModel.widgetSize.value)
        }
        val todayAppsTime by appsTimeViewModel.todayAppsTime.collectAsState()


        AppsGridView(
            appsTime = todayAppsTime,
            spacerHeight = dimens.spacerHeight,
            textSize = dimens.textSize,
            textTimeSize = dimens.textTimeSize,
            cardHeight = dimens.cardHeight,
        )
    }

}


@Composable
private fun AppsGridView(
    appsTime: List<Pair<AppData, ScreenTimeDay>>,
    spacerHeight: Dp,
    textSize: TextUnit,
    textTimeSize: TextUnit,
    cardHeight: Dp,

) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp)
    ) {
        item { Box(modifier = Modifier.height(spacerHeight).fillMaxWidth()) }
        item { Spacer(Modifier.height(2.dp)) }

        items(
            items = appsTime,
            key = { it.first.appID }
        ) { appTime ->


            AppCard(
                appTime = appTime,
                textSize = textSize,
                textTimeSize = textTimeSize,
                modifier = Modifier.height(cardHeight)
            )
            Spacer(Modifier.height(2.dp))
        }
    }
}
