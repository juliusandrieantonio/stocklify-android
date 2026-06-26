package com.scalo.stocklify.core.ui.components.texts

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.scalo.stocklify.core.ui.theme.StocklifyRadius
import com.scalo.stocklify.core.ui.theme.StocklifyStroke
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme

@Composable
fun StocklifyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: String? = null,
    optional: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        label?.let {
            Row {
                Text(
                    text = it,
                    style = StocklifyTheme.textStyles.subtitleXs,
                    color = StocklifyTheme.colors.typography400
                )

                if (optional) {
                    Text(
                        text = " (Optional)",
                        style = StocklifyTheme.textStyles.subtitleXs,
                        color = StocklifyTheme.colors.grayscale400
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .border(
                    width = StocklifyStroke.s,
                    color = StocklifyTheme.colors.grayscale200,
                    shape = RoundedCornerShape(StocklifyRadius.s)
                ),
            placeholder = placeholder?.let {
                {
                    Text(
                        text = it,
                        style = StocklifyTextStyles.bodyS,
                        color = StocklifyTheme.colors.grayscale400
                    )
                }
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            singleLine = singleLine,
            enabled = enabled,
            isError = isError,
            keyboardOptions = keyboardOptions,
            shape = RoundedCornerShape(StocklifyRadius.s),
            textStyle = StocklifyTheme.textStyles.bodyM
        )

        supportingText?.let {
            Spacer(Modifier.height(4.dp))

            Text(
                text = it,
                style = StocklifyTheme.textStyles.subtitleXs,
                color = if (isError) StocklifyTheme.colors.error else StocklifyTheme.colors.typography400
            )
        }
    }
}