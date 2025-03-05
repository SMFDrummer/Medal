package smf.talkweb.medal.ui.theme.components.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import smf.talkweb.medal.ui.theme.MedalTheme
import smf.talkweb.medal.ui.theme.components.textfield.base.CommonDecorationBox
import smf.talkweb.medal.ui.theme.components.textfield.base.FocusedOutlineThickness
import smf.talkweb.medal.ui.theme.components.textfield.base.HorizontalIconPadding
import smf.talkweb.medal.ui.theme.components.textfield.base.SupportingTopPadding
import smf.talkweb.medal.ui.theme.components.textfield.base.TextFieldColors
import smf.talkweb.medal.ui.theme.components.textfield.base.TextFieldHorizontalPadding
import smf.talkweb.medal.ui.theme.components.textfield.base.TextFieldMinHeight
import smf.talkweb.medal.ui.theme.components.textfield.base.TextFieldVerticalPadding
import smf.talkweb.medal.ui.theme.components.textfield.base.UnfocusedOutlineThickness
import smf.talkweb.medal.ui.theme.components.textfield.base.containerUnderline

@Composable
fun UnderlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = MedalTheme.typography.input,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    placeholder: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    colors: TextFieldColors = UnderlinedTextFieldDefaults.colors(),
    cursorBrush: Brush = SolidColor(colors.cursorColor(isError).value),
) {
    val textColor =
        textStyle.color.takeOrElse {
            colors.textColor(enabled, isError, interactionSource).value
        }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    CompositionLocalProvider(LocalTextSelectionColors provides colors.selectionColors) {
        BasicTextField(
            modifier =
                modifier
                    .defaultMinSize(
                        minHeight = UnderlinedTextFieldDefaults.MinHeight,
                    )
                    .fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = mergedTextStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            visualTransformation = visualTransformation,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
            cursorBrush = cursorBrush,
            decorationBox = @Composable { innerTextField ->
                UnderlinedTextFieldDefaults.DecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    visualTransformation = visualTransformation,
                    label = label,
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    prefix = prefix,
                    suffix = suffix,
                    supportingText = supportingText,
                    enabled = enabled,
                    isError = isError,
                    interactionSource = interactionSource,
                    colors = UnderlinedTextFieldDefaults.colors(),
                )
            },
        )
    }
}

@Composable
fun UnderlinedTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = MedalTheme.typography.input,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    placeholder: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    colors: TextFieldColors = UnderlinedTextFieldDefaults.colors(),
    cursorBrush: Brush = SolidColor(colors.cursorColor(isError).value),
) {
    val textColor =
        textStyle.color.takeOrElse {
            colors.textColor(enabled, isError, interactionSource).value
        }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    CompositionLocalProvider(LocalTextSelectionColors provides colors.selectionColors) {
        BasicTextField(
            modifier =
                modifier
                    .defaultMinSize(
                        minHeight = UnderlinedTextFieldDefaults.MinHeight,
                    )
                    .fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = mergedTextStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            visualTransformation = visualTransformation,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
            cursorBrush = cursorBrush,
            decorationBox = @Composable { innerTextField ->
                UnderlinedTextFieldDefaults.DecorationBox(
                    value = value.text,
                    innerTextField = innerTextField,
                    visualTransformation = visualTransformation,
                    label = label,
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    prefix = prefix,
                    suffix = suffix,
                    supportingText = supportingText,
                    enabled = enabled,
                    isError = isError,
                    interactionSource = interactionSource,
                    colors = UnderlinedTextFieldDefaults.colors(),
                )
            },
        )
    }
}

@Immutable
internal object UnderlinedTextFieldDefaults {
    val MinHeight = TextFieldMinHeight

    private fun contentPadding(
        start: Dp = 0.dp,
        end: Dp = 0.dp,
        top: Dp = TextFieldVerticalPadding,
        bottom: Dp = TextFieldVerticalPadding,
    ): PaddingValues = PaddingValues(start, top, end, bottom)

    private fun labelPadding(
        start: Dp = 0.dp,
        top: Dp = 0.dp,
        end: Dp = 0.dp,
        bottom: Dp = 0.dp,
    ): PaddingValues = PaddingValues(start, top, end, bottom)

    private fun supportingTextPadding(
        start: Dp = 0.dp,
        top: Dp = SupportingTopPadding,
        end: Dp = TextFieldHorizontalPadding,
        bottom: Dp = 0.dp,
    ): PaddingValues = PaddingValues(start, top, end, bottom)

    @Composable
    private fun leadingIconPadding(
        start: Dp = 0.dp,
        top: Dp = 0.dp,
        end: Dp = HorizontalIconPadding / 2,
        bottom: Dp = 0.dp,
    ): PaddingValues = PaddingValues(start, top, end, bottom)

    @Composable
    private fun trailingIconPadding(
        start: Dp = HorizontalIconPadding / 2,
        top: Dp = 0.dp,
        end: Dp = 0.dp,
        bottom: Dp = 0.dp,
    ): PaddingValues = PaddingValues(start, top, end, bottom)

