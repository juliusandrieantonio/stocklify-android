package com.scalo.stocklify.core.ui.components.chips

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.scalo.stocklify.core.ui.theme.StocklifyTheme

@Composable
fun StocklifyAssistChip(
    label: String,
    modifier: Modifier = Modifier,
    size: StocklifyChipSize = StocklifyChipSize.Medium,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues? = null,
    topIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) = StocklifyChip(
    label = label,
    modifier = modifier,
    variant = StocklifyChipVariant.Assist,
    size = size,
    enabled = enabled,
    onClick = onClick,
    contentPadding = contentPadding,
    topIcon = topIcon,
    leadingIcon = leadingIcon,
    trailingIcon = trailingIcon,
)

@Preview(showBackground = true)
@Composable
private fun StocklifyAssistChipPreview() {
    StocklifyTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            StocklifyAssistChip(label = "Category", onClick = {})
            StocklifyAssistChip(label = "Small", size = StocklifyChipSize.Small, onClick = {})
            StocklifyAssistChip(label = "Disabled", enabled = false)
            StocklifyAssistChip(
                label = "Top Icon",
                onClick = {},
                topIcon = {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(StocklifyTheme.colors.primary300, CircleShape)
                    )
                }
            )
        }
    }
}
