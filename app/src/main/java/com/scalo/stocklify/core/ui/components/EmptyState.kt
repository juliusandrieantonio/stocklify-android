package com.scalo.stocklify.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.scalo.stocklify.R
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme

@Composable
fun EmptyState(
    image: Painter,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = StocklifyTextStyles.subtitleM,
    titleColor: Color = StocklifyTheme.colors.typography500,
    descriptionStyle: TextStyle = StocklifyTextStyles.bodyS,
    descriptionColor: Color = StocklifyTheme.colors.typography200,
    action: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = image,
            contentDescription = null
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = title,
            style = titleStyle,
            color = titleColor
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = description,
            style = descriptionStyle,
            color = descriptionColor,
            textAlign = TextAlign.Center
        )

        action?.let {
            Spacer(Modifier.height(24.dp))
            it()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyStatePreview() {
    EmptyState(
        image = painterResource(R.drawable.sales_11),
        title = "No products yet",
        description = "Add products to start selling.",
        titleColor = StocklifyTheme.colors.primary700,
        titleStyle = StocklifyTextStyles.subtitleM,
        descriptionColor = StocklifyTheme.colors.primary700,
        descriptionStyle = StocklifyTextStyles.bodyXs
    )
}