package smf.talkweb.medal.ui.theme.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import smf.talkweb.medal.ui.theme.MedalTheme
import smf.talkweb.medal.ui.theme.LocalContentColor
import smf.talkweb.medal.ui.theme.contentColorFor
import smf.talkweb.medal.ui.theme.foundation.ButtonElevation

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    variant: IconButtonVariant = IconButtonVariant.Primary,
    shape: Shape = IconButtonDefaults.ButtonSquareShape,
    onClick: () -> Unit = {},
    contentPadding: PaddingValues = IconButtonDefaults.ButtonPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {
    val style = IconButtonDefaults.styleFor(variant, shape)

    IconButtonComponent(
        modifier = modifier,
        enabled = enabled,
        loading = loading,
        style = style,
        onClick = onClick,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content
    )
}

@Composable
private fun IconButtonComponent(
    modifier: Modifier,
    enabled: Boolean,
    loading: Boolean,
    style: IconButtonStyle,
    onClick: () -> Unit,
    contentPadding: PaddingValues,
    interactionSource: MutableInteractionSource,
    content: @Composable () -> Unit
) {
    val containerColor = style.colors.containerColor(enabled).value
    val contentColor = style.colors.contentColor(enabled).value
    val borderColor = style.colors.borderColor(enabled).value
    val borderStroke = if (borderColor != null) BorderStroke(IconButtonDefaults.OutlineHeight, borderColor) else null

    val shadowElevation = style.elevation?.shadowElevation(enabled, interactionSource)?.value ?: 0.dp

    Surface(
        onClick = onClick,
        modifier = modifier
            .defaultMinSize(
                minWidth = IconButtonDefaults.ButtonSize, minHeight = IconButtonDefaults.ButtonSize
            )
            .semantics { role = Role.Button },
        enabled = enabled,
        shape = style.shape,
        color = containerColor,
        contentColor = contentColor,
        border = borderStroke,
        shadowElevation = shadowElevation,
        interactionSource = interactionSource
    ) {
        Box(
            modifier = Modifier.padding(contentPadding), contentAlignment = Alignment.Center
        ) {
            // Add a loading indicator if needed
            content()
        }
    }
}

enum class IconButtonVariant {
    Primary,
    PrimaryOutlined,
    PrimaryElevated,
    PrimaryGhost,
    Secondary,
    SecondaryOutlined,
    SecondaryElevated,
    SecondaryGhost,
    Destructive,
    DestructiveOutlined,
    DestructiveElevated,
    DestructiveGhost,
    Ghost
}

internal object IconButtonDefaults {
    val ButtonSize = 44.dp
    val ButtonPadding = PaddingValues(4.dp)
    val ButtonSquareShape = RoundedCornerShape(12.dp)
    val ButtonCircleShape = RoundedCornerShape(percent = 50)
    val OutlineHeight = 1.dp

    @Composable
    fun buttonElevation() = ButtonElevation(
        defaultElevation = 2.dp,
        pressedElevation = 2.dp,
        focusedElevation = 2.dp,
        hoveredElevation = 2.dp,
        disabledElevation = 0.dp
    )

    @Composable
    fun styleFor(variant: IconButtonVariant, shape: Shape): IconButtonStyle {
        return when (variant) {
            IconButtonVariant.Primary -> primaryFilled(shape)
            IconButtonVariant.PrimaryOutlined -> primaryOutlined(shape)
            IconButtonVariant.PrimaryElevated -> primaryElevated(shape)
            IconButtonVariant.PrimaryGhost -> primaryGhost(shape)
            IconButtonVariant.Secondary -> secondaryFilled(shape)
            IconButtonVariant.SecondaryOutlined -> secondaryOutlined(shape)
            IconButtonVariant.SecondaryElevated -> secondaryElevated(shape)
            IconButtonVariant.SecondaryGhost -> secondaryGhost(shape)
            IconButtonVariant.Destructive -> destructiveFilled(shape)
            IconButtonVariant.DestructiveOutlined -> destructiveOutlined(shape)
            IconButtonVariant.DestructiveElevated -> destructiveElevated(shape)
            IconButtonVariant.DestructiveGhost -> destructiveGhost(shape)
            IconButtonVariant.Ghost -> ghost(shape)
        }
    }

