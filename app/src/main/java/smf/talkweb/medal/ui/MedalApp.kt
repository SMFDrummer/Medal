package smf.talkweb.medal.ui

import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.ManageAccounts
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.SupervisorAccount
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import api.Channel
import com.nomanr.composables.bottomsheet.rememberModalBottomSheetState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.SingleInsertPageDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import service.latestVersion
import smf.talkweb.medal.R
import smf.talkweb.medal.ui.page.MedalAppAccount
import smf.talkweb.medal.ui.page.MedalAppFunction
import smf.talkweb.medal.ui.page.MedalAppHome
import smf.talkweb.medal.ui.page.MedalAppProfile
import smf.talkweb.medal.ui.theme.MedalTheme
import smf.talkweb.medal.ui.theme.components.HorizontalDivider
import smf.talkweb.medal.ui.theme.components.Icon
import smf.talkweb.medal.ui.theme.components.IconButton
import smf.talkweb.medal.ui.theme.components.IconButtonVariant
import smf.talkweb.medal.ui.theme.components.ModalBottomSheet
import smf.talkweb.medal.ui.theme.components.NavigationBar
import smf.talkweb.medal.ui.theme.components.NavigationBarItem
import smf.talkweb.medal.ui.theme.components.Scaffold
import smf.talkweb.medal.ui.theme.components.Text
import smf.talkweb.medal.ui.theme.components.card.CardDefaults
import smf.talkweb.medal.ui.theme.components.card.OutlinedCard
import smf.talkweb.medal.ui.theme.components.menu_fab.MenuFabItem
import smf.talkweb.medal.ui.theme.components.menu_fab.MenuFloatingActionButton
import smf.talkweb.medal.ui.theme.components.snackbar.RenderSnackbar
import smf.talkweb.medal.ui.theme.components.snackbar.SnackbarHost
import smf.talkweb.medal.ui.theme.components.snackbar.SnackbarType
import smf.talkweb.medal.ui.theme.components.snackbar.Toast
import smf.talkweb.medal.ui.theme.components.snackbar.rememberSnackbarHost
import smf.talkweb.medal.ui.theme.components.topbar.TopBar
import smf.talkweb.medal.ui.theme.components.topbar.TopBarDefaults
import smf.talkweb.medal.ui.theme.foundation.DestinationTransition
import smf.talkweb.medal.ui.theme.typography
import smf.talkweb.medal.ui.util.MedalPath
import smf.talkweb.medal.ui.viewModel.AccountState
import smf.talkweb.medal.ui.viewModel.AccountViewModel
import smf.talkweb.medal.ui.viewModel.MedalAppState
import smf.talkweb.medal.ui.viewModel.MedalAppViewModel
import smf.talkweb.medal.ui.viewModel.ToastState
import smf.talkweb.medal.ui.viewModel.channelState
import smf.talkweb.medal.ui.viewModel.fabIsVisible
import smf.talkweb.medal.ui.viewModel.modalBottomSheetIsVisible
import smf.talkweb.medal.ui.viewModel.show
import smf.talkweb.medal.ui.viewModel.storeAccounts
import smf.talkweb.medal.ui.viewModel.toast

data class MedalAppNavArgs(
    val selectedItem: String = "home"
)

@RequiresApi(Build.VERSION_CODES.Q)
@Destination<RootGraph>(
    start = true,
    navArgs = MedalAppNavArgs::class,
    style = DestinationTransition::class
)
@Composable
fun MedalApp(args: MedalAppNavArgs, navigator: DestinationsNavigator) {
    val viewModel: MedalAppViewModel = koinViewModel()
    val scope = rememberCoroutineScope()

    val snackBarHostState = rememberSnackbarHost()
    toast = viewModel()
    val toastState by toast.snackbarState

    var selectedItem by remember { mutableStateOf(args.selectedItem) }

    LaunchedEffect(toastState) {
        if (toastState.toast.message == null) return@LaunchedEffect
        scope.launch {
            with(toastState.toast) {
                snackBarHostState.showSnackbar(message!!, actionLabel, withDismissAction)
            }
        }.invokeOnCompletion {
            toast.update { ToastState.toast set Toast() }
        }
    }

    LaunchedEffect(selectedItem) {
        viewModel.update {
            if (selectedItem == "account") {
                MedalAppState.fabIsVisible set true
            } else {
                MedalAppState.fabIsVisible set false
            }
        }
    }

    Scaffold(
        topBar = { MedalAppTopBar() },
        bottomBar = { MedalBottomNavigationBar(selectedItem) { selectedItem = it } },
        snackbarHost = {
            SnackbarHost(
                snackBarHostState,
                snackbar = { RenderSnackbar(toastState.type, it) })
        },
        floatingActionButton = { MedalAccountFab(navigator) }
    ) { paddingValue ->
        Crossfade(
            targetState = selectedItem,
            modifier = Modifier.padding(paddingValue)
        ) { targetItem ->
            when (targetItem) {
                "home" -> MedalAppHome(navigator)
                "function" -> MedalAppFunction(navigator)
                "account" -> MedalAppAccount(navigator)
                "profile" -> MedalAppProfile(navigator)
            }
        }
    }
    MedalAppModalBottomSheet()

}

