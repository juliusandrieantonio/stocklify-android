package com.scalo.stocklify.feature.products.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.scalo.stocklify.R
import com.scalo.stocklify.core.ui.components.buttons.StocklifyOutlineButton
import com.scalo.stocklify.core.ui.components.buttons.StocklifyPrimaryButton
import com.scalo.stocklify.core.ui.components.texts.DrawableText
import com.scalo.stocklify.core.ui.components.topbar.StocklifyTopBar
import com.scalo.stocklify.core.ui.theme.StocklifySize
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme

@Composable
fun ScanProductScreen(
    onBack: () -> Unit,
    onAddProduct: () -> Unit,
    onLater: () -> Unit
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderSection()

            Spacer(modifier = Modifier.height(StocklifySize.medium))

            HeroImage()

            Spacer(modifier = Modifier.height(StocklifySize.medium))

            StocklifyPrimaryButton(
                text = "Take Photo",
                onClick = onAddProduct,
                fullWidth = true
            )

            Spacer(modifier = Modifier.height(StocklifySpacing.columnGap))

            StocklifyOutlineButton(
                text = "Upload Image",
                onClick = onLater,
                fullWidth = true
            )

            Spacer(modifier = Modifier.height(StocklifySpacing.columnGap))

            FooterNote()

        }

    }
}

@Composable
private fun HeaderSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(space = StocklifySpacing.columnGap)) {
        Text(
            text = "Scan Product List",
            style = StocklifyTextStyles.heading4,
            color = StocklifyTheme.colors.primary700
        )

        Text(
            text = "Take a photo or upload your list. We’ll extract the products for you.",
            style = StocklifyTextStyles.subtitleS,
            color = StocklifyTheme.colors.typography400,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
private fun HeroImage() {
    Image(
        painter = painterResource(R.drawable.scan_1),
        contentDescription = null,
        modifier = Modifier.fillMaxWidth()
    )
}



@Composable
private fun FooterNote() {
    Text(
        text = "Supports JPG and PNG",
        style = StocklifyTextStyles.subtitleXs,
        color = StocklifyTheme.colors.typography100
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun ScanProductScreenPreview() {
    ScanProductScreen(
        onBack = {},
        onAddProduct = {},
        onLater = {}
    )
}