    @Composable
    fun primaryFilled(shape: Shape) = IconButtonStyle(
        colors = IconButtonColors(
            containerColor = MedalTheme.colors.primary,
            contentColor = MedalTheme.colors.onPrimary,
            disabledContainerColor = MedalTheme.colors.disabled,
            disabledContentColor = MedalTheme.colors.onDisabled
        ), shape = shape, elevation = null
    )

    @Composable
    fun primaryOutlined(shape: Shape) = IconButtonStyle(
        colors = IconButtonColors(
            containerColor = MedalTheme.colors.transparent,
            contentColor = MedalTheme.colors.primary,
            borderColor = MedalTheme.colors.primary,
            disabledContainerColor = MedalTheme.colors.transparent,
            disabledContentColor = MedalTheme.colors.onDisabled,
            disabledBorderColor = MedalTheme.colors.disabled
        ), shape = shape, elevation = null
    )

    @Composable
    fun primaryElevated(shape: Shape) = IconButtonStyle(
        colors = IconButtonColors(
            containerColor = MedalTheme.colors.primary,
            contentColor = MedalTheme.colors.onPrimary,
            disabledContainerColor = MedalTheme.colors.disabled,
            disabledContentColor = MedalTheme.colors.onDisabled
        ), shape = shape, elevation = buttonElevation()
    )

    @Composable
    fun primaryGhost(shape: Shape) = IconButtonStyle(
        colors = IconButtonColors(
            containerColor = MedalTheme.colors.transparent,
            contentColor = MedalTheme.colors.primary,
            borderColor = MedalTheme.colors.transparent,
            disabledContainerColor = MedalTheme.colors.transparent,
            disabledContentColor = MedalTheme.colors.onDisabled,
            disabledBorderColor = MedalTheme.colors.transparent
        ), shape = shape, elevation = null
    )

    @Composable
    fun secondaryFilled(shape: Shape) = IconButtonStyle(
        colors = IconButtonColors(
            containerColor = MedalTheme.colors.secondary,
            contentColor = MedalTheme.colors.onSecondary,
            disabledContainerColor = MedalTheme.colors.disabled,
            disabledContentColor = MedalTheme.colors.onDisabled
        ), shape = shape, elevation = null
    )

    @Composable
    fun secondaryOutlined(shape: Shape) = IconButtonStyle(
        colors = IconButtonColors(
            containerColor = MedalTheme.colors.transparent,
            contentColor = MedalTheme.colors.secondary,
            borderColor = MedalTheme.colors.secondary,
            disabledContainerColor = MedalTheme.colors.transparent,
            disabledContentColor = MedalTheme.colors.onDisabled,
            disabledBorderColor = MedalTheme.colors.disabled
        ), shape = shape, elevation = null
    )

    @Composable
    fun secondaryElevated(shape: Shape) = IconButtonStyle(
        colors = IconButtonColors(
            containerColor = MedalTheme.colors.secondary,
            contentColor = MedalTheme.colors.onSecondary,
            disabledContainerColor = MedalTheme.colors.disabled,
            disabledContentColor = MedalTheme.colors.onDisabled
        ), shape = shape, elevation = buttonElevation()
    )

    @Composable
    fun secondaryGhost(shape: Shape) = IconButtonStyle(
        colors = IconButtonColors(
            containerColor = MedalTheme.colors.transparent,
            contentColor = MedalTheme.colors.secondary,
            borderColor = MedalTheme.colors.transparent,
            disabledContainerColor = MedalTheme.colors.transparent,
            disabledContentColor = MedalTheme.colors.onDisabled,
            disabledBorderColor = MedalTheme.colors.transparent
        ), shape = shape, elevation = null
    )

    @Composable
    fun destructiveFilled(shape: Shape) = IconButtonStyle(
        colors = IconButtonColors(
            containerColor = MedalTheme.colors.error,
            contentColor = MedalTheme.colors.onError,
            disabledContainerColor = MedalTheme.colors.disabled,
            disabledContentColor = MedalTheme.colors.onDisabled
        ), shape = shape, elevation = null
    )

    @Composable
    fun destructiveOutlined(shape: Shape) = IconButtonStyle(
        colors = IconButtonColors(
            containerColor = MedalTheme.colors.transparent,
            contentColor = MedalTheme.colors.error,
            borderColor = MedalTheme.colors.error,
            disabledContainerColor = MedalTheme.colors.transparent,
            disabledContentColor = MedalTheme.colors.onDisabled,
            disabledBorderColor = MedalTheme.colors.disabled
        ), shape = shape, elevation = null
    )

