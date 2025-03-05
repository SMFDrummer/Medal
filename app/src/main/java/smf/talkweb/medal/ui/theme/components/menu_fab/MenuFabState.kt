package smf.talkweb.medal.ui.theme.components.menu_fab

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Stable
class MenuFabState {
    val menuFabStateEnum: MutableState<MenuFabStateEnum> =
        mutableStateOf(MenuFabStateEnum.Collapsed)
}

@Composable
fun rememberMenuFabState() = remember { MenuFabState() }

enum class MenuFabStateEnum {
    Collapsed, Expanded
}