@Composable
fun MedalAccountFab(navigator: DestinationsNavigator) {
    val viewModel: MedalAppViewModel = koinViewModel()
    val context = LocalContext.current
    val medalAppState by viewModel.medalAppState
    val accountViewModel: AccountViewModel = viewModel()

    val menuItems = remember {
        mutableStateListOf(
            MenuFabItem(
                icon = { Icon(Icons.Rounded.ManageAccounts, tint = MedalTheme.colors.onPrimary) },
                label = "绑定账号",
            ),
            MenuFabItem(
                icon = {
                    Icon(
                        Icons.Rounded.SupervisorAccount,
                        tint = MedalTheme.colors.onPrimary
                    )
                },
                label = "导入账号库"
            )
        )
    }

    val storeLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        result.data?.data?.let {
            accountViewModel.copyFile(
                context,
                it,
                MedalPath.MedalUserStore.locate,
                MedalPath.MedalUserStore.suffix
            ).invokeOnCompletion {
                accountViewModel.fetchStore(context)
            }
        }
    }

    val documentIndent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "application/json"
        putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/json", "text/plain"))
    }

    AnimatedVisibility(
        visible = medalAppState.fabIsVisible,
        enter = fadeIn(initialAlpha = 0.4f),
        exit = fadeOut()
    ) {
        MenuFloatingActionButton(
            srcIcon = Icons.Rounded.Add,
            items = menuItems,
            srcIconColor = MedalTheme.colors.onPrimary
        ) { item ->
            when (item.label) {
                "绑定账号" -> navigator.navigate(
                    SingleInsertPageDestination(
                        userId = "",
                        userNick = "",
                        phone = "",
                        password = "",
                        token = "",
                        pi = "",
                        sk = "",
                        ui = "",
                    )
                )

                "导入账号库" -> storeLauncher.launch(documentIndent)
            }
        }
    }
}

@Composable
fun MedalBottomNavigationBar(
    selectedItem: String,
    onSelect: (String) -> Unit
) {
    val items = listOf(
        Triple("home", R.string.home, Icons.Rounded.Home),
        Triple("function", R.string.function, Icons.Rounded.Apps),
        Triple("account", R.string.account, Icons.Rounded.AccountBox),
        Triple("profile", R.string.profile, Icons.Rounded.Star)
    )

    Column {
        HorizontalDivider()
        NavigationBar {
            items.forEach { (route, label, icon) ->
                NavigationBarItem(
                    selected = selectedItem == route,
                    onClick = { onSelect(route) },
                    label = { Text(textRes = label, textStyle = typography.label2, maxLines = 1) },
                    icon = { Icon(imageVector = icon) }
                )
            }
        }
    }
}

@Composable
fun MedalAppTopBar() {
    val viewModel: MedalAppViewModel = koinViewModel()
    val scrollBehavior = TopBarDefaults.enterAlwaysScrollBehavior()
    TopBar(
        scrollBehavior = scrollBehavior
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.update { MedalAppState.modalBottomSheetIsVisible set true } },
                    variant = IconButtonVariant.Ghost
                ) {
                    Icon(imageVector = Icons.Filled.MoreVert)
                }
            }
            Text(
                textRes = R.string.app_name,
                textStyle = typography.h1
            )
        }
    }
}

@Composable
fun MedalAppModalBottomSheet() {
    val viewModel: MedalAppViewModel = koinViewModel()
    val medalAppState by viewModel.medalAppState
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val normalCardColor = MedalTheme.colors.primary to CardDefaults.outlinedCardColors(
        containerColor = MedalTheme.colors.onPrimary,
        contentColor = MedalTheme.colors.primary,
    )
    val normalSelectedCardColor = MedalTheme.colors.onPrimary to CardDefaults.outlinedCardColors(
        containerColor = MedalTheme.colors.primary,
        contentColor = MedalTheme.colors.onPrimary,
    )
    val errorCardColor = MedalTheme.colors.error to CardDefaults.outlinedCardColors(
        containerColor = MedalTheme.colors.onError,
        contentColor = MedalTheme.colors.error,
    )
    val errorSelectedCardColor = MedalTheme.colors.onError to CardDefaults.outlinedCardColors(
        containerColor = MedalTheme.colors.error,
        contentColor = MedalTheme.colors.onError,
    )

    ModalBottomSheet(
        sheetState = sheetState,
        isVisible = medalAppState.modalBottomSheetIsVisible,
        onDismissRequest = {
            viewModel.update { MedalAppState.modalBottomSheetIsVisible set false }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { Text(textRes = R.string.please_select_a_channel, textStyle = typography.h2) }

            items(Channel.entries) { channel ->
                val color = latestVersion[channel.channelName]?.let {
                    when (it.status) {
                        0 -> if (medalAppState.channelState == it.channel) errorSelectedCardColor else errorCardColor
                        else -> if (medalAppState.channelState == it.channel) normalSelectedCardColor else normalCardColor
                    }
                } ?: normalCardColor

                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = color.first,
                            shape = CardDefaults.OutlinedShape
                        ),
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            viewModel.update {
                                MedalAppState.channelState set channel.channelName
                                MedalAppState.modalBottomSheetIsVisible set false
                            }
                        }
                        scope.launch {
                            latestVersion[channel.channelName]?.let {
                                when (it.status) {
                                    1 -> SnackbarType.Success.show("已切换至 ${channel.channelName} 渠道")
                                    else -> SnackbarType.Warn.show("切换至 ${channel.channelName} 渠道\n此渠道可能包含未知错误，请谨慎使用")
                                }
                            }
                        }
                    },
                    colors = color.second,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = channel.channelName,
                            style = typography.h4
                        )
                        Text(
                            text = channel.packageName,
                            style = typography.label2
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}