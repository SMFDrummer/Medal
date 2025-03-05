package smf.talkweb.medal.ui.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.optics.Copy
import arrow.optics.optics
import arrow.optics.updateCopy
import kotlinx.coroutines.launch
import service.getLatestVersion
import service.latestVersion
import service.model.LatestVersions

class MedalAppViewModel : ViewModel() {
    private val _medalAppState = mutableStateOf(MedalAppState())
    val medalAppState: State<MedalAppState> get() = _medalAppState

    fun update(block: Copy<MedalAppState>.() -> Unit) = _medalAppState.updateCopy(block)

    init {
        fetchLatestVersion()
    }

    fun fetchLatestVersion() = viewModelScope.launch {
        try {
            update { MedalAppState.latestVersionState set MedalAppState.LatestVersionState.Loading }
            latestVersion = getLatestVersion()
            update {
                MedalAppState.latestVersionState set MedalAppState.LatestVersionState.Success(
                    latestVersion
                )
            }
        } catch (e: Exception) {
            update { MedalAppState.latestVersionState set MedalAppState.LatestVersionState.Error(e) }
        }
    }
}

@optics
data class MedalAppState(
    val cardIsVisible: Boolean = false,
    val modalBottomSheetIsVisible: Boolean = false,
    val latestVersionState: LatestVersionState<Map<String, LatestVersions.LatestVersion>> = LatestVersionState.Loading,
    val channelState: String = "拓维官网包",
    val fabIsVisible: Boolean = false,
) {
    sealed class LatestVersionState<out R> {
        data object Loading : LatestVersionState<Nothing>()
        data class Success<out T>(val data: T) : LatestVersionState<T>()
        data class Error(val error: Throwable) : LatestVersionState<Nothing>()
    }

    companion object
}