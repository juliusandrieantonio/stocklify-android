package com.scalo.stocklify.feature.products.ui.screens

import android.content.Context
import android.net.Uri
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.FileProvider
import com.scalo.stocklify.R
import com.scalo.stocklify.core.ui.components.buttons.StocklifyOutlineButton
import com.scalo.stocklify.core.ui.components.buttons.StocklifyPrimaryButton
import com.scalo.stocklify.core.ui.components.topbar.StocklifyTopBar
import com.scalo.stocklify.core.ui.theme.StocklifySize
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme
import java.io.File

@Composable
fun ImportSuccessfulScreen(
    onBack: () -> Unit,
    onScanAnother: () -> Unit,
    onGoToInventory: () -> Unit,
    count: Int = 0
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

            FormsSection()

            Spacer(modifier = Modifier.height(StocklifySize.medium))

            HeaderSection(count = count)

            Spacer(modifier = Modifier.height(StocklifySize.medium))

            StocklifyPrimaryButton(
                text = "Scan Another List",
                onClick = onScanAnother,
                fullWidth = true
            )

            Spacer(modifier = Modifier.height(StocklifySpacing.columnGap))

            StocklifyOutlineButton(
                text = "Go to Inventory",
                onClick = onGoToInventory,
                fullWidth = true
            )

            Spacer(modifier = Modifier.height(StocklifySpacing.columnGap))

        }
    }
}

@Composable
private fun HeaderSection(count: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = StocklifySpacing.columnGap)
    ) {
        Text(
            text = "Import Successful!",
            style = StocklifyTextStyles.heading4,
            color = StocklifyTheme.colors.primary700
        )

        Text(
            text = "$count products have been added to your inventory.",
            style = StocklifyTextStyles.subtitleS,
            color = StocklifyTheme.colors.typography400,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FormsSection() {
    Image(
        painter = painterResource(R.drawable.store_6),
        contentDescription = null,
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun ImportSuccessfulScreenScreenPreview() {
    ImportSuccessfulScreen(
        onBack = {},
        onScanAnother = {},
        onGoToInventory = {},
        count = 4
    )
}
