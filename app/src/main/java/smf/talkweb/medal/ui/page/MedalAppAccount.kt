package smf.talkweb.medal.ui.page

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.AccountTree
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import api.Channel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.MedalAppDestination
import com.ramcosta.composedestinations.generated.destinations.SingleInsertPageDestination
import com.ramcosta.composedestinations.generated.destinations.SingleScaffoldDestination
import com.ramcosta.composedestinations.generated.destinations.StoreScaffoldDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel
import primitive
import service.model.User
import smf.talkweb.medal.R
import smf.talkweb.medal.dao.UserDao
import smf.talkweb.medal.dao.Users
import smf.talkweb.medal.dao.toUserDao
import smf.talkweb.medal.ui.theme.LocalContentColor
import smf.talkweb.medal.ui.theme.MedalTheme
import smf.talkweb.medal.ui.theme.components.Accordion
import smf.talkweb.medal.ui.theme.components.AlertDialog
import smf.talkweb.medal.ui.theme.components.BasicAlertDialog
import smf.talkweb.medal.ui.theme.components.Button
import smf.talkweb.medal.ui.theme.components.ButtonVariant
import smf.talkweb.medal.ui.theme.components.HorizontalDivider
import smf.talkweb.medal.ui.theme.components.Icon
import smf.talkweb.medal.ui.theme.components.IconButton
import smf.talkweb.medal.ui.theme.components.IconButtonVariant
import smf.talkweb.medal.ui.theme.components.Scaffold
import smf.talkweb.medal.ui.theme.components.Text
import smf.talkweb.medal.ui.theme.components.card.Card
import smf.talkweb.medal.ui.theme.components.card.CardDefaults
import smf.talkweb.medal.ui.theme.components.card.ElevatedCard
import smf.talkweb.medal.ui.theme.components.progress_indicators.CircularProgressIndicator
import smf.talkweb.medal.ui.theme.components.rememberAccordionState
import smf.talkweb.medal.ui.theme.components.snackbar.SnackbarType
import smf.talkweb.medal.ui.theme.components.textfield.TextField
import smf.talkweb.medal.ui.theme.components.topbar.MedalScreenTopBar
import smf.talkweb.medal.ui.theme.components.topbar.TopBarDefaults
import smf.talkweb.medal.ui.theme.contentColorFor
import smf.talkweb.medal.ui.theme.foundation.DestinationTransition
import smf.talkweb.medal.ui.theme.typography
import smf.talkweb.medal.ui.util.MedalPath
import smf.talkweb.medal.ui.util.lastModifiedDate
import smf.talkweb.medal.ui.viewModel.AccountState
import smf.talkweb.medal.ui.viewModel.AccountViewModel
import smf.talkweb.medal.ui.viewModel.MedalAppViewModel
import smf.talkweb.medal.ui.viewModel.bindState
import smf.talkweb.medal.ui.viewModel.show
import to
import java.io.File
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@NavGraph<RootGraph>(defaultTransitions = DestinationTransition::class)
annotation class AccountGraph

