package com.scalo.stocklify.core.ui.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.scalo.stocklify.core.ui.theme.StocklifyStaticColors
import com.scalo.stocklify.core.ui.theme.StocklifyTheme

enum class StocklifyCardVariant { Primary, Elevated, Outline, Ghost }

@Composable
internal fun StocklifyCard(
    title: String,
    content: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    variant: StocklifyCardVariant = StocklifyCardVariant.Elevated,
    padding: PaddingValues = PaddingValues(StocklifyTheme.spacing.l),
    backgroundColor: Color? = null,
    contentColor: Color? = null,
    titleStyle: TextStyle? = null,
    contentStyle: TextStyle? = null,
    titleColor: Color? = null,
    gap: Dp? = null,
    leadingIconSize: Dp? = null,
    trailingIconSize: Dp? = null,
    leadingIcon: Painter? = null,
    leadingIconTint: Color? = null,
    trailingIcon: Painter? = null,
    trailingIconTint: Color? = null,
) {
    val colors = StocklifyTheme.colors
    val radius = StocklifyTheme.radius
    val strokes = StocklifyTheme.stroke
    val spacing = StocklifyTheme.spacing

    val resolvedBackground = backgroundColor ?: when (variant) {
        StocklifyCardVariant.Primary  -> colors.primary100
        StocklifyCardVariant.Elevated -> StocklifyStaticColors.white
        StocklifyCardVariant.Outline  -> Color.Transparent
        StocklifyCardVariant.Ghost    -> StocklifyStaticColors.white
    }

    val resolvedTitleColor = contentColor ?: when (variant) {
        StocklifyCardVariant.Primary  -> colors.primary700
        StocklifyCardVariant.Elevated -> colors.primary700
        StocklifyCardVariant.Outline  -> colors.primary700
        StocklifyCardVariant.Ghost    -> colors.primary700
    }

    val resolvedContentColor = contentColor ?: when (variant) {
        StocklifyCardVariant.Primary  -> colors.typography400
        StocklifyCardVariant.Elevated -> colors.typography400
        StocklifyCardVariant.Outline  -> colors.typography400
        StocklifyCardVariant.Ghost    -> colors.typography400
    }

    val borerColor = contentColor ?: when (variant) {
        StocklifyCardVariant.Primary  -> colors.primary300
        StocklifyCardVariant.Elevated -> StocklifyStaticColors.white
        StocklifyCardVariant.Outline  -> colors.grayscale200
        StocklifyCardVariant.Ghost    -> colors.grayscale200
    }

    val elevation = when (variant) {
        StocklifyCardVariant.Primary  -> 0.dp
        StocklifyCardVariant.Elevated -> 2.dp
        StocklifyCardVariant.Outline  -> 0.dp
        StocklifyCardVariant.Ghost    -> 0.dp
    }

    val shape = RoundedCornerShape(radius.s)

    val surfaceModifier = modifier
        .fillMaxWidth()
        .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
        .border(
            width = strokes.s,
            color = borerColor,
            shape = shape
        )

    Surface(
        modifier = surfaceModifier,
        shape = shape,
        color = resolvedBackground,
        shadowElevation = elevation,
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier.padding(padding).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingIcon?.let {
                Icon(
                    painter = it,
                    contentDescription = null,
                    tint = leadingIconTint ?: resolvedTitleColor,
                    modifier = leadingIconSize?.let { s -> Modifier.size(s) } ?: Modifier,
                )
                Spacer(modifier = Modifier.width(gap ?: spacing.s))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = titleStyle ?: StocklifyTheme.textStyles.subtitleM,
                    color = titleColor ?: resolvedTitleColor,
                )
                Spacer(modifier = Modifier.height(spacing.xs))
                Text(
                    text = content,
                    style = contentStyle ?: StocklifyTheme.textStyles.bodyS,
                    color = resolvedContentColor,
                )
            }
            trailingIcon?.let {
                Spacer(modifier = Modifier.width(gap ?: spacing.s))
                Image(
                    painter = it,
                    contentDescription = null,
                    modifier = trailingIconSize?.let { s -> Modifier.size(s) } ?: Modifier,
                    colorFilter = if (trailingIconTint != null) {
                        ColorFilter.tint(trailingIconTint) } else { null },
                )
            }
        }
    }
}
