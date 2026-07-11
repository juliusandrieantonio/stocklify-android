package com.scalo.stocklify.feature.premium.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.scalo.stocklify.R
import com.scalo.stocklify.core.ui.components.buttons.StocklifyPrimaryButton
import com.scalo.stocklify.core.ui.components.texts.DrawableText
import com.scalo.stocklify.core.ui.components.texts.StocklifyTextField
import com.scalo.stocklify.core.ui.components.topbar.StocklifyTopBar
import com.scalo.stocklify.core.ui.theme.*
import com.scalo.stocklify.core.util.IconPosition
import kotlinx.coroutines.launch

@Composable
fun JoinOrganizationScreen(
    onBack: () -> Unit,
    onJoined: () -> Unit,
    viewModel: PremiumViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                OrganizationEvent.OrganizationJoined -> onJoined()
                is OrganizationEvent.ShowError -> scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = StocklifyTheme.colors.error,
                    contentColor = StocklifyStaticColors.white,
                )
            }
        },
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
            verticalArrangement = Arrangement.spacedBy(
                StocklifySpacing.columnGap
            )
        ) {

            HeaderSection()

            HeroImage()

            Spacer(modifier = Modifier.height(StocklifySize.extraExtraSmall))

            InvitationField(
                value = state.inviteCode,
                error = state.inviteCodeError,
                onValueChange = {
                    viewModel.onAction(OrganizationAction.InviteCodeChanged(it))
                }
            )

            Spacer(modifier = Modifier.height(StocklifySize.extraExtraSmall))

            JoinButton(
                isLoading = state.isLoading,
                onClick = { viewModel.onAction(OrganizationAction.JoinOrganization) }
            )

            InfoNote()
        }
    }
}

@Composable
private fun HeaderSection() {
    Text(
        text = "Join Existing Organization",
        style = StocklifyTextStyles.heading4,
        color = StocklifyTheme.colors.primary700
    )

    Text(
        text = "Enter the invitation code from your organization owner.",
        style = StocklifyTextStyles.subtitleXs,
        color = StocklifyTheme.colors.typography400
    )
}

@Composable
private fun HeroImage() {
    Image(
        painter = painterResource(R.drawable.store_4),
        contentDescription = "Join Organization"
    )
}

@Composable
private fun InvitationField(
    value: String,
    error: String?,
    onValueChange: (String) -> Unit
) {
    StocklifyTextField(
        value = value,
        onValueChange = onValueChange,
        label = "Invitation Code",
        placeholder = "Enter code",
        isError = error != null,
        supportingText = error
    )
}

@Composable
private fun JoinButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    StocklifyPrimaryButton(
        fullWidth = true,
        text = "Join Organization",
        enabled = !isLoading,
        isLoading = isLoading,
        onClick = onClick
    )
}

@Composable
private fun InfoNote() {
    DrawableText(
        text = buildAnnotatedString {
            append("You can only join an organization if the owner has a Premium account.")
        },
        painter = painterResource(R.drawable.outlined_info),
        iconPosition = IconPosition.Start,
        iconTint = StocklifyTheme.colors.typography100,
        textStyle = StocklifyTextStyles.subtitleXs,
        textColor = StocklifyTheme.colors.typography100,
        spacing = StocklifySpacing.s
    )
}


@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun JoinOrganizationScreenPreview() {
    JoinOrganizationScreen(
        onBack = {  },
        onJoined = {

        }
    )
}