@NavGraph<AccountGraph>(defaultTransitions = DestinationTransition::class)
annotation class AccountToolGraph

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Destination<AccountGraph>(start = true)
@Composable
fun MedalAppAccount(navigator: DestinationsNavigator) {
    val context = LocalContext.current
    val accountViewModel: AccountViewModel = koinViewModel()
    val accountState by accountViewModel.accountState
    val state = rememberPullToRefreshState()
    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        accountViewModel.fetchAll(context)
        accountViewModel.startWatching(context)
        onDispose {
            accountViewModel.stopWatching()
        }
    }

    LaunchedEffect(accountState.singleAccounts, accountState.storeAccounts) {
        accountViewModel.update { AccountState.bindState set accountViewModel.calculateBindState() }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                accountViewModel.fetchAll(context).invokeOnCompletion { isRefreshing = false }
            }
        },
        state = state,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                containerColor = MedalTheme.colors.primary,
                color = MedalTheme.colors.onPrimary,
                state = state
            )
        },
    ) {
        AnimatedContent(accountState.fileOperationState) { fileOperationState ->
            when (fileOperationState) {
                is AccountState.FileOperationState.Loading ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }

                is AccountState.FileOperationState.Error ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MedalTheme.colors.error,
                                    contentColor = MedalTheme.colors.onError
                                )
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                                    Icon(
                                        Icons.Rounded.Error,
                                        modifier = Modifier
                                            .size(42.dp)
                                            .align(Alignment.CenterHorizontally)
                                    )
                                    Text("出错了！", style = typography.h2)
                                    fileOperationState.error.message?.let {
                                        Text(it, style = typography.h4)
                                    }
                                    Text(
                                        fileOperationState.error.stackTraceToString(),
                                        style = typography.label2
                                    )
                                }
                            }
                        }
                    }

                else -> {
                    AnimatedContent(accountState.bindState) { bindState ->
                        when (bindState) {
                            is AccountState.BindState.None -> Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "¯\\_(ツ)_/¯\n空空如也",
                                    style = typography.body1,
                                    color = LocalContentColor.current.copy(alpha = 0.8f)
                                )
                            }

                            is AccountState.BindState.Single -> SinglePage(
                                Modifier,
                                navigator
                            )

                            is AccountState.BindState.Store -> StorePage(Modifier)
                            is AccountState.BindState.Both -> LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                item {
                                    Card(
                                        border = BorderStroke(1.dp, MedalTheme.colors.onBackground),
                                        onClick = {
                                            navigator.navigate(SingleScaffoldDestination)
                                        }
                                    ) {
                                        Accordion(
                                            state = rememberAccordionState(
                                                expanded = true, enabled = false, clickable = false
                                            ),
                                            headerContent = {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(16.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        modifier = Modifier
                                                            .weight(0.1f)
                                                            .size(32.dp),
                                                        imageVector = Icons.Rounded.AccountCircle,
                                                        tint = MedalTheme.colors.onBackground
                                                    )
                                                    Spacer(Modifier.width(16.dp))
                                                    Text(
                                                        modifier = Modifier.weight(0.9f),
                                                        text = "单一账号",
                                                        style = typography.h2
                                                    )
                                                }
                                            },
                                            bodyContent = {
                                                HorizontalDivider()
                                                Text(
                                                    modifier = Modifier
                                                        .padding(16.dp)
                                                        .align(Alignment.End),
                                                    text = "已绑定 ${accountState.singleAccounts.size} 个账号",
                                                    style = typography.body2
                                                )
                                            }
                                        )
                                    }
                                }
                                item {
                                    Card(
                                        border = BorderStroke(1.dp, MedalTheme.colors.onBackground),
                                        onClick = {
                                            navigator.navigate(StoreScaffoldDestination)
                                        }
                                    ) {
                                        Accordion(
                                            state = rememberAccordionState(
                                                expanded = true, enabled = false, clickable = false
                                            ),
                                            headerContent = {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(16.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        modifier = Modifier
                                                            .weight(0.1f)
                                                            .size(32.dp),
                                                        imageVector = Icons.Rounded.AccountTree,
                                                        tint = MedalTheme.colors.onBackground
                                                    )
                                                    Spacer(Modifier.width(16.dp))
                                                    Text(
                                                        modifier = Modifier.weight(0.9f),
                                                        text = "账号库",
                                                        style = typography.h2
                                                    )
                                                }
                                            },
                                            bodyContent = {
                                                HorizontalDivider()
                                                Text(
                                                    modifier = Modifier
                                                        .padding(16.dp)
                                                        .align(Alignment.End),
                                                    text = "已导入 ${accountState.storeAccounts.size} 个账号库",
                                                    style = typography.body2
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Destination<AccountGraph>
@Composable
fun SingleScaffold(navigator: DestinationsNavigator) {
    val scrollBehavior = TopBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            MedalScreenTopBar(
                title = "单一账号",
                scrollBehavior = scrollBehavior
            ) {
                navigator.navigate(MedalAppDestination(selectedItem = "account"))
            }
        }
    ) { paddingValues ->
        SinglePage(modifier = Modifier.padding(paddingValues), navigator)
    }

    BackHandler {
        navigator.navigate(MedalAppDestination(selectedItem = "account"))
    }
}

@Destination<AccountGraph>
@Composable
fun StoreScaffold(navigator: DestinationsNavigator) {
    val scrollBehavior = TopBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            MedalScreenTopBar(
                title = "账号库",
                scrollBehavior = scrollBehavior
            ) {
                navigator.navigate(MedalAppDestination(selectedItem = "account"))
            }
        }
    ) { paddingValues ->
        StorePage(modifier = Modifier.padding(paddingValues))
    }

    BackHandler {
        navigator.navigate(MedalAppDestination(selectedItem = "account"))
    }
}

