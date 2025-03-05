package smf.talkweb.medal.ui.page

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.PhoneIphone
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import api.Channel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import manager.platformManager
import org.koin.androidx.compose.koinViewModel
import service.latestVersion
import smf.talkweb.medal.R
import smf.talkweb.medal.pref.MedalPreferences
import smf.talkweb.medal.pref.dataStore
import smf.talkweb.medal.pref.read
import smf.talkweb.medal.pref.write
import smf.talkweb.medal.ui.theme.MedalTheme
import smf.talkweb.medal.ui.theme.components.Accordion
import smf.talkweb.medal.ui.theme.components.Button
import smf.talkweb.medal.ui.theme.components.HorizontalDivider
import smf.talkweb.medal.ui.theme.components.Icon
import smf.talkweb.medal.ui.theme.components.IconButton
import smf.talkweb.medal.ui.theme.components.IconButtonVariant
import smf.talkweb.medal.ui.theme.components.Text
import smf.talkweb.medal.ui.theme.components.card.Card
import smf.talkweb.medal.ui.theme.components.card.CardDefaults
import smf.talkweb.medal.ui.theme.components.card.ElevatedCard
import smf.talkweb.medal.ui.theme.components.card.OutlinedCard
import smf.talkweb.medal.ui.theme.components.progress_indicators.LinearProgressIndicator
import smf.talkweb.medal.ui.theme.components.rememberAccordionState
import smf.talkweb.medal.ui.theme.contentColorFor
import smf.talkweb.medal.ui.theme.typography
import smf.talkweb.medal.ui.viewModel.MedalAppState
import smf.talkweb.medal.ui.viewModel.MedalAppViewModel
import smf.talkweb.medal.ui.viewModel.cardIsVisible
import smf.talkweb.medal.ui.viewModel.channelState
import smf.talkweb.medal.ui.viewModel.modalBottomSheetIsVisible

