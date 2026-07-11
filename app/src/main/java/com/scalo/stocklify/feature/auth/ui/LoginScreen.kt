package com.scalo.stocklify.feature.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.scalo.stocklify.R
import com.scalo.stocklify.core.ui.components.buttons.StocklifyPrimaryButton
import com.scalo.stocklify.core.ui.components.buttons.StocklifyTertiaryButton
import com.scalo.stocklify.core.ui.components.texts.StocklifyTextField
import com.scalo.stocklify.core.ui.theme.StocklifySize
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme

@Composable
fun LoginScreen(
    onLoggedIn: () -> Unit,
    onRegister: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is LoginEvent.LoggedIn -> onLoggedIn()
                is LoginEvent.ShowError -> {
                    // TODO: SHOW ERROR MESSAGE
                }
            }
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(StocklifySpacing.screenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            HeaderSection()

            Spacer(modifier = Modifier.height(StocklifySize.smallest))

            var passwordVisible by remember { mutableStateOf(false) }

            Column(verticalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap)) {
                StocklifyTextField(
                    value = state.email,
                    onValueChange = { viewModel.onAction(LoginAction.EmailChanged(it)) },
                    label = "Email",
                    placeholder = "Enter your email",
                    isError = state.emailError != null,
                    supportingText = state.emailError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                StocklifyTextField(
                    value = state.password,
                    onValueChange = { viewModel.onAction(LoginAction.PasswordChanged(it)) },
                    label = "Password",
                    placeholder = "Enter your password",
                    isError = state.passwordError != null,
                    supportingText = state.passwordError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (passwordVisible) VisualTransformation.None
                                           else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(
                                    if (passwordVisible) R.drawable.outlined_visible
                                    else R.drawable.outlined_not_visible
                                ),
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = StocklifyTheme.colors.grayscale400
                            )
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(StocklifySize.large))

            StocklifyPrimaryButton(
                fullWidth = true,
                text = "Log In",
                isLoading = state.isLoading,
                onClick = { viewModel.onAction(LoginAction.Submit) }
            )

            Spacer(Modifier.height(StocklifySpacing.l))

            Row(horizontalArrangement = Arrangement.Center) {
                Text(
                    text = "Don't have an account?",
                    style = StocklifyTextStyles.subtitleXs,
                    color = StocklifyTheme.colors.typography400
                )

                Spacer(Modifier.height(StocklifySpacing.xs))

                StocklifyTertiaryButton(
                    text = "Sign Up",
                    onClick = onRegister
                )
            }
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
            text = "Welcome Back",
            style = StocklifyTextStyles.heading4,
            color = StocklifyTheme.colors.primary700
        )

        Text(
            text = "Log in to continue managing your store.",
            style = StocklifyTextStyles.subtitleS,
            color = StocklifyTheme.colors.typography400
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onLoggedIn = {},
        onRegister = {}
    )
}
