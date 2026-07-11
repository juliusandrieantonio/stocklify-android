package com.scalo.stocklify.feature.preparing.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.scalo.stocklify.R
import com.scalo.stocklify.core.ui.components.buttons.StocklifyPrimaryButton
import com.scalo.stocklify.core.ui.theme.StocklifySize
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme


@Composable
fun PreparingWorkScreen(
    onFinished: (hasOrganizationCached: Boolean, hasStoreCached: Boolean) -> Unit,
    viewModel: PreparingWorkViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val animatedProgress by animateFloatAsState(
        targetValue = state.progress,
        animationSpec = tween(durationMillis = 400),
        label = "scan_progress"
    )

    val context = LocalContext.current
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { viewModel.start() }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            viewModel.start()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is PreparingWorkEvent.Ready ->
                    onFinished(event.hasOrganizationCached, event.hasStoreCached)
            }
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(StocklifySpacing.screenPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            HeroImage()

            Spacer(modifier = Modifier.height(StocklifySize.medium))

            ProgressSection(animatedProgress, state.statusMessage)

        }
    }
}

@Composable
private fun ProgressSection(animatedProgress: Float, statusMessage: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = StocklifySpacing.columnGap)
    ) {
        Text(
            text = "Setting Up Workspace",
            style = StocklifyTextStyles.heading5,
            color = StocklifyTheme.colors.primary700
        )

        Text(
            text = statusMessage,
            style = StocklifyTextStyles.subtitleS,
            color = StocklifyTheme.colors.typography300,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(StocklifySize.smallest))

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxWidth(),
            color = StocklifyTheme.colors.primary500,
            trackColor = StocklifyTheme.colors.primary100,
            strokeCap = StrokeCap.Round,
        )

        Text(
            text = "${"%.2f".format(animatedProgress * 100)} / 100",
            style = StocklifyTextStyles.subtitleS,
            color = StocklifyTheme.colors.typography300,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(StocklifySize.extraExtraSmall))


    }
}

@Composable
private fun HeroImage() {
    Image(
        painter = painterResource(R.drawable.logo),
        contentDescription = null,
        contentScale = ContentScale.FillBounds
    )
}

@Composable
private fun FooterNote(onLogin: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(StocklifySpacing.xl, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Already have an account?",
            style = StocklifyTextStyles.subtitleXs,
            color = StocklifyTheme.colors.typography100
        )

        Text(
            text = "Login",
            textAlign = TextAlign.Center,
            style = StocklifyTextStyles.subtitleXs,
            color = StocklifyTheme.colors.primary500,
            modifier = Modifier.clickable(enabled = true, onClick = onLogin)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun showPreview() {
    PreparingWorkScreen(
        onFinished = { _, _ -> }
    )
}