@Composable
fun MedalAppHome(navigator: DestinationsNavigator) {
    val viewModel: MedalAppViewModel = koinViewModel()
    val context = LocalContext.current
    val medalAppState by viewModel.medalAppState
    val accordionExpandedState = rememberAccordionState(clickable = true)

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (medalAppState.latestVersionState is MedalAppState.LatestVersionState.Success) {
            val channelState by context.dataStore.read(
                MedalPreferences.CHANNEL_NAME,
                Channel.Official.channelName
            ).collectAsState(Channel.Official.channelName)
            val currentChannelState by remember {
                derivedStateOf {
                    medalAppState.channelState.takeIf { it != channelState } ?: channelState
                }
            }
            LaunchedEffect(channelState) {
                viewModel.update {
                    MedalAppState.channelState set channelState
                    MedalAppState.cardIsVisible set true
                }
            }

            LaunchedEffect(currentChannelState) {
                if (currentChannelState != channelState) {
                    context.dataStore.write(
                        MedalPreferences.CHANNEL_NAME,
                        currentChannelState,
                        this
                    ).invokeOnCompletion {
                        when (currentChannelState) {
                            "iOS" -> platformManager.switchToIOS()
                                .also { Log.d("PlatformManager", "switchToIOS") }

                            else -> Channel.entries
                                .first { it.channelName == currentChannelState }
                                .let { channel ->
                                    platformManager.switchToAndroid(channel).also {
                                        Log.d(
                                            "PlatformManager",
                                            "switchToAndroid with $channelState"
                                        )
                                    }
                                }
                        }
                    }
                }
            }

            val cardColor by animateColorAsState(
                targetValue = latestVersion[currentChannelState]?.let {
                    when (it.status) {
                        0 -> if (currentChannelState == it.channel) MedalTheme.colors.error else MedalTheme.colors.background
                        else -> if (currentChannelState == it.channel) MedalTheme.colors.success else MedalTheme.colors.background
                    }
                } ?: MedalTheme.colors.background,
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label = "CardColorTransition"
            )

            val iconScale by remember { mutableFloatStateOf(1f) }
            val animatedScale by animateFloatAsState(
                targetValue = iconScale,
                animationSpec = keyframes {
                    durationMillis = 500
                    1.2f at 150 using LinearEasing
                    0.8f at 300 using FastOutSlowInEasing
                    1f at 500
                },
                label = "IconScale"
            )

            val transition = updateTransition(currentChannelState, label = "ChannelSwitch")
            val cardElevation by transition.animateDp(
                transitionSpec = {
                    tween(durationMillis = 300, easing = FastOutSlowInEasing)
                }, label = "Elevation"
            ) { if (it == "iOS") 8.dp else 4.dp }

            AnimatedVisibility(
                visible = medalAppState.cardIsVisible,
                enter = expandVertically() + fadeIn(initialAlpha = 0.4f),
                exit = shrinkVertically() + fadeOut()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    colors = CardDefaults.cardColors(
                        containerColor = cardColor,
                        contentColor = contentColorFor(MedalTheme.colors.onBackground)
                    ),
                    onClick = { viewModel.update { MedalAppState.modalBottomSheetIsVisible set true } },
                    elevation = CardDefaults.outlinedCardElevation(defaultElevation = cardElevation),
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AnimatedContent(
                            targetState = currentChannelState,
                        ) { targetChannel ->
                            Icon(
                                imageVector = if (targetChannel == "iOS") Icons.Outlined.PhoneIphone else Icons.Outlined.Android,
                                modifier = Modifier.size(40.dp).graphicsLayer { scaleX = animatedScale; scaleY = animatedScale }
                            )
                        }
                        Column {
                            Text(
                                textRes = R.string.current_channel,
                                textStyle = typography.h3,
                                modifier = Modifier.offset(y = (-4).dp)
                            )

                            AnimatedContent(
                                targetState = currentChannelState,
                            ) { targetChannel ->
                                Text(
                                    text = "Medal 运行于 $targetChannel 渠道",
                                    style = typography.body3,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }

        val cardColor by animateColorAsState(
            targetValue = when(medalAppState.latestVersionState) {
                is MedalAppState.LatestVersionState.Loading -> MedalTheme.colors.secondary
                is MedalAppState.LatestVersionState.Success -> MedalTheme.colors.background
                is MedalAppState.LatestVersionState.Error -> MedalTheme.colors.warn
            }
        )

        ElevatedCard(
            modifier = Modifier
                .padding(0.dp)
                .animateContentSize(),
            border = BorderStroke(2.dp, contentColorFor(cardColor)),
            colors = CardDefaults.elevatedCardColors(containerColor = cardColor, contentColor = contentColorFor(cardColor))
        ) {
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
                                    textStyle = typography.body3
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
                    AnimatedContent(
                        modifier = Modifier.animateContentSize(),
                        targetState = medalAppState.latestVersionState,
                        transitionSpec = {
                            when (targetState) {
                                is MedalAppState.LatestVersionState.Success -> slideInVertically { height -> height } + fadeIn() togetherWith
                                        slideOutHorizontally { width -> -width } + fadeOut()

                                is MedalAppState.LatestVersionState.Error -> slideInHorizontally { width -> width } + fadeIn() togetherWith
                                        slideOutVertically { height -> -height } + fadeOut()

                                else -> fadeIn() togetherWith fadeOut()
                            }
                        }
                    ) { state ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            when (state) {
                                is MedalAppState.LatestVersionState.Loading -> LinearProgressIndicator()

                                is MedalAppState.LatestVersionState.Success -> {
                                    state.data.forEach { (key, value) ->
                                        Card(
                                            shape = ShapeDefaults.Small,
                                            colors = CardDefaults.cardColors(
                                                containerColor = MedalTheme.colors.onPrimary,
                                                contentColor = contentColorFor(
                                                    MedalTheme.colors.onPrimary
                                                )
                                            )
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = key,
                                                    style = typography.h4
                                                )
                                                Text(
                                                    text = value.version,
                                                    style = typography.body2
                                                )
                                            }
                                        }
                                    }
                                }

                                is MedalAppState.LatestVersionState.Error ->
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Card(
                                            shape = ShapeDefaults.ExtraSmall,
                                            colors = CardDefaults.cardColors(
                                                containerColor = MedalTheme.colors.error,
                                                contentColor = contentColorFor(
                                                    MedalTheme.colors.error
                                                )
                                            )
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
                                                        state.error.message?.let {
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