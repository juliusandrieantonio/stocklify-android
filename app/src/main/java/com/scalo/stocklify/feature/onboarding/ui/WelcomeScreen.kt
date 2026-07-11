package com.scalo.stocklify.feature.onboarding.ui

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.scalo.stocklify.R
import com.scalo.stocklify.core.ui.components.buttons.StocklifyPrimaryButton
import com.scalo.stocklify.core.ui.components.texts.DrawableText
import com.scalo.stocklify.core.ui.theme.StocklifySize
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme
import com.scalo.stocklify.core.util.IconPosition

@Composable
fun WelcomeScreen(
    onContinueAsGuest: () -> Unit,
    onLogin: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(StocklifySpacing.screenPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HeaderSection()

            Spacer(modifier = Modifier.height(StocklifySize.large))

            HeroImage()

            Spacer(modifier = Modifier.height(StocklifySize.medium))

            StocklifyPrimaryButton(
                text = "Continue as Guest",
                onClick = {
                    viewModel.completeWelcomeScreen()
                    onContinueAsGuest()
                },
                fullWidth = true
            )

            Spacer(modifier = Modifier.height(StocklifySize.small))

            LoginComponent {
                viewModel.completeWelcomeScreen()
                onLogin()
            }

            Spacer(modifier = Modifier.weight(1f))

            InfoNote()

        }
    }
}

@Composable
private fun InfoNote() {
    DrawableText(
        text = buildAnnotatedString {
            append("You can create an account later to backup and sync your data.")
        },
        painter = painterResource(R.drawable.outlined_info),
        iconPosition = IconPosition.Start,
        iconTint = StocklifyTheme.colors.typography100,
        textStyle = StocklifyTextStyles.subtitleXs,
        textColor = StocklifyTheme.colors.typography100,
        spacing = StocklifySpacing.s
    )
}

@Composable
private fun HeaderSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = StocklifySpacing.columnGap)
    ) {
        Image(
            painter = painterResource(R.drawable.splash_logo),
            contentDescription = "logo",
            modifier = Modifier.width(StocklifySize.xLarge)
        )
        Text(
            text = "Welcome to Stocklify",
            style = StocklifyTextStyles.heading4,
            color = StocklifyTheme.colors.primary700
        )

        Text(
            text = "Sell Faster. Manage Inventory Smarter.",
            style = StocklifyTextStyles.subtitleS,
            color = StocklifyTheme.colors.typography400,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun HeroImage() {
    Image(
        painter = painterResource(R.drawable.store_8),
        contentDescription = null,
        modifier = Modifier.fillMaxWidth().height(250.dp),
        contentScale = ContentScale.FillBounds
    )
}

@Composable
private fun LoginComponent(onLogin: () -> Unit) {
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
    WelcomeScreen(
        {},
        {}
    )
}
