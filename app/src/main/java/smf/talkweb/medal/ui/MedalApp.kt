package smf.talkweb.medal.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nomanr.composables.bottomsheet.rememberModalBottomSheetState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import service.getLatestVersion
import service.latestVersion
import service.model.LatestVersions
import smf.talkweb.medal.R
import smf.talkweb.medal.ui.theme.MedalTheme
import smf.talkweb.medal.ui.theme.components.Accordion
import smf.talkweb.medal.ui.theme.components.Button
import smf.talkweb.medal.ui.theme.components.HorizontalDivider
import smf.talkweb.medal.ui.theme.components.Icon
import smf.talkweb.medal.ui.theme.components.IconButton
import smf.talkweb.medal.ui.theme.components.IconButtonVariant
import smf.talkweb.medal.ui.theme.components.ModalBottomSheet
import smf.talkweb.medal.ui.theme.components.Scaffold
import smf.talkweb.medal.ui.theme.components.Text
import smf.talkweb.medal.ui.theme.components.card.Card
import smf.talkweb.medal.ui.theme.components.card.CardColors
import smf.talkweb.medal.ui.theme.components.card.ElevatedCard
import smf.talkweb.medal.ui.theme.components.progress_indicators.LinearProgressIndicator
import smf.talkweb.medal.ui.theme.components.rememberAccordionState
import smf.talkweb.medal.ui.theme.components.topbar.TopBar
import smf.talkweb.medal.ui.theme.components.topbar.TopBarDefaults
import smf.talkweb.medal.ui.theme.typography

class MedalAppViewModel : ViewModel() {
    private val _medalAppState = MutableStateFlow(MedalAppState())
    val medalAppState = _medalAppState.asStateFlow()

    data class MedalAppState(
        val modalBottomSheetIsVisible: Boolean = false,
        val latestVersionState: LatestVersionState<Map<String, LatestVersions.LatestVersion>> = LatestVersionState.Loading
    ) {
        sealed class LatestVersionState<out R> {
            data object Loading: LatestVersionState<Nothing>()
            data class Success<out T>(val data: T): LatestVersionState<T>()
            data class Error(val error: Throwable): LatestVersionState<Nothing>()
        }
    }

    inner class StateBuilder(private var state: MedalAppState) {
        var modalBottomSheetIsVisible: Boolean
            get() = state.modalBottomSheetIsVisible
            set(value) {
                state = state.copy(modalBottomSheetIsVisible = value)
            }

        var latestVersionState: MedalAppState.LatestVersionState<Map<String, LatestVersions.LatestVersion>>
            get() = state.latestVersionState
            set(value) {
                state = state.copy(latestVersionState = value)
            }

        fun build(): MedalAppState = state
    }

    init {
        fetchLatestVersion()
    }

    fun fetchLatestVersion() = viewModelScope.launch {
        try {
            update { latestVersionState = MedalAppState.LatestVersionState.Loading }
            latestVersion = getLatestVersion()
            update { latestVersionState = MedalAppState.LatestVersionState.Success(latestVersion) }
        } catch (e: Exception) {
            update { latestVersionState = MedalAppState.LatestVersionState.Error(e) }
        }
    }

    fun update(block: StateBuilder.() -> Unit) {
        _medalAppState.value = StateBuilder(_medalAppState.value).apply(block).build()
    }
}

@Composable
fun MedalApp(modifier: Modifier = Modifier) {
    val viewModel: MedalAppViewModel = viewModel()
    MedalTheme {
        Scaffold(
            topBar = {
                MedalAppTopBar(viewModel = viewModel)
            },
            bottomBar = {

            }
        ) { paddingValue ->
            MedalAppContent(modifier = Modifier.padding(paddingValue), viewModel = viewModel)
        }
        MedalAppModalBottomSheet(viewModel = viewModel)
    }
}