    @Composable
    fun containerBorderThickness(
        interactionSource: InteractionSource,
    ): Dp {
        val focused by interactionSource.collectIsFocusedAsState()

        return if (focused) FocusedOutlineThickness else UnfocusedOutlineThickness
    }

    @Composable
    fun DecorationBox(
        value: String,
        innerTextField: @Composable () -> Unit,
        enabled: Boolean,
        visualTransformation: VisualTransformation,
        interactionSource: InteractionSource,
        isError: Boolean = false,
        label: @Composable (() -> Unit)? = null,
        placeholder: @Composable (() -> Unit)? = null,
        leadingIcon: @Composable (() -> Unit)? = null,
        trailingIcon: @Composable (() -> Unit)? = null,
        prefix: @Composable (() -> Unit)? = null,
        suffix: @Composable (() -> Unit)? = null,
        supportingText: @Composable (() -> Unit)? = null,
        colors: TextFieldColors = colors(),
        container: @Composable () -> Unit = {
            ContainerBox(enabled, isError, interactionSource, colors)
        },
    ) {
        CommonDecorationBox(
            value = value,
            innerTextField = innerTextField,
            visualTransformation = visualTransformation,
            placeholder = placeholder,
            label = label,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            prefix = prefix,
            suffix = suffix,
            supportingText = supportingText,
            enabled = enabled,
            isError = isError,
            interactionSource = interactionSource,
            colors = colors,
            contentPadding = contentPadding(),
            labelPadding = labelPadding(),
            supportingTextPadding = supportingTextPadding(),
            leadingIconPadding = leadingIconPadding(),
            trailingIconPadding = trailingIconPadding(),
            container = container,
        )
    }

    @Composable
    fun ContainerBox(
        enabled: Boolean,
        isError: Boolean,
        interactionSource: InteractionSource,
        colors: TextFieldColors,
        borderThickness: Dp = containerBorderThickness(interactionSource),
    ) {
        Box(
            Modifier
                .background(colors.containerColor(enabled, isError, interactionSource).value)
                .containerUnderline(enabled, isError, interactionSource, colors, borderThickness),
        )
    }

    @Composable
    fun colors(): TextFieldColors {
        return TextFieldColors(
            focusedTextColor = MedalTheme.colors.text,
            unfocusedTextColor = MedalTheme.colors.text,
            disabledTextColor = MedalTheme.colors.onDisabled,
            errorTextColor = MedalTheme.colors.text,
            focusedContainerColor = MedalTheme.colors.transparent,
            unfocusedContainerColor = MedalTheme.colors.transparent,
            disabledContainerColor = MedalTheme.colors.transparent,
            errorContainerColor = MedalTheme.colors.transparent,
            cursorColor = MedalTheme.colors.primary,
            errorCursorColor = MedalTheme.colors.error,
            textSelectionColors = LocalTextSelectionColors.current,
            focusedOutlineColor = MedalTheme.colors.primary,
            unfocusedOutlineColor = MedalTheme.colors.secondary,
            disabledOutlineColor = MedalTheme.colors.disabled,
            errorOutlineColor = MedalTheme.colors.error,
            focusedLeadingIconColor = MedalTheme.colors.primary,
            unfocusedLeadingIconColor = MedalTheme.colors.primary,
            disabledLeadingIconColor = MedalTheme.colors.onDisabled,
            errorLeadingIconColor = MedalTheme.colors.primary,
            focusedTrailingIconColor = MedalTheme.colors.primary,
            unfocusedTrailingIconColor = MedalTheme.colors.primary,
            disabledTrailingIconColor = MedalTheme.colors.onDisabled,
            errorTrailingIconColor = MedalTheme.colors.primary,
            focusedLabelColor = MedalTheme.colors.primary,
            unfocusedLabelColor = MedalTheme.colors.primary,
            disabledLabelColor = MedalTheme.colors.textDisabled,
            errorLabelColor = MedalTheme.colors.error,
            focusedPlaceholderColor = MedalTheme.colors.textSecondary,
            unfocusedPlaceholderColor = MedalTheme.colors.textSecondary,
            disabledPlaceholderColor = MedalTheme.colors.textDisabled,
            errorPlaceholderColor = MedalTheme.colors.textSecondary,
            focusedSupportingTextColor = MedalTheme.colors.primary,
            unfocusedSupportingTextColor = MedalTheme.colors.primary,
            disabledSupportingTextColor = MedalTheme.colors.textDisabled,
            errorSupportingTextColor = MedalTheme.colors.error,
            focusedPrefixColor = MedalTheme.colors.primary,
            unfocusedPrefixColor = MedalTheme.colors.primary,
            disabledPrefixColor = MedalTheme.colors.onDisabled,
            errorPrefixColor = MedalTheme.colors.primary,
            focusedSuffixColor = MedalTheme.colors.primary,
            unfocusedSuffixColor = MedalTheme.colors.primary,
            disabledSuffixColor = MedalTheme.colors.onDisabled,
            errorSuffixColor = MedalTheme.colors.primary,
        )
    }
}
