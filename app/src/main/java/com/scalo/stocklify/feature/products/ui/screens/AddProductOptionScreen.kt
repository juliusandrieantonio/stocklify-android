package com.scalo.stocklify.feature.products.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.scalo.stocklify.R
import com.scalo.stocklify.core.ui.components.buttons.StocklifyPrimaryButton
import com.scalo.stocklify.core.ui.components.cards.StocklifyOutlineCard
import com.scalo.stocklify.core.ui.components.cards.StocklifyPrimaryCard
import com.scalo.stocklify.core.ui.components.topbar.StocklifyTopBar
import com.scalo.stocklify.core.ui.theme.StocklifySize
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme

@Composable
fun AddProductOptionScreen(
    onBack: () -> Unit,
    onLater: () -> Unit,
    onScanProductList: () -> Unit,
    onAddManually: () -> Unit,
) {
    Scaffold(
        topBar = {
            StocklifyTopBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.outlined_left_arrow),
                            contentDescription = "Back",
                            tint = StocklifyTheme.colors.primary700
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(StocklifySpacing.screenPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HeaderSection()

            Spacer(modifier = Modifier.height(StocklifySize.large))

            OptionSection(
                onScanProductList = onScanProductList,
                onAddManually = onAddManually,
            )

            Spacer(modifier = Modifier.height(StocklifySize.large))

            StocklifyPrimaryButton(
                text = "I'll do this later",
                onClick = onLater,
                fullWidth = true
            )
        }

    }
}

@Composable
private fun HeaderSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(space = StocklifySpacing.columnGap)) {
        Text(
            text = "How would you like to add products?",
            style = StocklifyTextStyles.heading4,
            color = StocklifyTheme.colors.primary700,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Choose the method that works best for you.",
            style = StocklifyTextStyles.subtitleS,
            color = StocklifyTheme.colors.typography400,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun OptionSection(
    onScanProductList: () -> Unit,
    onAddManually: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(space = StocklifySpacing.columnGapM)
    ) {
        StocklifyPrimaryCard(
            title = "Scan Product List",
            content = "Import multiple products from handwritten or printed list.",
            contentStyle = StocklifyTextStyles.bodyXs,
            leadingIcon = painterResource(R.drawable.outlined_scan),
            trailingIcon = painterResource(R.drawable.outlined_right_chevron),
            gap = StocklifySpacing.m,
            leadingIconSize = StocklifySize.small,
            onClick = onScanProductList,
        )

        StocklifyOutlineCard(
            title = "Add Manually",
            content = "Enter product details one by one.",
            contentStyle = StocklifyTextStyles.bodyXs,
            leadingIcon = painterResource(R.drawable.outlined_pen),
            trailingIcon = painterResource(R.drawable.outlined_right_chevron),
            gap = StocklifySpacing.m,
            leadingIconSize = StocklifySize.small,
            onClick = onAddManually,
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun AddProductOptionScreenPreview() {
    AddProductOptionScreen(
        onBack = {},
        onLater = {},
        onScanProductList = {},
        onAddManually = {},
    )
}
