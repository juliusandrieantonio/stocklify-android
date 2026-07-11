package com.scalo.stocklify.feature.auth.ui

import io.github.jan.supabase.auth.exception.AuthErrorCode
import io.github.jan.supabase.auth.exception.AuthRestException
import java.io.IOException

private const val GENERIC_ERROR = "Something went wrong. Please try again."
private const val NETWORK_ERROR = "Unable to connect. Please check your internet connection and try again."
private const val RATE_LIMIT_ERROR = "Too many attempts. Please wait a moment and try again."

fun Throwable.toLoginErrorMessage(): String = when {
    this is AuthRestException -> when (errorCode) {
        AuthErrorCode.InvalidCredentials,
        AuthErrorCode.UserNotFound -> "Incorrect email or password."

        AuthErrorCode.EmailNotConfirmed -> "Please verify your email before logging in."

        AuthErrorCode.UserBanned -> "This account has been suspended. Contact support for help."

        AuthErrorCode.OverRequestRateLimit,
        AuthErrorCode.OverEmailSendRateLimit,
        AuthErrorCode.OverSmsSendRateLimit -> RATE_LIMIT_ERROR

        AuthErrorCode.CaptchaFailed -> "Verification failed. Please try again."

        else -> GENERIC_ERROR
    }

    this is IOException -> NETWORK_ERROR

    else -> GENERIC_ERROR
}

fun Throwable.toRegisterErrorMessage(): String = when {
    this is AuthRestException -> when (errorCode) {
        AuthErrorCode.EmailExists,
        AuthErrorCode.UserAlreadyExists -> "An account with this email already exists. Try logging in instead."

        AuthErrorCode.WeakPassword -> "Please choose a stronger password."

        AuthErrorCode.ValidationFailed,
        AuthErrorCode.BadJson -> "Please check your details and try again."

        AuthErrorCode.SignupDisabled,
        AuthErrorCode.EmailProviderDisabled -> "Sign ups aren't available right now. Please try again later."

        AuthErrorCode.OverRequestRateLimit,
        AuthErrorCode.OverEmailSendRateLimit,
        AuthErrorCode.OverSmsSendRateLimit -> RATE_LIMIT_ERROR

        AuthErrorCode.CaptchaFailed -> "Verification failed. Please try again."

        else -> GENERIC_ERROR
    }

    this is IOException -> NETWORK_ERROR

    else -> GENERIC_ERROR
}

fun Throwable.toVerifyEmailErrorMessage(): String = when {
    this is AuthRestException -> when (errorCode) {
        AuthErrorCode.OverRequestRateLimit,
        AuthErrorCode.OverEmailSendRateLimit,
        AuthErrorCode.OverSmsSendRateLimit -> RATE_LIMIT_ERROR

        else -> GENERIC_ERROR
    }

    this is IOException -> NETWORK_ERROR

    else -> GENERIC_ERROR
}
