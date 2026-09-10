package widgets.widgetsModule.widgets.appstime.ui.mediumView.appsTimes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import widgets.Theme.Paddings
import widgets.widgetsModule.widgets.appstime.AppsTimeViewModel
import widgets.widgetsModule.widgets.appstime.ui.AppsTimesDimens


@Composable
fun AppsTimeTableView(modifier: Modifier, appsTimeViewModel: AppsTimeViewModel){


    BoxWithConstraints(modifier = modifier
        .padding(Paddings.small) ){

        val dimens = remember(maxWidth, maxHeight) {
            AppsTimesDimens.calculate(maxWidth, maxHeight, appsTimeViewModel.widgetSize.value)
        }

        val appsTime by appsTimeViewModel.last7DaysAppsTime.collectAsState()

        AppsTimeTable(appsTime,
            dimens.timeTableTimeSize,
            dimens.timeTableCardWidth,
            dimens.timeTableCardHeight,
            dimens.timeTableIconSize)
    }
}