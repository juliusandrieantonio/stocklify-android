package com.scalo.stocklify.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object StocklifyStroke {
    val xs: Dp = 0.5.dp
    val s: Dp = 1.dp
    val m: Dp = 2.dp
    val l: Dp = 4.dp
}

object StocklifyRadius {
    val xxs: Dp = 2.dp
    val xs: Dp = 4.dp
    val s: Dp = 8.dp
    val m: Dp = 12.dp
    val l: Dp = 20.dp
    val xl: Dp = 30.dp
    val full: Dp = 999.dp
}

object StocklifySpacing {
    val xxs: Dp = 2.dp
    val xs: Dp = 4.dp
    val s: Dp = 8.dp
    val m: Dp = 12.dp
    val l: Dp = 16.dp
    val xl: Dp = 24.dp
    val xxl: Dp = 32.dp
    val xxxl: Dp = 48.dp
    val huge: Dp = 64.dp

    val screenPadding: Dp = 24.dp
    val columnGap: Dp = 12.dp
    val columnGapM: Dp = 24.dp
    val columnGapL: Dp = 36.dp

}

object StocklifySize {
    val extraExtraSmall: Dp = 12.dp
    val extraSmall: Dp = 18.dp
    val smallest: Dp = 24.dp
    val small: Dp = 36.dp
    val medium: Dp = 48.dp
    val large: Dp = 64.dp
    val xLarge: Dp = 86.dp
    val xxlarge: Dp = 128.dp
}

private val LocalStocklifyColors = staticCompositionLocalOf { LightStocklifyColors }

object StocklifyTheme {
    val colors: StocklifyColors
        @Composable
        @ReadOnlyComposable
        get() = LocalStocklifyColors.current

    val stroke: StocklifyStroke
        get() = StocklifyStroke

    val radius: StocklifyRadius
        get() = StocklifyRadius

    val spacing: StocklifySpacing
        get() = StocklifySpacing

    val textStyles: StocklifyTextStyles
        get() = StocklifyTextStyles
}

private fun stocklifyLightColorScheme(colors: StocklifyColors) = lightColorScheme(
    primary = colors.primary500,
    onPrimary = StocklifyStaticColors.white,
    primaryContainer = colors.primary100,
    onPrimaryContainer = colors.primary700,
    secondary = colors.grayscale500,
    onSecondary = StocklifyStaticColors.white,
    secondaryContainer = colors.grayscale100,
    onSecondaryContainer = colors.grayscale700,
    tertiary = colors.primary300,
    onTertiary = colors.primary700,
    background = StocklifyStaticColors.white,
    onBackground = colors.typography500,
    surface = StocklifyStaticColors.white,
    onSurface = colors.typography500,
    surfaceVariant = colors.grayscale100,
    onSurfaceVariant = colors.typography300,
    outline = colors.grayscale300,
    outlineVariant = colors.grayscale200,
    error = colors.error,
    onError = StocklifyStaticColors.white,
)

private fun stocklifyDarkColorScheme(colors: StocklifyColors) = darkColorScheme(
    primary = colors.primary500,
    onPrimary = colors.primary700,
    primaryContainer = colors.primary100,
    onPrimaryContainer = colors.primary600,
    secondary = colors.grayscale500,
    onSecondary = colors.grayscale100,
    secondaryContainer = colors.grayscale200,
    onSecondaryContainer = colors.grayscale700,
    tertiary = colors.primary300,
    onTertiary = colors.primary700,
    background = colors.grayscale100,
    onBackground = colors.typography500,
    surface = colors.grayscale100,
    onSurface = colors.typography500,
    surfaceVariant = colors.grayscale200,
    onSurfaceVariant = colors.typography300,
    outline = colors.grayscale400,
    outlineVariant = colors.grayscale300,
    error = colors.error,
    onError = StocklifyStaticColors.black,
)

@Composable
fun StocklifyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val stocklifyColors = if (darkTheme) DarkStocklifyColors else LightStocklifyColors

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> stocklifyDarkColorScheme(stocklifyColors)
        else -> stocklifyLightColorScheme(stocklifyColors)
    }

    CompositionLocalProvider(LocalStocklifyColors provides stocklifyColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content,
        )
    }
}