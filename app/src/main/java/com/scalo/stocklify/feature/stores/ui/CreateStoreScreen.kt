package com.scalo.stocklify.feature.stores.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.scalo.stocklify.R
import com.scalo.stocklify.core.ui.components.buttons.StocklifyPrimaryButton
import com.scalo.stocklify.core.ui.components.texts.DrawableText
import com.scalo.stocklify.core.ui.components.texts.StocklifyTextField
import com.scalo.stocklify.core.ui.components.topbar.StocklifyTopBar
import com.scalo.stocklify.core.ui.theme.*
import com.scalo.stocklify.core.util.IconPosition
import com.scalo.stocklify.core.util.Screen

@Composable
fun CreateStoreScreen(
    onBack: () -> Unit,
    onCreated: () -> Unit,
    viewModel: StoreViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val shape = RoundedCornerShape(StocklifyRadius.s)

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                StoreEvent.StoreCreated -> onCreated()
                is StoreEvent.ShowError -> {
                    // TODO: SHOW ERROR MESSAGE
                }
            }
        }
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

            Spacer(modifier = Modifier.height(StocklifySize.small))

            StoreFormSection()

            Spacer(modifier = Modifier.height(StocklifySize.small))

            PremiumInfoCard(shape)

            Spacer(modifier = Modifier.height(StocklifySize.medium))

            StocklifyPrimaryButton(
                fullWidth = true,
                text = "Create Store",
                onClick = {
                    viewModel.createStore()
                }
            )

            Spacer(Modifier.height(StocklifySpacing.s))

            FooterNote()
        }
    }
}

@Composable
private fun HeaderSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(space = StocklifySpacing.columnGap)) {

        Text(
            text = "Create Your Store",
            style = StocklifyTextStyles.heading4,
            color = StocklifyTheme.colors.primary700
        )

        Text(
            text = "Let’s get to know your business.",
            style = StocklifyTextStyles.subtitleS,
            color = StocklifyTheme.colors.typography400
        )
    }
}

@Composable
private fun StoreFormSection() {

    Column(
        verticalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap)
    ) {

        StocklifyTextField(
            value = "",
            onValueChange = {},
            label = "Store Name",
            placeholder = "Enter your store name"
        )

        Spacer(modifier = Modifier.height(StocklifySize.small))

        StocklifyTextField(
            value = "",
            onValueChange = {},
            label = "Store Type",
            placeholder = "Enter your store type",
            optional = true
        )
    }
}

@Composable
private fun PremiumInfoCard(shape: RoundedCornerShape) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(StocklifyTheme.colors.primary100)
            .border(
                width = StocklifyStroke.s,
                color = StocklifyTheme.colors.primary200,
                shape = shape
            )
            .padding(StocklifySpacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        DrawableText(
            text = buildAnnotatedString {
                append("Team members and ")

                withStyle(SpanStyle(color = StocklifyTheme.colors.primary600)) {
                    append("store sharing ")
                }

                append("are available on ")

                withStyle(SpanStyle(color = StocklifyTheme.colors.primary600)) {
                    append("Premium.")
                }
            },
            painter = painterResource(R.drawable.filled_crown),
            iconPosition = IconPosition.Start,
            iconTint = Color(0xFFECB913),
            iconSize = StocklifySize.small,
            textStyle = StocklifyTextStyles.subtitleXs,
            spacing = StocklifySpacing.l
        )
    }
}

@Composable
private fun FooterNote() {
    Text(
        text = "You can change this later in Store Settings.",
        style = StocklifyTextStyles.subtitleXs,
        color = StocklifyTheme.colors.typography100
    )
}


@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun CreateStoreScreenPreview() {
    CreateStoreScreen(
        onBack = {  },
        onCreated = {

        }
    )
}
