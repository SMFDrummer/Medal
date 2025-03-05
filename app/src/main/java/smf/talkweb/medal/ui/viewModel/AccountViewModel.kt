package smf.talkweb.medal.ui.viewModel

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.Q
import android.os.FileObserver
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.optics.Copy
import arrow.optics.optics
import arrow.optics.updateCopy
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import smf.talkweb.medal.ui.util.MedalPath
import smf.talkweb.medal.ui.util.copyToPrivate
import smf.talkweb.medal.ui.util.deleteFromPrivate
import smf.talkweb.medal.ui.util.fetchFromPrivate
import smf.talkweb.medal.ui.util.generateToPrivate
import smf.talkweb.medal.ui.viewModel.AccountState.FileOperationState
import java.io.File

@OptIn(FlowPreview::class)
class AccountViewModel : ViewModel() {
    private val _accountState = mutableStateOf(AccountState())
    val accountState: State<AccountState> get() = _accountState

    private var fileWatchers: MutableList<FileObserver> = mutableListOf()
    private val fetchChannel = Channel<Unit>(Channel.CONFLATED)

    fun init(context: Context) {
        viewModelScope.launch {
            fetchChannel.receiveAsFlow()
                .debounce(500)
                .collect { fetchAll(context) }
        }
    }

    private fun triggerDebounceFetch() = fetchChannel.trySend(Unit)

    @RequiresApi(Q)
    inner class FileWatcher(dir: File) : FileObserver(dir) {
        override fun onEvent(event: Int, path: String?) {
            if (event == CREATE || event == DELETE || event == MODIFY) {
                triggerDebounceFetch()
            }
        }
    }

    @RequiresApi(Q)
    fun startWatching(context: Context) {
        stopWatching()
        listOf(MedalPath.MedalUserSingle, MedalPath.MedalUserStore).forEach { path ->
            context.getExternalFilesDir(path.locate)?.let { dir ->
                val watcher = FileWatcher(dir).apply { startWatching() }
                fileWatchers.add(watcher)
            }
        }
    }

    override fun onCleared() {
        stopWatching()
        fetchChannel.close()
    }

    fun fetchAll(context: Context) = viewModelScope.launch {
        try {
            update { AccountState.fileOperationState set FileOperationState.Loading }
            val single = context.fetchFromPrivate(MedalPath.MedalUserSingle.locate, MedalPath.MedalUserSingle.suffix)
            val store = context.fetchFromPrivate(MedalPath.MedalUserStore.locate, MedalPath.MedalUserStore.suffix)
            update {
                AccountState.singleAccounts set single
                AccountState.storeAccounts set store
                AccountState.bindState set calculateBindState()
                AccountState.fileOperationState set FileOperationState.Success
            }
        } catch (e: Exception) {
            update { AccountState.fileOperationState set FileOperationState.Error(e) }
        }
    }

    fun stopWatching() {
        fileWatchers.forEach { it.stopWatching() }
        fileWatchers.clear()
    }

    fun update(block: Copy<AccountState>.() -> Unit) = _accountState.updateCopy(block)

    fun fetchSingle(context: Context) = viewModelScope.launch {
        try {
            update { AccountState.fileOperationState set FileOperationState.Loading }
            val list = context.fetchFromPrivate(MedalPath.MedalUserSingle.locate, MedalPath.MedalUserSingle.suffix)
            update {
                AccountState.singleAccounts set list
                AccountState.bindState set calculateBindState()
                AccountState.fileOperationState set FileOperationState.Success
            }
        } catch (e: Exception) {
            update { AccountState.fileOperationState set FileOperationState.Error(e) }
        }
    }
    fun fetchStore(context: Context) = viewModelScope.launch {
        try {
            update { AccountState.fileOperationState set FileOperationState.Loading }
            val list = context.fetchFromPrivate(MedalPath.MedalUserStore.locate, MedalPath.MedalUserStore.suffix)
            update {
                AccountState.storeAccounts set list
                AccountState.bindState set calculateBindState()
                AccountState.fileOperationState set FileOperationState.Success
            }
        } catch (e: Exception) {
            update { AccountState.fileOperationState set FileOperationState.Error(e) }
        }
    }

    fun copyFile(context: Context, from: Uri, to: String, suffix: String = "") = viewModelScope.launch {
        try {
            update { AccountState.fileOperationState set FileOperationState.Loading }
            context.copyToPrivate(from, to, suffix)
            update { AccountState.fileOperationState set FileOperationState.Success }
        } catch (e: Exception) {
            update { AccountState.fileOperationState set FileOperationState.Error(e) }
        }
    }

    fun generateFile(context: Context, from: Any?, to: String, byName: String) = viewModelScope.launch {
        try {
            update { AccountState.fileOperationState set FileOperationState.Loading }
            context.generateToPrivate(from, to, byName)
            update { AccountState.fileOperationState set FileOperationState.Success }
        } catch (e: Exception) {
            update { AccountState.fileOperationState set FileOperationState.Error(e) }
        }
    }

    fun deleteFile(context: Context, locate: String, fileName: String) = viewModelScope.launch {
        try {
            update { AccountState.fileOperationState set FileOperationState.Loading }
            context.deleteFromPrivate(locate, fileName)
            update { AccountState.fileOperationState set FileOperationState.Success }
        } catch (e: Exception) {
            update { AccountState.fileOperationState set FileOperationState.Error(e) }
        }
    }

    fun calculateBindState() = when {
        accountState.value.singleAccounts.isEmpty() && accountState.value.storeAccounts.isEmpty() -> AccountState.BindState.None
        accountState.value.singleAccounts.isNotEmpty() && accountState.value.storeAccounts.isNotEmpty() -> AccountState.BindState.Both
        accountState.value.singleAccounts.isNotEmpty() -> AccountState.BindState.Single
        else -> AccountState.BindState.Store
    }
}

@optics
data class AccountState(
    val singleAccounts: List<File> = listOf(),
    val storeAccounts: List<File> = listOf(),
    val bindState: BindState = BindState.None,
    val fileOperationState: FileOperationState = FileOperationState.Idle,
) {
    sealed class FileOperationState {
        data object Idle : FileOperationState()
        data object Loading : FileOperationState()
        data object Success: FileOperationState()
        data class Error(val error: Throwable): FileOperationState()
    }

    sealed class BindState {
        data object None : BindState()
        data object Single : BindState()
        data object Store : BindState()
        data object Both : BindState()
    }

    companion object
}