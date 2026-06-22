package com.scalo.stocklify.core.ui.theme

import androidx.compose.ui.graphics.Color

data class StocklifyColors(
    val primary100: Color,
    val primary200: Color,
    val primary300: Color,
    val primary400: Color,
    val primary500: Color,
    val primary600: Color,
    val primary700: Color,
    val typography100: Color,
    val typography200: Color,
    val typography300: Color,
    val typography400: Color,
    val typography500: Color,
    val typography600: Color,
    val grayscale100: Color,
    val grayscale200: Color,
    val grayscale300: Color,
    val grayscale400: Color,
    val grayscale500: Color,
    val grayscale600: Color,
    val grayscale700: Color,
    val success: Color,
    val error: Color,
    val warning: Color,
    val transparentGray500: Color,
    val transparentGray100: Color,
    val transparentPrimary600: Color
)

val LightStocklifyColors = StocklifyColors(
    primary100 = Color(0xFFEAEEFA),
    primary200 = Color(0xFFD5DDF6),
    primary300 = Color(0xFFABBCED),
    primary400 = Color(0xFF5878DA),
    primary500 = Color(0xFF2E57D1),
    primary600 = Color(0xFF2545A7),
    primary700 = Color(0xFF1C347D),
    typography100 = Color(0xFF71788E),
    typography200 = Color(0xFF515C7D),
    typography300 = Color(0xFF3C445C),
    typography400 = Color(0xFF282D3D),
    typography500 = Color(0xFF14171F),
    typography600 = Color(0xFF0E1016),
    grayscale100 = Color(0xFFECEFF8),
    grayscale200 = Color(0xFFDDE1EE),
    grayscale300 = Color(0xFFC2C9DD),
    grayscale400 = Color(0xFF9CA5C2),
    grayscale500 = Color(0xFF7983A1),
    grayscale600 = Color(0xFF5B637B),
    grayscale700 = Color(0xFF42495C),
    success = Color(0xFF40A14A),
    error = Color(0xFFD6292C),
    warning = Color(0xFFEC7113),
    transparentGray500 = Color(0xFF7983A1).copy(alpha = 0.06f),
    transparentGray100 = Color(0xFFECEFF8).copy(alpha = 0.92f),
    transparentPrimary600 = Color(0xFF2545A7).copy(alpha = 0.24f)
)

val DarkStocklifyColors = StocklifyColors(
    primary100 = Color(0xFF1C347D),
    primary200 = Color(0xFF2545A7),
    primary300 = Color(0xFF2E57D1),
    primary400 = Color(0xFF5878DA),
    primary500 = Color(0xFFABBCED),
    primary600 = Color(0xFFD5DDF6),
    primary700 = Color(0xFFEAEEFA),
    typography100 = Color(0xFF71788E),
    typography200 = Color(0xFFACB0BB),
    typography300 = Color(0xFFC6C9D2),
    typography400 = Color(0xFFE3E4E8),
    typography500 = Color(0xFFF1F2F4),
    typography600 = Color(0xFFF7F8FC),
    grayscale100 = Color(0xFF16181C),
    grayscale200 = Color(0xFF444855),
    grayscale300 = Color(0xFF71788E),
    grayscale400 = Color(0xFFACB0BB),
    grayscale500 = Color(0xFFC6C9D2),
    grayscale600 = Color(0xFFE3E4E8),
    grayscale700 = Color(0xFFF1F2F4),
    success = Color(0xFF76CB7F),
    error = Color(0xFFE56A6D),
    warning = Color(0xFFF19343),
    transparentGray500 = Color(0xFFC2C9DD).copy(alpha = 0.06f),
    transparentGray100 = Color(0xFF42495C).copy(alpha = 0.92f),
    transparentPrimary600 = Color(0xFFD5DDF6).copy(alpha = 0.24f)
)

object StocklifyStaticColors {
    val white = Color(0xFFFFFFFF)
    val black = Color(0xFF000000)
}