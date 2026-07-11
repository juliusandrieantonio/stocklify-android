package com.scalo.stocklify.feature.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.scalo.stocklify.core.ui.components.texts.StocklifyTextField
import com.scalo.stocklify.core.ui.components.topbar.StocklifyTopBar
import com.scalo.stocklify.core.ui.theme.StocklifySize
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme

@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegistered: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                RegisterEvent.Registered -> onRegistered()
                is RegisterEvent.ShowError -> {
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
                .verticalScroll(scrollState)
                .padding(StocklifySpacing.screenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            HeaderSection()

            Spacer(modifier = Modifier.height(StocklifySize.smallest))

            var passwordVisible by remember { mutableStateOf(false) }
            var confirmPasswordVisible by remember { mutableStateOf(false) }

            Column(verticalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap)) {
                StocklifyTextField(
                    value = state.firstName,
                    onValueChange = { viewModel.onAction(RegisterAction.FirstNameChanged(it)) },
                    label = "First Name",
                    placeholder = "Enter your first name",
                    isError = state.firstNameError != null,
                    supportingText = state.firstNameError
                )

                StocklifyTextField(
                    value = state.middleName,
                    onValueChange = { viewModel.onAction(RegisterAction.MiddleNameChanged(it)) },
                    label = "Middle Name",
                    placeholder = "Enter your middle name (optional)"
                )

                StocklifyTextField(
                    value = state.surname,
                    onValueChange = { viewModel.onAction(RegisterAction.SurnameChanged(it)) },
                    label = "Surname",
                    placeholder = "Enter your surname",
                    isError = state.surnameError != null,
                    supportingText = state.surnameError
                )

                StocklifyTextField(
                    value = state.phone,
                    onValueChange = { viewModel.onAction(RegisterAction.PhoneChanged(it)) },
                    label = "Phone",
                    placeholder = "Enter your phone number",
                    isError = state.phoneError != null,
                    supportingText = state.phoneError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                StocklifyTextField(
                    value = state.email,
                    onValueChange = { viewModel.onAction(RegisterAction.EmailChanged(it)) },
                    label = "Email",
                    placeholder = "Enter your email",
                    isError = state.emailError != null,
                    supportingText = state.emailError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                StocklifyTextField(
                    value = state.password,
                    onValueChange = { viewModel.onAction(RegisterAction.PasswordChanged(it)) },
                    label = "Password",
                    placeholder = "Create a password",
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

                StocklifyTextField(
                    value = state.confirmPassword,
                    onValueChange = { viewModel.onAction(RegisterAction.ConfirmPasswordChanged(it)) },
                    label = "Confirm Password",
                    placeholder = "Re-enter your password",
                    isError = state.confirmPasswordError != null,
                    supportingText = state.confirmPasswordError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None
                                           else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                painter = painterResource(
                                    if (confirmPasswordVisible) R.drawable.outlined_visible
                                    else R.drawable.outlined_not_visible
                                ),
                                contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password",
                                tint = StocklifyTheme.colors.grayscale400
                            )
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(StocklifySize.large))

            StocklifyPrimaryButton(
                fullWidth = true,
                text = "Sign Up",
                isLoading = state.isLoading,
                onClick = { viewModel.onAction(RegisterAction.Submit) }
            )
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
            text = "Create Your Account",
            style = StocklifyTextStyles.heading4,
            color = StocklifyTheme.colors.primary700
        )

        Text(
            text = "Let's get your store set up.",
            style = StocklifyTextStyles.subtitleS,
            color = StocklifyTheme.colors.typography400
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(
        onBack = {},
        onRegistered = {}
    )
}
