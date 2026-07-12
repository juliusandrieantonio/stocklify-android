package com.scalo.stocklify.feature.auth.ui

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.scalo.stocklify.R
import com.scalo.stocklify.core.ui.components.buttons.StocklifyOutlineButton
import com.scalo.stocklify.core.ui.components.buttons.StocklifyPrimaryButton
import com.scalo.stocklify.core.ui.theme.StocklifySize
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme

@Composable
fun VerifyEmailScreen(
    onVerified: () -> Unit,
    onSignOut: () -> Unit,
    viewModel: VerifyEmailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                VerifyEmailEvent.Verified -> onVerified()
                VerifyEmailEvent.SignedOut -> onSignOut()
            }
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(StocklifySpacing.screenPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HeaderSection(state.email)

            Spacer(modifier = Modifier.height(StocklifySize.large))

            if (state.message != null) {
                Text(
                    text = state.message.orEmpty(),
                    style = StocklifyTextStyles.subtitleS,
                    color = StocklifyTheme.colors.typography300,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(StocklifySize.small))
            }

            StocklifyPrimaryButton(
                fullWidth = true,
                text = "Go back to Login Screen",
                isLoading = state.isChecking,
                onClick = { viewModel.signOut() }
            )
        }
    }
}

@Composable
private fun HeaderSection(email: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = StocklifySpacing.columnGap)
    ) {
        Text(
            text = "Verify Your Email",
            style = StocklifyTextStyles.heading4,
            color = StocklifyTheme.colors.primary700
        )

        Text(
            text = if (email.isNotBlank())
                "We've sent a verification link to $email. Please verify your email to continue."
            else
                "Please verify your email to continue.",
            style = StocklifyTextStyles.subtitleS,
            color = StocklifyTheme.colors.typography400,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun HeroImage() {
    Image(
        painter = painterResource(R.drawable.splash_logo),
        contentDescription = "logo",
        modifier = Modifier.width(StocklifySize.xLarge)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VerifyEmailScreenPreview() {
    VerifyEmailScreen(
        onVerified = {},
        onSignOut = {}
    )
}
