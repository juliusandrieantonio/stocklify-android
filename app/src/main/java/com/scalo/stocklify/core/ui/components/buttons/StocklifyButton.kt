package com.scalo.stocklify.core.ui.components.buttons
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.scalo.stocklify.core.ui.theme.StocklifyStaticColors
import com.scalo.stocklify.core.ui.theme.StocklifyTheme

enum class StocklifyButtonSize { Small, Medium, Large }
enum class StocklifyButtonVariant { Primary, Tertiary, Outline, Ghost }

@Composable
internal fun StocklifyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: StocklifyButtonVariant = StocklifyButtonVariant.Primary,
    size: StocklifyButtonSize = StocklifyButtonSize.Medium,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    fullWidth: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    val colors = StocklifyTheme.colors
    val radius = StocklifyTheme.radius
    val strokes = StocklifyTheme.stroke

    val height = when (variant) {
        StocklifyButtonVariant.Primary   -> 48.dp
        StocklifyButtonVariant.Tertiary -> 48.dp
        StocklifyButtonVariant.Outline   -> 48.dp
        StocklifyButtonVariant.Ghost     -> 48.dp
    }

    val textStyle = when (variant) {
        StocklifyButtonVariant.Primary   -> StocklifyTheme.textStyles.buttonS
        StocklifyButtonVariant.Tertiary -> StocklifyTheme.textStyles.buttonS
        StocklifyButtonVariant.Outline   -> StocklifyTheme.textStyles.buttonS
        StocklifyButtonVariant.Ghost     -> StocklifyTheme.textStyles.buttonS
    }

    val padding = when (size) {
        StocklifyButtonSize.Small  -> PaddingValues(horizontal = 12.dp)
        StocklifyButtonSize.Medium -> PaddingValues(horizontal = 20.dp)
        StocklifyButtonSize.Large  -> PaddingValues(horizontal = 28.dp)
    }

    val containerColor = when (variant) {
        StocklifyButtonVariant.Primary   -> colors.primary500
        StocklifyButtonVariant.Tertiary -> StocklifyStaticColors.white
        StocklifyButtonVariant.Outline   -> Color.Transparent
        StocklifyButtonVariant.Ghost     -> Color.Transparent
    }

    val contentColor = when (variant) {
        StocklifyButtonVariant.Primary   -> StocklifyStaticColors.white
        StocklifyButtonVariant.Tertiary -> colors.typography100
        StocklifyButtonVariant.Outline   -> colors.primary600
        StocklifyButtonVariant.Ghost     -> colors.primary500
    }

    val disabledContainerColor = when (variant) {
        StocklifyButtonVariant.Primary   -> colors.primary200
        StocklifyButtonVariant.Tertiary -> colors.grayscale100
        StocklifyButtonVariant.Outline   -> Color.Transparent
        StocklifyButtonVariant.Ghost     -> Color.Transparent
    }

    val disabledContentColor = when (variant) {
        StocklifyButtonVariant.Primary   -> colors.primary400
        StocklifyButtonVariant.Tertiary -> colors.typography300
        StocklifyButtonVariant.Outline   -> colors.grayscale300
        StocklifyButtonVariant.Ghost     -> colors.grayscale300
    }

    val stroke = when (variant) {
        StocklifyButtonVariant.Primary   -> strokes.s
        StocklifyButtonVariant.Tertiary -> strokes.s
        StocklifyButtonVariant.Outline   -> strokes.s
        StocklifyButtonVariant.Ghost     -> strokes.s
    }

    val shape = RoundedCornerShape(radius.s)

    val buttonModifier = modifier
        .height(height)
        .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
        .then(
            if (variant == StocklifyButtonVariant.Outline) {
                Modifier.border(
                    width = stroke,
                    color = if (enabled) StocklifyTheme.colors.transparentPrimary600 else colors.grayscale300,
                    shape = shape
                )
            } else Modifier
        )

    Button(
        onClick = onClick,
        modifier = buttonModifier,
        enabled = enabled && !isLoading,
        shape = shape,
        contentPadding = padding,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (variant == StocklifyButtonVariant.Primary) 2.dp else 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        ),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                color = contentColor,
                strokeWidth = stroke
            )
        } else {
            leadingIcon?.let {
                it()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = textStyle
            )
            trailingIcon?.let {
                Spacer(modifier = Modifier.width(8.dp))
                it()
            }
        }
    }
}