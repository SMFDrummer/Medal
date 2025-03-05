package smf.talkweb.medal.ui.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import arrow.optics.Copy
import arrow.optics.optics
import arrow.optics.updateCopy
import smf.talkweb.medal.ui.theme.components.snackbar.SnackbarType
import smf.talkweb.medal.ui.theme.components.snackbar.Toast

lateinit var toast: SnackbarViewModel

class SnackbarViewModel : ViewModel() {
    private val _snackbarState = mutableStateOf(ToastState())
    val snackbarState: MutableState<ToastState> get() = _snackbarState

    fun update(block: Copy<ToastState>.() -> Unit) = _snackbarState.updateCopy(block)
}

@optics
data class ToastState(
    val toast: Toast = Toast(),
    val type: SnackbarType = SnackbarType.Primary
) {
    companion object
}

fun SnackbarType.show(message: String? = null, actionLabel: String? = null, withDismissAction: Boolean = false) = toast.update {
    ToastState.toast set Toast(message, actionLabel, withDismissAction)
    ToastState.type set this@show
}