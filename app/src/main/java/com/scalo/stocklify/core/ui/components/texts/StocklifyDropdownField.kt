package com.scalo.stocklify.core.ui.components.texts

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.scalo.stocklify.core.ui.theme.*
import com.scalo.stocklify.core.ui.theme.StocklifyStaticColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StocklifyDropdownField(
    value: String,
    options: List<String>,
    onValueSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    enabled: Boolean = true,
    optional: Boolean = false,
    isError: Boolean = false,
    supportingText: String? = null
) {

    var expanded by remember { mutableStateOf(false) }

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

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                if (enabled) {
                    expanded = !expanded
                }
            }
        ) {

            OutlinedTextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                enabled = enabled,
                isError = isError,
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryEditable, enabled)
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
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                shape = RoundedCornerShape(StocklifyRadius.s),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = StocklifyTheme.colors.primary500,
                    unfocusedBorderColor = StocklifyTheme.colors.grayscale200,
                    disabledBorderColor = StocklifyTheme.colors.grayscale200,
                    errorBorderColor = StocklifyTheme.colors.error,
                    focusedTrailingIconColor = StocklifyTheme.colors.typography400,
                    unfocusedTrailingIconColor = StocklifyTheme.colors.typography400,
                    disabledTrailingIconColor = StocklifyTheme.colors.grayscale400,
                    errorTrailingIconColor = StocklifyTheme.colors.error,
                    cursorColor = StocklifyTheme.colors.primary500,
                    focusedTextColor = StocklifyTheme.colors.typography400,
                    unfocusedTextColor = StocklifyTheme.colors.typography400,
                    disabledTextColor = StocklifyTheme.colors.grayscale400,
                ),
                textStyle = StocklifyTheme.textStyles.bodyS
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = StocklifyStaticColors.white
            ) {

                options.forEach { option ->

                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option,
                                style = StocklifyTextStyles.bodyS,
                                color = StocklifyTheme.colors.typography400
                            )
                        },
                        onClick = {
                            onValueSelected(option)
                            expanded = false
                        },
                        colors = MenuDefaults.itemColors(
                            textColor = StocklifyTheme.colors.typography400,
                            disabledTextColor = StocklifyTheme.colors.grayscale400,
                        )
                    )
                }
            }
        }

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