package com.scalo.stocklify.core.ui.components.chips

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyTheme

@Composable
fun StocklifyFilterChip(
    label: String,
    selected: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    size: StocklifyChipSize = StocklifyChipSize.Medium,
    enabled: Boolean = true,
    contentPadding: PaddingValues? = null,
    topIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) = StocklifyChip(
    label = label,
    modifier = modifier,
    variant = StocklifyChipVariant.Filter,
    size = size,
    selected = selected,
    enabled = enabled,
    onClick = { onToggle(!selected) },
    contentPadding = contentPadding,
    topIcon = topIcon,
    leadingIcon = leadingIcon,
    trailingIcon = trailingIcon,
)

@Preview(showBackground = true)
@Composable
private fun StocklifyFilterChipPreview() {
    StocklifyTheme {
        var selectedA by remember { mutableStateOf(false) }
        var selectedB by remember { mutableStateOf(true) }
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            StocklifyFilterChip(label = "Electronics", selected = selectedA, onToggle = { selectedA = it })
            StocklifyFilterChip(label = "Beverages", selected = selectedB, onToggle = { selectedB = it })
            StocklifyFilterChip(label = "Disabled", selected = false, onToggle = {}, enabled = false)
        }
    }
}

@Preview(showBackground = true, name = "Filter Chip — Top Icon")
@Composable
private fun StocklifyFilterChipTopIconPreview() {
    StocklifyTheme {
        val colors = StocklifyTheme.colors
        var selectedA by remember { mutableStateOf(false) }
        var selectedB by remember { mutableStateOf(true) }
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            StocklifyFilterChip(
                label = "All",
                selected = selectedA,
                onToggle = { selectedA = it },
                topIcon = {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(colors.grayscale300, CircleShape)
                    )
                }
            )
            StocklifyFilterChip(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                label = "Food",
                selected = selectedB,
                onToggle = { selectedB = it },
                topIcon = {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(colors.primary300, CircleShape)
                    )
                }
            )
        }
    }
}