@Destination<AccountGraph>
@Composable
fun SinglePage(
    modifier: Modifier,
    navigator: DestinationsNavigator
) {
    val accountViewModel: AccountViewModel = koinViewModel()
    val accountState by accountViewModel.accountState
    val context = LocalContext.current
    var isVisible by remember { mutableStateOf(false) }
    var currentFile by remember { mutableStateOf<File?>(null) }
    val fileContentCache = remember { mutableStateMapOf<String, String>() }
    val loadingFiles = remember { mutableStateListOf<String>() }

    if (isVisible) {
        BasicAlertDialog(onDismissRequest = { isVisible = false }) {
            Column(
                modifier = Modifier
                    .background(MedalTheme.colors.primary)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides MedalTheme.colors.onPrimary
                ) {
                    Button(
                        text = "修改",
                        variant = ButtonVariant.Secondary,
                        onClick = {
                            isVisible = false
                            val userDao =
                                fileContentCache[currentFile?.absolutePath]?.to<User>(JsonFeature.IgnoreUnknownKeys)?.toUserDao()
                                    ?: UserDao(primitive { "" })
                            navigator.navigate(
                                SingleInsertPageDestination(
                                    userId = userDao.userId.content,
                                    userNick = userDao.userNick ?: "",
                                    phone = userDao.phone ?: "",
                                    password = userDao.password ?: "",
                                    token = userDao.token ?: "",
                                    pi = userDao.pi ?: "",
                                    sk = userDao.sk ?: "",
                                    ui = userDao.ui ?: "",
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        text = "删除",
                        variant = ButtonVariant.Secondary,
                        onClick = {
                            isVisible = false
                            accountViewModel.deleteFile(
                                context,
                                MedalPath.MedalUserSingle.locate,
                                currentFile!!.name
                            ).invokeOnCompletion {
                                SnackbarType.Success.show("删除成功")
                                accountViewModel.fetchSingle(context)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    accountState.singleAccounts.apply { Log.d("SinglePage", this.joinToString { it.name }) }
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(accountState.singleAccounts) { file ->
            HorizontalDivider()
            val fileKey = file.absolutePath

            LaunchedEffect(fileKey) {
                if (!fileContentCache.containsKey(fileKey) && !loadingFiles.contains(fileKey)) {
                    loadingFiles.add(fileKey)
                    val content = withContext(Dispatchers.IO) {
                        try {
                            file.readText()
                        } catch (e: Exception) {
                            "读取失败: ${e.message}"
                        }
                    }
                    fileContentCache[fileKey] = content
                    loadingFiles.remove(fileKey)
                }
            }

            Card(modifier = Modifier.fillMaxWidth(), shape = RectangleShape, onClick = {
                currentFile = file
                isVisible = true
            }) {
                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text(file.nameWithoutExtension, style = typography.h4)
                        if (loadingFiles.contains(fileKey)) {
                            CircularProgressIndicator(modifier = Modifier.size(12.dp))
                        } else {
                            val content = fileContentCache[fileKey] ?: ""
                            Text(
                                text = content.takeIf { it.isNotBlank() }
                                    ?.to<User>(JsonFeature.IgnoreUnknownKeys)?.userId?.content
                                    ?: "无内容",
                                style = typography.label3,
                                color = LocalContentColor.current.copy(alpha = 0.8f)
                            )
                        }
                    }
                    Spacer(Modifier.weight(1f))
                    Column(horizontalAlignment = Alignment.End) {
                        val date = file.lastModifiedDate()
                        Text(
                            date.first,
                            style = typography.label2,
                            color = LocalContentColor.current.copy(alpha = 0.8f)
                        )
                        Text(
                            date.second,
                            style = typography.label2,
                            color = LocalContentColor.current.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }

}

@Destination<AccountGraph>
@Composable
fun StorePage(modifier: Modifier) {
    val accountViewModel: AccountViewModel = koinViewModel()
    val accountState by accountViewModel.accountState
    val context = LocalContext.current
    var isVisible by remember { mutableStateOf(false) }
    var currentFile by remember { mutableStateOf<File?>(null) }
    val fileContentCache = remember { mutableStateMapOf<String, String>() }
    val loadingFiles = remember { mutableStateListOf<String>() }

    if (isVisible) {
        BasicAlertDialog(onDismissRequest = { isVisible = false }) {
            Column(
                modifier = Modifier
                    .background(MedalTheme.colors.primary)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides MedalTheme.colors.onPrimary
                ) {
                    Button(
                        text = "重置",
                        variant = ButtonVariant.Secondary,
                        onClick = {
                            isVisible = false
                            fileContentCache[currentFile?.absolutePath]?.to<Users>(JsonFeature.IgnoreUnknownKeys)?.let {
                                val users =
                                    Users(it.users.map { user -> user.copy(activate = true) })
                                accountViewModel.generateFile(
                                    context,
                                    Json.encodeToString(users),
                                    MedalPath.MedalUserStore.locate,
                                    currentFile!!.name
                                ).invokeOnCompletion {
                                    SnackbarType.Success.show("重置成功")
                                    accountViewModel.fetchStore(context)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        text = "删除",
                        variant = ButtonVariant.Secondary,
                        onClick = {
                            isVisible = false
                            accountViewModel.deleteFile(
                                context,
                                MedalPath.MedalUserStore.locate,
                                currentFile!!.name
                            ).invokeOnCompletion {
                                SnackbarType.Success.show("删除成功")
                                accountViewModel.fetchSingle(context)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    LazyColumn(modifier = modifier.fillMaxSize()) {
        accountState.storeAccounts.apply { Log.d("StorePage", this.joinToString { it.name }) }
        items(accountState.storeAccounts) { file ->
            HorizontalDivider()
            val fileKey = file.absolutePath

            LaunchedEffect(fileKey) {
                if (!fileContentCache.containsKey(fileKey) && !loadingFiles.contains(fileKey)) {
                    loadingFiles.add(fileKey)
                    val content = withContext(Dispatchers.IO) {
                        try {
                            file.bufferedReader().readText()
                        } catch (e: Exception) {
                            "读取失败: ${e.message}"
                        }
                    }
                    fileContentCache[fileKey] = content
                    loadingFiles.remove(fileKey)
                }
            }

            Card(modifier = Modifier.fillMaxWidth(), shape = RectangleShape, onClick = {
                currentFile = file
                isVisible = true
            }) {
                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text(file.nameWithoutExtension, style = typography.h4)
                        if (loadingFiles.contains(fileKey)) {
                            CircularProgressIndicator(modifier = Modifier.size(12.dp))
                        } else {
                            val content = fileContentCache[fileKey] ?: ""
                            Text(
                                text = content.takeIf { it.isNotBlank() }
                                    ?.to<Users>(JsonFeature.IgnoreUnknownKeys)?.users?.joinToString { it.userId.content }
                                    ?: "无内容",
                                style = typography.label3,
                                color = LocalContentColor.current.copy(alpha = 0.8f),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                        }
                    }
                    Spacer(Modifier.weight(1f))
                    Column(horizontalAlignment = Alignment.End) {
                        val date = file.lastModifiedDate()
                        Text(
                            date.first,
                            style = typography.label2,
                            color = LocalContentColor.current.copy(alpha = 0.8f)
                        )
                        Text(
                            date.second,
                            style = typography.label2,
                            color = LocalContentColor.current.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

data class SingleInsertPageArgs(
    val userId: String = "",
    val userNick: String = "",
    val phone: String = "",
    val password: String = "",
    val token: String = "",
    val pi: String = "",
    val sk: String = "",
    val ui: String = "",
)

@OptIn(ExperimentalUuidApi::class)
@Destination<AccountToolGraph>(
    start = true,
    navArgs = SingleInsertPageArgs::class,
    style = DestinationTransition::class
)
@Composable
fun SingleInsertPage(args: SingleInsertPageArgs, navigator: DestinationsNavigator) {
    val context = LocalContext.current
    val medalAppViewModel: MedalAppViewModel = koinViewModel()
    val accountViewModel: AccountViewModel = koinViewModel()
    val medalAppState by medalAppViewModel.medalAppState
    val scrollBehavior = TopBarDefaults.enterAlwaysScrollBehavior()
    val accordionState = rememberAccordionState(clickable = true)
    var alertDialogIsVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    var userId by remember { mutableStateOf(args.userId) }
    var userNick by remember { mutableStateOf(args.userNick) }
    var phone by remember { mutableStateOf(args.phone) }
    var password by remember { mutableStateOf(args.password) }
    var token by remember { mutableStateOf(args.token) }
    var pi by remember { mutableStateOf(args.pi) }
    var sk by remember { mutableStateOf(args.sk) }
    var ui by remember { mutableStateOf(args.ui) }

    Scaffold(
        topBar = {
            MedalScreenTopBar(
                title = stringResource(R.string.bind_single_account),
                scrollBehavior = scrollBehavior,
                onBack = { navigator.navigateUp() }
            )
        }
    ) { paddingValues ->
        AnimatedContent(
            medalAppState.channelState, modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .imePadding()
        ) { state ->
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                item { Text("绑定 $state 渠道账号", style = typography.h4) }
                item {
                    when (state) {
                        Channel.IOS.channelName -> TextField(
                            modifier = Modifier.animateContentSize(),
                            value = userId,
                            onValueChange = { userId = it },
                            label = { Text("用户 udid") },
                            placeholder = {
                                Text(
                                    "11111111-1234-1234-1234-123456789000",
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            isError = if (userId.isNotEmpty()) !Regex("^\\d{8}-\\d{4}-\\d{4}-\\d{4}-\\d{12}$").matches(
                                userId
                            ) else false
                        )

                        Channel.Official.channelName,
                        Channel.TapTap.channelName,
                        Channel.Haoyoukuaibao.channelName -> TextField(
                            value = userId,
                            onValueChange = { userId = it },
                            label = { Text("用户 userId") },
                            singleLine = true,
                            placeholder = { Text("12345678") },
                            isError = if (userId.isNotEmpty()) !Regex("^\\d{8}$").matches(userId) else false
                        )

                        else -> TextField(
                            modifier = Modifier.animateContentSize(),
                            value = userId,
                            onValueChange = { userId = it },
                            label = { Text("用户 userId") }
                        )
                    }
                }
                item {
                    TextField(
                        value = userNick,
                        onValueChange = { userNick = it },
                        singleLine = true,
                        placeholder = { Text("拓维$userId") },
                        label = { Text("用户昵称") }
                    )
                }
                item {
                    TextField(
                        modifier = Modifier.animateContentSize(),
                        value = phone,
                        prefix = { Text("+86", style = typography.label1) },
                        onValueChange = { phone = it },
                        label = { Text("手机号") },
                        supportingText = {
                            Text(
                                R.string.phone_tips,
                                textStyle = typography.label2
                            )
                        },
                        isError = if (phone.isNotEmpty()) !Regex("^1[3-9]\\d{9}\$").matches(phone) else false
                    )
                }
                item {
                    TextField(
                        modifier = Modifier.animateContentSize(),
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("密码") },
                        supportingText = {
                            Text(
                                R.string.password_tips,
                                textStyle = typography.label2
                            )
                        }
                    )
                }
                item {
                    TextField(
                        modifier = Modifier.animateContentSize(),
                        value = token,
                        onValueChange = { token = it },
                        label = { Text("Token") },
                        placeholder = { Text("xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx") },
                        supportingText = { Text(R.string.token_tips) },
                        isError = try {
                            if (token.isNotEmpty()) Uuid.parse(token); false
                        } catch (_: Exception) {
                            true
                        }
                    )
                }
                item {
                    ElevatedCard(
                        modifier = Modifier
                            .padding(0.dp)
                            .animateContentSize(),
                        border = BorderStroke(1.dp, contentColorFor(MedalTheme.colors.background))
                    ) {
                        Accordion(
                            state = accordionState,
                            headerContent = {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier.weight(0.8f)
                                    ) {
                                        Column {
                                            Text(
                                                textRes = R.string.bind_credential,
                                                textStyle = typography.h3
                                            )
                                            Text(
                                                textRes = R.string.bind_credential_description,
                                                textStyle = typography.body3
                                            )
                                        }
                                    }
                                    Box(
                                        modifier = Modifier.weight(0.2f),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        IconButton(
                                            variant = IconButtonVariant.Ghost,
                                            onClick = { accordionState.toggle() }) {
                                            Icon(
                                                modifier = Modifier.rotate(accordionState.animationProgress * 180),
                                                imageVector = Icons.Outlined.KeyboardArrowDown
                                            )
                                        }
                                    }
                                }
                            },
                            bodyContent = {
                                HorizontalDivider()
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    TextField(
                                        value = ui,
                                        onValueChange = { ui = it },
                                        singleLine = true,
                                        label = { Text("User ID") }
                                    )
                                    TextField(
                                        value = sk,
                                        onValueChange = { sk = it },
                                        singleLine = true,
                                        label = { Text("Security Key") }
                                    )
                                    when (state) {
                                        Channel.IOS.channelName -> TextField(
                                            value = pi,
                                            onValueChange = { pi = it },
                                            singleLine = true,
                                            label = { Text("Personal ID") }
                                        )

                                        else -> TextField(
                                            value = ui,
                                            onValueChange = { pi = it },
                                            readOnly = true,
                                            enabled = false,
                                            singleLine = true,
                                            label = { Text("Personal ID") },
                                            supportingText = {
                                                Text(
                                                    R.string.credential_ui_tips,
                                                    textStyle = typography.label2
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
                item {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        text = "绑定账号",
                        onClick = { alertDialogIsVisible = true })
                }
            }
        }

        if (alertDialogIsVisible) {
            AlertDialog(
                onDismissRequest = { alertDialogIsVisible = false },
                onConfirmClick = {
                    scope.launch {
                        navigator.navigateUp()
                    }.invokeOnCompletion {
                        onConfirm(
                            context, accountViewModel, UserDao(
                                userId = primitive { userId },
                                userNick = userNick.ifEmpty { "拓维$userId" },
                                phone = phone.ifEmpty { null },
                                password = password.ifEmpty { null },
                                token = token.ifEmpty { null },
                                pi = pi.ifEmpty { null },
                                sk = sk.ifEmpty { null },
                                ui = ui.ifEmpty { null },
                            )
                        )
                    }
                },
                title = userNick.ifEmpty { "拓维$userId" },
                text = "确认绑定账号 $userId 吗？",
                confirmButtonText = "确认",
                dismissButtonText = "取消"
            )
        }
    }

    BackHandler {
        navigator.navigateUp()
    }
}

private fun onConfirm(context: Context, accountViewModel: AccountViewModel, userDao: UserDao) {
    accountViewModel.generateFile(
        context,
        Json.encodeToString(userDao.toUser()),
        MedalPath.MedalUserSingle.locate,
        (userDao.userNick ?: "拓维${userDao.userId.content}") + MedalPath.MedalUserSingle.suffix
    ).invokeOnCompletion {
        accountViewModel.fetchSingle(context)
    }
}
