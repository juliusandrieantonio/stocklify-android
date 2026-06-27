package com.scalo.stocklify.feature.products.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.scalo.stocklify.R
import com.scalo.stocklify.core.ui.components.buttons.StocklifyPrimaryButton
import com.scalo.stocklify.core.ui.components.texts.DrawableText
import com.scalo.stocklify.core.ui.theme.StocklifySize
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme

@Composable
fun StoreReadyScreen(
    onGoToSalesMode: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(StocklifySpacing.screenPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HeroImage()

        HeaderSection()

        Spacer(modifier = Modifier.height(StocklifySize.medium))

        ContentSection()

        Spacer(modifier = Modifier.height(StocklifySize.medium))

        StocklifyPrimaryButton(
            text = "Go to Sales Mode",
            onClick = onGoToSalesMode,
            fullWidth = true
        )

    }
}

@Composable
private fun HeaderSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(space = StocklifySpacing.columnGap)) {
        Text(
            text = "Your store is ready!",
            style = StocklifyTextStyles.heading4,
            color = StocklifyTheme.colors.primary700
        )

        Text(
            text = "Start selling with Sales Mode.",
            style = StocklifyTextStyles.subtitleS,
            color = StocklifyTheme.colors.typography400
        )
    }
}


@Composable
private fun HeroImage() {
    Image(
        painter = painterResource(R.drawable.store_3),
        contentDescription = null,
        modifier = Modifier.fillMaxWidth()
    )
}


@Composable
private fun ContentSection() {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(space = StocklifySpacing.columnGap)
    ) {
        DrawableText(
            text = buildAnnotatedString {
                append("Manage inventory")
            },
            painter = painterResource(R.drawable.outlined_clipboard),
            iconSize = StocklifySize.smallest,
            iconTint = StocklifyTheme.colors.primary700,
            iconBackgroundColor = StocklifyTheme.colors.primary100,
            spacing = StocklifySpacing.columnGap,
            textStyle = StocklifyTextStyles.subtitleXs,
            textColor = StocklifyTheme.colors.typography500
        )

        DrawableText(
            text = buildAnnotatedString {
                append("Record sales")
            },
            painter = painterResource(R.drawable.outlined_list),
            iconSize = StocklifySize.smallest,
            iconTint = StocklifyTheme.colors.primary700,
            iconBackgroundColor = StocklifyTheme.colors.primary100,
            spacing = StocklifySpacing.columnGap,
            textStyle = StocklifyTextStyles.subtitleXs,
            textColor = StocklifyTheme.colors.typography500
        )

        DrawableText(
            text = buildAnnotatedString {
                append("Grow your business")
            },
            painter = painterResource(R.drawable.outlined_graph),
            iconSize = StocklifySize.smallest,
            iconTint = StocklifyTheme.colors.primary700,
            iconBackgroundColor = StocklifyTheme.colors.primary100,
            spacing = StocklifySpacing.columnGap,
            textStyle = StocklifyTextStyles.subtitleXs,
            textColor = StocklifyTheme.colors.typography500
        )
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun StoreReadyScreenPreview() {
    StoreReadyScreen(
        onGoToSalesMode = {}
    )
}
