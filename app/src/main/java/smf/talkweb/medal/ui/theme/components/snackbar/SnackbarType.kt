package smf.talkweb.medal.ui.theme.components.snackbar

import androidx.compose.runtime.Composable
import smf.talkweb.medal.ui.theme.MedalTheme

enum class SnackbarType {
    Primary, Secondary, Tertiary, Error, Success, Warn,
}

@Composable
fun RenderSnackbar(type: SnackbarType, data: SnackbarData) = when (type) {
    SnackbarType.Primary -> Snackbar(data, containerColor = MedalTheme.colors.primary)
    SnackbarType.Secondary -> Snackbar(data, containerColor = MedalTheme.colors.secondary)
    SnackbarType.Tertiary -> Snackbar(data, containerColor = MedalTheme.colors.tertiary)
    SnackbarType.Error -> Snackbar(data, containerColor = MedalTheme.colors.error)
    SnackbarType.Success -> Snackbar(data, containerColor = MedalTheme.colors.success)
    SnackbarType.Warn -> Snackbar(data, containerColor = MedalTheme.colors.warn)
}

data class Toast(
    val message: String? = null,
    val actionLabel: String? = null,
    val withDismissAction: Boolean = false,
)