    @Composable
    fun destructiveElevated(shape: Shape) = IconButtonStyle(
        colors = IconButtonColors(
            containerColor = MedalTheme.colors.error,
            contentColor = MedalTheme.colors.onError,
            disabledContainerColor = MedalTheme.colors.disabled,
            disabledContentColor = MedalTheme.colors.onDisabled
        ), shape = shape, elevation = buttonElevation()
    )

    @Composable
    fun destructiveGhost(shape: Shape) = IconButtonStyle(
        colors = IconButtonColors(
            containerColor = MedalTheme.colors.transparent,
            contentColor = MedalTheme.colors.error,
            borderColor = MedalTheme.colors.transparent,
            disabledContainerColor = MedalTheme.colors.transparent,
            disabledContentColor = MedalTheme.colors.onDisabled,
            disabledBorderColor = MedalTheme.colors.transparent
        ), shape = shape, elevation = null
    )

    @Composable
    fun ghost(shape: Shape) = IconButtonStyle(
        colors = IconButtonColors(
            containerColor = MedalTheme.colors.transparent,
            contentColor = LocalContentColor.current,
            disabledContainerColor = MedalTheme.colors.transparent,
            disabledContentColor = MedalTheme.colors.onDisabled
        ), shape = shape, elevation = null
    )
}

@Immutable
data class IconButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val borderColor: Color? = null,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
    val disabledBorderColor: Color? = null
) {
    @Composable
    fun containerColor(enabled: Boolean) = rememberUpdatedState(if (enabled) containerColor else disabledContainerColor)

    @Composable
    fun contentColor(enabled: Boolean) = rememberUpdatedState(if (enabled) contentColor else disabledContentColor)

    @Composable
    fun borderColor(enabled: Boolean) = rememberUpdatedState(if (enabled) borderColor else disabledBorderColor)
}

@Immutable
data class IconButtonStyle(
    val colors: IconButtonColors, val shape: Shape, val elevation: ButtonElevation? = null
)

@Composable
@Preview("Primary Icon Buttons")
fun PrimaryIconButtonPreview() {
    MedalTheme {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Primary Icon Buttons", style = MedalTheme.typography.h2)

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                IconButton(variant = IconButtonVariant.Primary) {
                    Icon(Icons.Filled.AcUnit, contentDescription = "PrimaryFilled")
                }
                IconButton(variant = IconButtonVariant.PrimaryOutlined) {
                    Icon(Icons.Filled.AcUnit, contentDescription = "PrimaryOutlined")
                }
                IconButton(variant = IconButtonVariant.PrimaryElevated) {
                    Icon(Icons.Filled.AcUnit, contentDescription = "PrimaryElevated")
                }
                IconButton(variant = IconButtonVariant.PrimaryGhost) {
                    Icon(Icons.Filled.AcUnit, contentDescription = "PrimaryGhost")
                }
            }
        }
    }
}

@Composable
@Preview("Secondary Icon Buttons")
fun SecondaryIconButtonPreview() {
    MedalTheme {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Secondary Icon Buttons", style = MedalTheme.typography.h2)

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                IconButton(variant = IconButtonVariant.Secondary) {
                    Icon(Icons.Filled.AcUnit, contentDescription = "SecondaryFilled")
                }
                IconButton(variant = IconButtonVariant.SecondaryOutlined) {
                    Icon(Icons.Filled.AcUnit, contentDescription = "SecondaryOutlined")
                }
                IconButton(variant = IconButtonVariant.SecondaryElevated) {
                    Icon(Icons.Filled.AcUnit, contentDescription = "SecondaryElevated")
                }
                IconButton(variant = IconButtonVariant.SecondaryGhost) {
                    Icon(Icons.Filled.AcUnit, contentDescription = "SecondaryGhost")
                }
            }
        }
    }
}

