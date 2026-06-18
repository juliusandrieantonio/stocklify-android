package com.scalo.stocklify.core.ui.components.buttons

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.scalo.stocklify.core.ui.theme.StocklifyTheme

@Composable
fun StocklifyPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: StocklifyButtonSize = StocklifyButtonSize.Medium,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    fullWidth: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) = StocklifyButton(
    text = text,
    onClick = onClick,
    modifier = modifier,
    variant = StocklifyButtonVariant.Primary,
    size = size,
    isLoading = isLoading,
    enabled = enabled,
    fullWidth = fullWidth,
    leadingIcon = leadingIcon,
    trailingIcon = trailingIcon
)

@Preview(name = "Primary Buttons", showBackground = true)
@Composable
private fun StocklifyPrimaryButtonPreview() {
    StocklifyTheme {
        StocklifyPrimaryButton(
            text = "Large",
            onClick = {},
            size = StocklifyButtonSize.Medium,
            fullWidth = true
        )
    }
}
