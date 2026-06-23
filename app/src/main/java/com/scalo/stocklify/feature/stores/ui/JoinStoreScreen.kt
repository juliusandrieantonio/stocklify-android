package com.scalo.stocklify.feature.stores.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.scalo.stocklify.R
import com.scalo.stocklify.core.ui.components.buttons.StocklifyPrimaryButton
import com.scalo.stocklify.core.ui.components.texts.DrawableText
import com.scalo.stocklify.core.ui.components.texts.StocklifyTextField
import com.scalo.stocklify.core.ui.components.topbar.StocklifyTopBar
import com.scalo.stocklify.core.ui.theme.*
import com.scalo.stocklify.core.util.IconPosition

@Composable
fun JoinStoreScreen(
    onBack: () -> Unit,
    onJoined: () -> Unit
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
            verticalArrangement = Arrangement.spacedBy(
                StocklifySpacing.columnGap
            )
        ) {

            HeaderSection()

            HeroImage()

            DescriptionText()

            Spacer(modifier = Modifier.height(StocklifySize.smallest))

            InvitationField()

            Spacer(modifier = Modifier.height(StocklifySize.smallest))

            JoinButton(onJoined)

            InfoNote()
        }
    }
}

@Composable
private fun HeaderSection() {
    Text(
        text = "Join Existing Store",
        style = StocklifyTextStyles.heading4,
        color = StocklifyTheme.colors.primary700
    )
}

@Composable
private fun HeroImage() {
    Image(
        painter = painterResource(R.drawable.store_4),
        contentDescription = "Join Store"
    )
}

@Composable
private fun DescriptionText() {
    Text(
        text = "Enter the invitation code from your store owner.",
        style = StocklifyTextStyles.subtitleXs,
        color = StocklifyTheme.colors.typography400
    )
}

@Composable
private fun InvitationField() {
    StocklifyTextField(
        value = "",
        onValueChange = {},
        label = "Invitation Code",
        placeholder = "Enter code"
    )
}

@Composable
private fun JoinButton(onJoined: () -> Unit) {
    StocklifyPrimaryButton(
        fullWidth = true,
        text = "Join Store",
        onClick = onJoined
    )
}

@Composable
private fun InfoNote() {
    DrawableText(
        text = buildAnnotatedString {
            append("You can only join a store if the owner has a Premium account.")
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
fun JoinStoreScreenPreview() {
    JoinStoreScreen(
        onBack = {  },
        onJoined = {

        }
    )
}