@Composable
@Preview("Destructive Icon Buttons")
fun DestructiveIconButtonPreview() {
    MedalTheme {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Destructive Icon Buttons", style = MedalTheme.typography.h2)

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                IconButton(variant = IconButtonVariant.Destructive) {
                    Icon(Icons.Filled.AcUnit, contentDescription = "DestructiveFilled")
                }
                IconButton(variant = IconButtonVariant.DestructiveOutlined) {
                    Icon(Icons.Filled.AcUnit, contentDescription = "DestructiveOutlined")
                }
                IconButton(variant = IconButtonVariant.DestructiveElevated) {
                    Icon(Icons.Filled.AcUnit, contentDescription = "DestructiveElevated")
                }
                IconButton(variant = IconButtonVariant.DestructiveGhost) {
                    Icon(Icons.Filled.AcUnit, contentDescription = "DestructiveGhost")
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
@Preview("Ghost Icon Buttons")
fun GhostIconButtonPreview() {
    MedalTheme {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Ghost Icon Buttons", style = MedalTheme.typography.h2)

            FlowRow(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(8))
                        .background(MedalTheme.colors.background),
                    contentAlignment = Alignment.Center
                ) {
                    CompositionLocalProvider(LocalContentColor provides contentColorFor(color = MedalTheme.colors.background)) {
                        IconButton(variant = IconButtonVariant.Ghost) {
                            Icon(Icons.Filled.AcUnit, contentDescription = "DestructiveFilled")
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(8))
                        .background(MedalTheme.colors.primary),
                    contentAlignment = Alignment.Center
                ) {
                    CompositionLocalProvider(LocalContentColor provides contentColorFor(color = MedalTheme.colors.primary)) {
                        IconButton(variant = IconButtonVariant.Ghost) {
                            Icon(Icons.Filled.AcUnit, contentDescription = "DestructiveFilled")
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(8))
                        .background(MedalTheme.colors.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    CompositionLocalProvider(LocalContentColor provides contentColorFor(color = MedalTheme.colors.secondary)) {
                        IconButton(variant = IconButtonVariant.Ghost) {
                            Icon(Icons.Filled.AcUnit, contentDescription = "DestructiveFilled")
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(8))
                        .background(MedalTheme.colors.tertiary),
                    contentAlignment = Alignment.Center
                ) {
                    CompositionLocalProvider(LocalContentColor provides contentColorFor(color = MedalTheme.colors.tertiary)) {
                        IconButton(variant = IconButtonVariant.Ghost) {
                            Icon(Icons.Filled.AcUnit, contentDescription = "DestructiveFilled")
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(8))
                        .background(MedalTheme.colors.surface),
                    contentAlignment = Alignment.Center
                ) {
                    CompositionLocalProvider(LocalContentColor provides contentColorFor(color = MedalTheme.colors.surface)) {
                        IconButton(variant = IconButtonVariant.Ghost) {
                            Icon(Icons.Filled.AcUnit, contentDescription = "DestructiveFilled")
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(8))
                        .background(MedalTheme.colors.error),
                    contentAlignment = Alignment.Center
                ) {
                    CompositionLocalProvider(LocalContentColor provides contentColorFor(color = MedalTheme.colors.error)) {
                        IconButton(variant = IconButtonVariant.Ghost) {
                            Icon(Icons.Filled.AcUnit, contentDescription = "DestructiveFilled")
                        }
                    }
                }


            }
        }
    }
}

@Composable
@Preview("All Icon Button Shapes")
fun IconButtonShapesPreview() {
    MedalTheme {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Square Shape", style = MedalTheme.typography.h2)

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                IconButton(
                    variant = IconButtonVariant.Primary, shape = IconButtonDefaults.ButtonSquareShape
                ) {
                    Icon(Icons.Filled.AcUnit, contentDescription = "SquareShape")
                }
                IconButton(
                    variant = IconButtonVariant.PrimaryOutlined, shape = IconButtonDefaults.ButtonSquareShape
                ) {
                    Icon(Icons.Filled.AcUnit, contentDescription = "SquareShapeOutlined")
                }
            }

            Text(text = "Circle Shape", style = MedalTheme.typography.h2)

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                IconButton(
                    variant = IconButtonVariant.Primary, shape = IconButtonDefaults.ButtonCircleShape
                ) {
                    Icon(Icons.Filled.AcUnit, contentDescription = "CircleShape")
                }
                IconButton(
                    variant = IconButtonVariant.PrimaryOutlined, shape = IconButtonDefaults.ButtonCircleShape
                ) {
                    Icon(Icons.Filled.AcUnit, contentDescription = "CircleShapeOutlined")
                }
            }
        }
    }
}