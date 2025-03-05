package smf.talkweb.medal.ui.theme.components.topbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import smf.talkweb.medal.ui.theme.MedalTheme.typography
import smf.talkweb.medal.ui.theme.components.Icon
import smf.talkweb.medal.ui.theme.components.IconButton
import smf.talkweb.medal.ui.theme.components.IconButtonVariant
import smf.talkweb.medal.ui.theme.components.Text

@Composable
fun MedalScreenTopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopBarScrollBehavior? = null,
    title: String,
    onBack: () -> Unit = {},
) {
    TopBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        colors = TopBarDefaults.topBarColors(),
    ) {
        Row(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            IconButton(
                variant = IconButtonVariant.Ghost,
                onClick = onBack,
            ) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "More Options")
            }

            Text(text = title, style = typography.h3)
        }
    }
}