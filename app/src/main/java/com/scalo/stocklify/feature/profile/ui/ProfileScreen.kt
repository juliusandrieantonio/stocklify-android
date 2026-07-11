package com.scalo.stocklify.feature.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.scalo.stocklify.R
import com.scalo.stocklify.core.model.enums.MembershipRole
import com.scalo.stocklify.core.ui.components.buttons.StocklifyOutlineButton
import com.scalo.stocklify.core.ui.components.cards.StocklifyElevatedCard
import com.scalo.stocklify.core.ui.components.cards.StocklifyOutlineCard
import com.scalo.stocklify.core.ui.components.chips.StocklifyStatusChip
import com.scalo.stocklify.core.ui.components.chips.StocklifyStatusChipType
import com.scalo.stocklify.core.ui.theme.StocklifySize
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme

@Composable
fun ProfileScreen(
    onSignedOut: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                ProfileEvent.SignedOut -> onSignedOut()
                is ProfileEvent.ShowError -> {
                    // TODO: SHOW ERROR MESSAGE
                }
            }
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Profile",
                style = StocklifyTextStyles.heading4,
                color = StocklifyTheme.colors.primary700
            )

            Spacer(Modifier.height(StocklifySpacing.l))

            StocklifyOutlineButton(
                text = "Sign Out",
                isLoading = state.isLoading,
                onClick = { viewModel.onAction(ProfileAction.SignOut) }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(onSignedOut = {})
}
