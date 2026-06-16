package com.scalo.stocklify.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val Roboto = FontFamily.Default

private fun lineHeight110(fontSizeSp: Float) = (fontSizeSp * 1.1f).sp
private fun lineHeight140(fontSizeSp: Float) = (fontSizeSp * 1.4f).sp
private fun lineHeight100(fontSizeSp: Float) = fontSizeSp.sp

object StocklifyTextStyles {
    val heading1 = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 54.sp,
        lineHeight = lineHeight110(54f),
        letterSpacing = 0.sp,
    )
    val heading2 = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 42.sp,
        lineHeight = lineHeight110(42f),
        letterSpacing = 0.sp,
    )
    val heading3 = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = lineHeight110(32f),
        letterSpacing = 0.sp,
    )
    val heading4 = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = lineHeight110(24f),
        letterSpacing = 0.sp,
    )
    val heading5 = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = lineHeight110(20f),
        letterSpacing = 0.sp,
    )
    val heading6 = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = lineHeight110(18f),
        letterSpacing = 0.sp,
    )
    val subtitleL = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = lineHeight110(18f),
        letterSpacing = 0.sp,
    )
    val subtitleM = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = lineHeight110(16f),
        letterSpacing = 0.sp,
    )
    val subtitleS = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = lineHeight110(14f),
        letterSpacing = 0.sp,
    )
    val subtitleXs = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = lineHeight110(12f),
        letterSpacing = 0.sp,
    )
    val subtitleXxs = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = lineHeight110(10f),
        letterSpacing = 0.sp,
    )
    val bodyL = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = lineHeight140(18f),
        letterSpacing = 0.sp,
    )
    val bodyM = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = lineHeight140(16f),
        letterSpacing = 0.sp,
    )
    val bodyS = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = lineHeight140(14f),
        letterSpacing = 0.sp,
    )
    val bodyXs = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = lineHeight140(12f),
        letterSpacing = 0.sp,
    )
    val buttonL = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = lineHeight100(20f),
        letterSpacing = 0.5.sp,
    )
    val buttonM = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = lineHeight100(16f),
        letterSpacing = 0.5.sp,
    )
    val buttonS = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = lineHeight100(14f),
        letterSpacing = 0.5.sp,
    )
    val menuAndTabs = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = lineHeight100(14f),
        letterSpacing = 0.5.sp,
    )
    val caption = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = lineHeight100(20f),
        letterSpacing = 1.sp,
    )
}

val Typography = Typography(
    displayLarge = StocklifyTextStyles.heading1,
    displayMedium = StocklifyTextStyles.heading2,
    displaySmall = StocklifyTextStyles.heading3,
    headlineLarge = StocklifyTextStyles.heading4,
    headlineMedium = StocklifyTextStyles.heading5,
    headlineSmall = StocklifyTextStyles.heading6,
    titleLarge = StocklifyTextStyles.subtitleL,
    titleMedium = StocklifyTextStyles.subtitleM,
    titleSmall = StocklifyTextStyles.subtitleS,
    bodyLarge = StocklifyTextStyles.bodyL,
    bodyMedium = StocklifyTextStyles.bodyM,
    bodySmall = StocklifyTextStyles.bodyS,
    labelLarge = StocklifyTextStyles.buttonL,
    labelMedium = StocklifyTextStyles.buttonM,
    labelSmall = StocklifyTextStyles.buttonS,
)
