package widgets.widgetsModule.widgets.photos

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Immutable
data class PhotosDimens(
    val baseSettingsFontSize: TextUnit = TextUnit.Unspecified,
    val smallSettingsFontSize: TextUnit = TextUnit.Unspecified,
    val buttonTextSize: TextUnit = TextUnit.Unspecified,
    val buttonHeight: Dp = Dp.Unspecified,
) {
    companion object {
        fun getDimens(  maxWidth: Dp,
                        maxHeight: Dp): PhotosDimens {
            return PhotosDimens(
                baseSettingsFontSize = (maxHeight * 0.06f).value.sp,
                smallSettingsFontSize = (maxHeight * 0.05f).value.sp,
                buttonTextSize = (maxHeight * 0.06f).value.sp,
                buttonHeight = (maxHeight * 0.1f),
            )
        }
    }
}