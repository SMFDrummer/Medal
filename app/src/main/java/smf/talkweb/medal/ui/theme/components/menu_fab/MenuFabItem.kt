package smf.talkweb.medal.ui.theme.components.menu_fab

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

class MenuFabItem(
    val icon: @Composable () -> Unit,
    val label: String,
    val srcIconColor: Color = Color.White,
    val labelTextColor: Color = Color.White,
    val labelBackgroundColor: Color = Color.Black.copy(alpha = 0.6F),
    val fabBackgroundColor: Color = Color.Unspecified,
)