package com.scalo.stocklify.core.ui.components.texts

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.scalo.stocklify.core.util.IconPosition

@Composable
fun DrawableText(
    text: AnnotatedString,
    painter: Painter,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    iconSize: Dp = 20.dp,
    spacing: Dp = 4.dp,
    iconTint: Color = LocalContentColor.current,
    textColor: Color = LocalContentColor.current,
    textStyle: TextStyle = LocalTextStyle.current,
    iconPosition: IconPosition = IconPosition.Start
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (iconPosition == IconPosition.Start) {
            Icon(
                painter = painter,
                contentDescription = contentDescription,
                modifier = Modifier.size(iconSize),
                tint = iconTint
            )

            Spacer(Modifier.width(spacing))
        }

        Text(
            text = text,
            style = textStyle,
            color = textColor
        )

        if (iconPosition == IconPosition.End) {
            Spacer(Modifier.width(spacing))

            Icon(
                painter = painter,
                contentDescription = contentDescription,
                modifier = Modifier.size(iconSize),
                tint = iconTint
            )
        }
    }
}
