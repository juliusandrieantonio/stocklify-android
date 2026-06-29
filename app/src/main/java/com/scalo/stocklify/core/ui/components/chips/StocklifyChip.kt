package com.scalo.stocklify.core.ui.components.chips

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.scalo.stocklify.core.ui.theme.StocklifyTheme

enum class StocklifyChipSize { Small, Medium, Large }
enum class StocklifyChipVariant { Filter, Assist, Status }

@Composable
internal fun StocklifyChip(
    label: String,
    modifier: Modifier = Modifier,
    variant: StocklifyChipVariant = StocklifyChipVariant.Assist,
    size: StocklifyChipSize = StocklifyChipSize.Medium,
    selected: Boolean = false,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues? = null,
    topIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    val colors = StocklifyTheme.colors
    val radius = StocklifyTheme.radius
    val stroke = StocklifyTheme.stroke

    val defaultHorizontalPadding = when (size) {
        StocklifyChipSize.Small -> 8.dp
        StocklifyChipSize.Medium -> 12.dp
        StocklifyChipSize.Large -> 16.dp
    }
    val defaultVerticalPadding = when (size) {
        StocklifyChipSize.Small -> 4.dp
        StocklifyChipSize.Medium -> 6.dp
        StocklifyChipSize.Large -> 8.dp
    }
    val textStyle = when (size) {
        StocklifyChipSize.Small -> StocklifyTheme.textStyles.subtitleXxs
        StocklifyChipSize.Medium -> StocklifyTheme.textStyles.subtitleXs
        StocklifyChipSize.Large -> StocklifyTheme.textStyles.subtitleXs
    }
    val minHeight = when (size) {
        StocklifyChipSize.Small -> 24.dp
        StocklifyChipSize.Medium -> 32.dp
        StocklifyChipSize.Large -> 48.dp
    }

    val (containerColor, contentColor, borderColor) = when (variant) {
        StocklifyChipVariant.Filter -> if (selected) {
            Triple(colors.primary700, Color.White, Color.Transparent)
        } else {
            Triple(colors.base, colors.typography200, colors.grayscale300)
        }
        StocklifyChipVariant.Assist -> Triple(colors.grayscale100, colors.typography200, Color.Transparent)
        StocklifyChipVariant.Status -> Triple(colors.primary100, colors.primary600, Color.Transparent)
    }

    val disabledContainerColor = colors.grayscale100
    val disabledContentColor = colors.grayscale400

    val shape = RoundedCornerShape(radius.s)

    val chipModifier = modifier
        .defaultMinSize(minHeight = minHeight)
        .clip(shape)
        .then(
            if (borderColor != Color.Transparent)
                Modifier.border(width = stroke.s, color = borderColor, shape = shape)
            else Modifier
        )
        .then(
            if (onClick != null && enabled)
                Modifier.clickable(onClick = onClick)
            else Modifier
        )

    val resolvedPadding = contentPadding
        ?: PaddingValues(horizontal = defaultHorizontalPadding, vertical = defaultVerticalPadding)

    Surface(
        modifier = chipModifier,
        shape = shape,
        color = if (enabled) containerColor else disabledContainerColor,
        contentColor = if (enabled) contentColor else disabledContentColor,
    ) {
        if (topIcon != null) {
            Column(
                modifier = Modifier.padding(resolvedPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                topIcon()
                Spacer(Modifier.height(4.dp))
                Text(text = label, style = textStyle)
            }
        } else {
            Row(
                modifier = Modifier.padding(resolvedPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                leadingIcon?.let {
                    it()
                    Spacer(Modifier.width(4.dp))
                }
                Text(text = label, style = textStyle)
                trailingIcon?.let {
                    Spacer(Modifier.width(4.dp))
                    it()
                }
            }
        }
    }
}