@Composable
fun MedalAppTopBar(modifier: Modifier = Modifier, viewModel: MedalAppViewModel) {
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
                    onClick = { viewModel.update { modalBottomSheetIsVisible = true } },
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
fun MedalAppContent(modifier: Modifier = Modifier, viewModel: MedalAppViewModel) {
    val context = LocalContext.current
    val medalAppState by viewModel.medalAppState.collectAsState()
    val accordionExpandedState = rememberAccordionState(clickable = true)

    val normalCardColor = CardColors(
        containerColor = MedalTheme.colors.onPrimary,
        contentColor = MedalTheme.colors.primary,
        disabledContainerColor = MedalTheme.colors.disabled,
        disabledContentColor = MedalTheme.colors.onDisabled
    )
    val normalSelectedCardColor = CardColors(
        containerColor = MedalTheme.colors.primary,
        contentColor = MedalTheme.colors.onPrimary,
        disabledContainerColor = MedalTheme.colors.disabled,
        disabledContentColor = MedalTheme.colors.onDisabled
    )
    val errorCardColor = CardColors(
        containerColor = MedalTheme.colors.onError,
        contentColor = MedalTheme.colors.error,
        disabledContainerColor = MedalTheme.colors.disabled,
        disabledContentColor = MedalTheme.colors.onDisabled
    )
    val errorSelectedCardColor = CardColors(
        containerColor = MedalTheme.colors.error,
        contentColor = MedalTheme.colors.onError,
        disabledContainerColor = MedalTheme.colors.disabled,
        disabledContentColor = MedalTheme.colors.onDisabled
    )

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedCard(modifier = Modifier.padding(0.dp)) {
            Accordion(
                state = accordionExpandedState,
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
                                    textRes = R.string.latest_version_title,
                                    textStyle = typography.h3
                                )
                                Text(
                                    textRes = R.string.latest_version_description,
                                    textStyle = typography.label3
                                )
                            }
                        }
                        Box(
                            modifier = Modifier.weight(0.2f),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            IconButton(variant = IconButtonVariant.Ghost, onClick = {
                                accordionExpandedState.toggle()
                            }) {
                                Icon(
                                    modifier = Modifier.rotate(accordionExpandedState.animationProgress * 180),
                                    imageVector = Icons.Outlined.KeyboardArrowDown
                                )
                            }
                        }
                    }
                },
                bodyContent = {
                    HorizontalDivider()
                    LazyColumn (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        when(medalAppState.latestVersionState) {
                            is MedalAppViewModel.MedalAppState.LatestVersionState.Loading -> item {
                                LinearProgressIndicator()
                            }
                            is MedalAppViewModel.MedalAppState.LatestVersionState.Success -> items((medalAppState.latestVersionState as MedalAppViewModel.MedalAppState.LatestVersionState.Success).data.toList()){ (key, value) ->
                                Card(
                                    modifier = Modifier.clickable(value.status == 1) {

                                    },
                                    shape = ShapeDefaults.ExtraSmall,
                                    colors =
                                ) {

                                }
                            }
                            is MedalAppViewModel.MedalAppState.LatestVersionState.Error -> item {
                                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Card(
                                        shape = ShapeDefaults.ExtraSmall,
                                        colors = errorSelectedCardColor
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier.weight(0.15f),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(imageVector = Icons.Filled.Error)
                                            }
                                            Box(
                                                modifier = Modifier.weight(0.75f),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Column(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                                ) {
                                                    Text(
                                                        textRes = R.string.error_title,
                                                        textStyle = typography.h4
                                                    )
                                                    (medalAppState.latestVersionState as MedalAppViewModel.MedalAppState.LatestVersionState.Error).error.message?.let {
                                                        Text(
                                                            text = it,
                                                            style = typography.body3
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    Button(
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = {
                                            viewModel.fetchLatestVersion()
                                        }
                                    ) {
                                        Text(
                                            textRes = R.string.retry,
                                            textStyle = typography.h4
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun MedalAppModalBottomSheet(modifier: Modifier = Modifier, viewModel: MedalAppViewModel) {
    val medalAppState by viewModel.medalAppState.collectAsState()
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        sheetState = sheetState,
        isVisible = medalAppState.modalBottomSheetIsVisible,
        onDismissRequest = { viewModel.update { modalBottomSheetIsVisible = false } }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            repeat(20) {
                Text("Test content")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MedalAppPreview(modifier: Modifier = Modifier) {
    MedalTheme {
        MedalAppContent(modifier, MedalAppViewModel())
    }
}