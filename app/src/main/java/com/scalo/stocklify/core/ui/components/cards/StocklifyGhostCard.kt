package com.scalo.stocklify.core.ui.components.cards

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.tooling.preview.Preview
import com.scalo.stocklify.core.ui.theme.StocklifyTheme

@Composable
fun StocklifyGhostCard(
    title: String,
    content: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    padding: PaddingValues = PaddingValues(StocklifyTheme.spacing.l),
    backgroundColor: Color? = null,
    contentColor: Color? = null,
    titleColor: Color? = null,
    titleStyle: TextStyle? = null,
    contentStyle: TextStyle? = null,
    gap: Dp? = null,
    leadingIconSize: Dp? = null,
    trailingIconSize: Dp? = null,
    leadingIcon: Painter? = null,
    leadingIconTint: Color? = null,
    trailingIcon: Painter? = null,
    trailingIconTint: Color? = null,
) = StocklifyCard(
    title = title,
    content = content,
    modifier = modifier,
    onClick = onClick,
    variant = StocklifyCardVariant.Ghost,
    padding = padding,
    backgroundColor = backgroundColor,
    contentColor = contentColor,
    titleColor = titleColor,
    titleStyle = titleStyle,
    contentStyle = contentStyle,
    gap = gap,
    leadingIconSize = leadingIconSize,
    trailingIconSize = trailingIconSize,
    leadingIcon = leadingIcon,
    leadingIconTint = leadingIconTint,
    trailingIcon = trailingIcon,
    trailingIconTint = trailingIconTint,
)

@Preview(name = "Ghost Card", showBackground = true)
@Composable
private fun StocklifyGhostCardPreview() {
    StocklifyTheme {
        StocklifyGhostCard(
            title = "Recent Activity",
            content = "No activity yet"
        )
    }
}
