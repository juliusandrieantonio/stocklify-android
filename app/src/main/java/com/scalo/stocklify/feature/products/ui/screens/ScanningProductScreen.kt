package com.scalo.stocklify.feature.products.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.scalo.stocklify.R
import com.scalo.stocklify.core.ui.components.topbar.StocklifyTopBar
import com.scalo.stocklify.core.ui.theme.StocklifySize
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme
import com.scalo.stocklify.feature.products.ui.ScanProductViewModel
import com.scalo.stocklify.feature.products.ui.ScanningState

@Composable
fun ScanningProductScreen(
    onBack: () -> Unit,
    onFinish: () -> Unit,
    viewModel: ScanProductViewModel = hiltViewModel(),
) {
    val scanningState by viewModel.scanningState.collectAsState()

    LaunchedEffect(scanningState) {
        if (scanningState is ScanningState.Success || scanningState is ScanningState.Error) {
            onFinish()
        }
    }

    val progress = when (val state = scanningState) {
        is ScanningState.Loading -> state.progress
        is ScanningState.Success -> 1f
        else -> 0f
    }

    val message = when (val state = scanningState) {
        is ScanningState.Loading -> state.message
        is ScanningState.Success -> "Done!"
        is ScanningState.Error -> "Something went wrong"
        ScanningState.Idle -> ""
    }

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

            FormsSection()

            Spacer(modifier = Modifier.height(StocklifySize.medium))

            Content()

            Spacer(modifier = Modifier.height(StocklifySize.medium))

            ScanProgressBar(progress = progress, message = message)
        }
    }
}

@Composable
private fun HeaderSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = StocklifySpacing.columnGap)
    ) {
        Text(
            text = "Reading your list...",
            style = StocklifyTextStyles.heading4,
            color = StocklifyTheme.colors.primary700
        )

        Text(
            text = "This may take a few seconds.",
            style = StocklifyTextStyles.subtitleS,
            color = StocklifyTheme.colors.typography400,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FormsSection() {
    Image(
        painter = painterResource(R.drawable.scanner_2),
        contentDescription = null,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun Content() {
    Text(
        text = "Extracting products, prices, and quantities...",
        style = StocklifyTextStyles.subtitleS,
        color = StocklifyTheme.colors.typography400
    )
}

@Composable
private fun ScanProgressBar(progress: Float, message: String) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 400),
        label = "scan_progress"
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxWidth(),
            color = StocklifyTheme.colors.primary500,
            trackColor = StocklifyTheme.colors.primary100,
            strokeCap = StrokeCap.Round,
        )

        Text(
            text = message,
            style = StocklifyTextStyles.subtitleXs,
            color = StocklifyTheme.colors.typography100
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun ScanningProductScreenPreview() {
    ScanningProductScreen(
        onBack = {},
        onFinish = {}
    )
}
