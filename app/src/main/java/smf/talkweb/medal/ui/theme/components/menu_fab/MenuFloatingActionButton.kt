package smf.talkweb.medal.ui.theme.components.menu_fab

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import smf.talkweb.medal.ui.theme.MedalTheme

@Composable
fun MenuFloatingActionButton(
    srcIcon: ImageVector,
    items: SnapshotStateList<MenuFabItem>,
    modifier: Modifier = Modifier,
    menuFabState: MenuFabState = rememberMenuFabState(),
    srcIconColor: Color = Color.White,
    fabBackgroundColor: Color = Color.Unspecified,
    showLabels: Boolean = true,
    onFabItemClicked: (item: MenuFabItem) -> Unit
) {
    val transition = updateTransition(
        targetState = menuFabState.menuFabStateEnum.value,
        label = "menuFabStateEnum"
    )

    val rotateAnim: Float by transition.animateFloat(
        transitionSpec = {
            if (targetState == MenuFabStateEnum.Expanded) {
                spring(stiffness = Spring.StiffnessLow)
            } else {
                spring(stiffness = Spring.StiffnessMedium)
            }
        }, label = "rotateAnim"
    ) { state ->
        if (state == MenuFabStateEnum.Collapsed) 0F else -45F
    }

    val alphaAnim: Float by transition.animateFloat(transitionSpec = {
        tween(durationMillis = 200)
    }, label = "alphaAnim") { state ->
        if (state == MenuFabStateEnum.Expanded) 1F else 0F
    }

    Box(modifier = modifier, contentAlignment = Alignment.BottomEnd) {
        items.forEachIndexed { index, item ->
            val shrinkAnim by transition.animateFloat(targetValueByState = { state ->
                when (state) {
                    MenuFabStateEnum.Collapsed -> 5F
                    MenuFabStateEnum.Expanded -> (index + 1) * 60F + if (index == 0) 5F else 0F
                }
            }, label = "shrinkAnim", transitionSpec = {
                if (targetState == MenuFabStateEnum.Expanded) {
                    spring(stiffness = Spring.StiffnessLow, dampingRatio = 0.58F)
                } else {
                    spring(stiffness = Spring.StiffnessMedium)
                }
            })
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(
                        bottom = shrinkAnim.dp,
                        top = 5.dp,
                        end = 5.dp
                    )
                    .alpha(alphaAnim)
            ) {
                if (showLabels) {
                    Text(
                        item.label,
                        color = item.labelTextColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .alpha(alphaAnim)
                            .background(color = item.labelBackgroundColor, shape = RoundedCornerShape(8.dp))
                            .padding(start = 6.dp, end = 6.dp, top = 4.dp, bottom = 4.dp),
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                FloatingActionButton(
                    containerColor = if (item.fabBackgroundColor == Color.Unspecified) MedalTheme.colors.primary else item.fabBackgroundColor,
                    modifier = Modifier.size(46.dp),
                    onClick = {
                        menuFabState.menuFabStateEnum.value = MenuFabStateEnum.Collapsed
                        onFabItemClicked(item)
                    },
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 4.dp
                    )
                ) {
                    item.icon()
                }
            }
        }

        FloatingActionButton(
            containerColor = if (fabBackgroundColor == Color.Unspecified) MedalTheme.colors.primary else fabBackgroundColor,
            onClick = {
                menuFabState.menuFabStateEnum.value =
                    if (menuFabState.menuFabStateEnum.value == MenuFabStateEnum.Collapsed) MenuFabStateEnum.Expanded else MenuFabStateEnum.Collapsed
            }) {
            Icon(
                imageVector = srcIcon,
                modifier = Modifier.rotate(rotateAnim),
                tint = srcIconColor,
                contentDescription = null
            )
        }
    }
}