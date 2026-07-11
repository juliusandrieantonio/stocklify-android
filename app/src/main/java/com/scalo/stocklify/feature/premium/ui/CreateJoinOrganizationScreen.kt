package com.scalo.stocklify.feature.premium.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.scalo.stocklify.R
import com.scalo.stocklify.core.ui.components.buttons.StocklifyPrimaryButton
import com.scalo.stocklify.core.ui.components.texts.DrawableText
import com.scalo.stocklify.core.ui.theme.StocklifyRadius
import com.scalo.stocklify.core.ui.theme.StocklifySize
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyStroke
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme
import com.scalo.stocklify.core.util.IconPosition


@Composable
fun CreateJoinOrganizationScreen(
    onCreateOrganization: () -> Unit,
    onJoinOrganization: () -> Unit
) {

    val shape = RoundedCornerShape(StocklifyRadius.s)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(StocklifySpacing.screenPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            StocklifySpacing.columnGap,
            Alignment.CenterVertically
        )
    ) {

        HeaderSection()

        HeroImage()

        StocklifyPrimaryButton(
            fullWidth = true,
            text = "Create Organization",
            onClick = onCreateOrganization
        )

        JoinOrganizationSection(shape, onJoinOrganization)
    }
}

@Composable
private fun HeaderSection() {
    Image(
        painter = painterResource(R.drawable.splash_logo),
        contentDescription = null,
        modifier = Modifier.size(StocklifySize.xxlarge, StocklifySize.smallest)
    )

    Text(
        text = "Welcome to Stocklify",
        style = StocklifyTextStyles.heading4,
        color = StocklifyTheme.colors.primary700
    )

    Text(
        text = "Let's setup your business.",
        style = StocklifyTextStyles.subtitleS,
        color = StocklifyTheme.colors.typography400
    )
}

@Composable
private fun HeroImage() {
    Image(
        painter = painterResource(R.drawable.store_1),
        contentDescription = null,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun JoinOrganizationSection(shape: RoundedCornerShape, onJoinOrganization: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = StocklifyStroke.s,
                color = StocklifyTheme.colors.transparentPrimary600,
                shape = shape
            )
            .padding(vertical = StocklifySpacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            StocklifySpacing.columnGap,
            Alignment.CenterVertically
        )
    ) {

        Text(
            text = "Already have an organization invitation code?",
            style = StocklifyTextStyles.subtitleXs,
            color = StocklifyTheme.colors.typography500
        )

        Text(
            text = "Join Existing Organization",
            style = StocklifyTextStyles.subtitleXs,
            color = StocklifyTheme.colors.primary600,
            modifier = Modifier.clickable {
                onJoinOrganization()
            }
        )
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun CreateJoinOrganizationScreenPreview() {
    CreateJoinOrganizationScreen(
        onCreateOrganization = {
        },
        onJoinOrganization = {
        }
    )
}
