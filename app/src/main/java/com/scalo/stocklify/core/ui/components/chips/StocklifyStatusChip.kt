package com.scalo.stocklify.core.ui.components.chips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.scalo.stocklify.core.ui.theme.StocklifyTheme

enum class StocklifyStatusChipType { Success, Error, Warning, Info }

@Composable
fun StocklifyStatusChip(
    label: String,
    type: StocklifyStatusChipType,
    modifier: Modifier = Modifier,
    size: StocklifyChipSize = StocklifyChipSize.Medium,
    contentPadding: PaddingValues? = null,
) {
    val colors = StocklifyTheme.colors
    val radius = StocklifyTheme.radius

    val (containerColor, contentColor) = when (type) {
        StocklifyStatusChipType.Success -> Color(0xFFE8F5E9) to colors.success
        StocklifyStatusChipType.Error -> Color(0xFFFFEBEE) to colors.error
        StocklifyStatusChipType.Warning -> Color(0xFFFFF3E0) to colors.warning
        StocklifyStatusChipType.Info -> colors.primary100 to colors.primary600
    }

    val resolvedPadding = contentPadding ?: PaddingValues(
        horizontal = when (size) {
            StocklifyChipSize.Small -> 8.dp
            StocklifyChipSize.Medium -> 12.dp
        },
        vertical = when (size) {
            StocklifyChipSize.Small -> 4.dp
            StocklifyChipSize.Medium -> 6.dp
        },
    )
    val textStyle = StocklifyTheme.textStyles.subtitleXs

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(radius.full),
        color = containerColor,
        contentColor = contentColor,
    ) {
        Text(
            text = label,
            style = textStyle,
            modifier = Modifier.padding(resolvedPadding),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StocklifyStatusChipPreview() {
    StocklifyTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            StocklifyStatusChip(label = "In Stock", type = StocklifyStatusChipType.Success)
            StocklifyStatusChip(label = "Out of Stock", type = StocklifyStatusChipType.Error)
            StocklifyStatusChip(label = "Low Stock", type = StocklifyStatusChipType.Warning)
            StocklifyStatusChip(label = "Info", type = StocklifyStatusChipType.Info)
        }
    }
}
