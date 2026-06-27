package com.scalo.stocklify.feature.stores.ui

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
import com.scalo.stocklify.core.ui.components.buttons.StocklifyOutlineButton
import com.scalo.stocklify.core.ui.components.buttons.StocklifyPrimaryButton
import com.scalo.stocklify.core.ui.components.texts.DrawableText
import com.scalo.stocklify.core.ui.theme.StocklifySize
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme

@Composable
fun StoreCreatedScreen(
    onAddProduct: () -> Unit,
    onLater: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(StocklifySpacing.screenPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HeaderSection()

        HeroImage()

        ContentSection()

        Spacer(modifier = Modifier.height(StocklifySize.medium))

        StocklifyPrimaryButton(
            text = "Add Product",
            onClick = onAddProduct,
            fullWidth = true
        )

        Spacer(modifier = Modifier.height(StocklifySpacing.columnGap))

        StocklifyOutlineButton(
            text = "I'll do this later.",
            onClick = onLater,
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
            text = "Let’s add your first product.",
            style = StocklifyTextStyles.subtitleS,
            color = StocklifyTheme.colors.typography400
        )
    }
}


@Composable
private fun HeroImage() {
    Image(
        painter = painterResource(R.drawable.store_2),
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
                append("Add products to your inventory")
            },
            painter = painterResource(R.drawable.outlined_package),
            iconSize = StocklifySize.smallest,
            iconTint = StocklifyTheme.colors.primary700,
            iconBackgroundColor = StocklifyTheme.colors.primary100,
            spacing = StocklifySpacing.columnGap,
            textStyle = StocklifyTextStyles.subtitleXs,
            textColor = StocklifyTheme.colors.typography500
        )

        DrawableText(
            text = buildAnnotatedString {
                append("Set prices and stock")
            },
            painter = painterResource(R.drawable.outlined_price_tag),
            iconSize = StocklifySize.smallest,
            iconTint = StocklifyTheme.colors.primary700,
            iconBackgroundColor = StocklifyTheme.colors.primary100,
            spacing = StocklifySpacing.columnGap,
            textStyle = StocklifyTextStyles.subtitleXs,
            textColor = StocklifyTheme.colors.typography500
        )

        DrawableText(
            text = buildAnnotatedString {
                append("Start selling in seconds")
            },
            painter = painterResource(R.drawable.outlined_cart_1),
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
fun StoreCreatedScreenPreview() {
    StoreCreatedScreen(
        onAddProduct = {},
        onLater = {}
    )
}
