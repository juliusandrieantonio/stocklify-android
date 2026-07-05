package com.scalo.stocklify.feature.salesmode.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.VerticalDivider
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.scalo.stocklify.R
import com.scalo.stocklify.core.model.models.Category
import com.scalo.stocklify.core.ui.components.buttons.StocklifyOutlineButton
import com.scalo.stocklify.core.ui.components.buttons.StocklifyPrimaryButton
import com.scalo.stocklify.core.ui.components.chips.StocklifyChipSize
import com.scalo.stocklify.core.ui.components.chips.StocklifyFilterChip
import com.scalo.stocklify.core.ui.components.texts.StocklifyDropdownField
import com.scalo.stocklify.core.ui.components.texts.StocklifyTextField
import com.scalo.stocklify.core.ui.theme.StocklifySize
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyStaticColors
import com.scalo.stocklify.core.ui.theme.StocklifyStroke
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesModeFilterSheet(
    current: SalesModeFilter,
    categories: List<Category>,
    onApply: (SalesModeFilter) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var draft by remember(current) { mutableStateOf(current) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = StocklifyStaticColors.white,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = StocklifySpacing.xl, vertical = StocklifySpacing.l),
            verticalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap),
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Filter Products",
                    style = StocklifyTextStyles.subtitleS,
                    color = StocklifyTheme.colors.primary700,
                    modifier = Modifier.weight(1f),
                )

                Text(
                    text = "Reset",
                    style = StocklifyTextStyles.subtitleS,
                    color = StocklifyTheme.colors.primary700,
                    modifier = Modifier.clickable(enabled = true) {
                        draft = SalesModeFilter()
                    }
                )

            }



            Spacer(modifier = Modifier.height(StocklifySpacing.xs))


            Text(
                text = "Category",
                style = StocklifyTextStyles.subtitleXs,
                color = StocklifyTheme.colors.typography400,
            )

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap),
            ) {
                StocklifyFilterChip(
                    label = "All",
                    selected = draft.categoryCode == null,
                    onToggle = { draft = draft.copy(categoryCode = null) },
                    size = StocklifyChipSize.Large,
                )
                categories.forEach { category ->
                    StocklifyFilterChip(
                        size = StocklifyChipSize.Large,
                        label = category.name,
                        selected = draft.categoryCode == category.code,
                        onToggle = { draft = draft.copy(categoryCode = category.code) },
                    )
                }
            }

            Spacer(modifier = Modifier.height(StocklifySpacing.xs))

            Text(
                text = "Price Range",
                style = StocklifyTextStyles.subtitleXs,
                color = StocklifyTheme.colors.typography400,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap),
            ) {
                StocklifyTextField(
                    leadingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(R.drawable.outlined_peso),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(StocklifyTheme.colors.grayscale400),
                                modifier = Modifier
                                    .padding(horizontal = 14.dp)
                                    .size(StocklifySize.extraSmall)
                            )
                            VerticalDivider(
                                modifier = Modifier
                                    .height(48.dp)
                                    .padding(end = 12.dp),
                                thickness = StocklifyStroke.s,
                                color = StocklifyTheme.colors.grayscale200,
                            )
                        }
                    },
                    modifier = Modifier.weight(1f),
                    value = draft.minPrice?.toString() ?: "",
                    onValueChange = { draft = draft.copy(minPrice = it.toDoubleOrNull()) },
                    label = "Min (₱)",
                    placeholder = "0.00",
                    optional = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                )
                StocklifyTextField(
                    leadingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(R.drawable.outlined_peso),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(StocklifyTheme.colors.grayscale400),
                                modifier = Modifier
                                    .padding(horizontal = 14.dp)
                                    .size(StocklifySize.extraSmall)
                            )
                            VerticalDivider(
                                modifier = Modifier
                                    .height(48.dp)
                                    .padding(end = 12.dp),
                                thickness = StocklifyStroke.s,
                                color = StocklifyTheme.colors.grayscale200,
                            )
                        }
                    },
                    modifier = Modifier.weight(1f),
                    value = draft.maxPrice?.toString() ?: "",
                    onValueChange = { draft = draft.copy(maxPrice = it.toDoubleOrNull()) },
                    label = "Max (₱)",
                    placeholder = "0.00",
                    optional = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                )
            }

            Spacer(modifier = Modifier.height(StocklifySpacing.xs))

            Text(
                text = "Stock Status",
                style = StocklifyTextStyles.subtitleXs,
                color = StocklifyTheme.colors.typography400,
            )

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap),
            ) {
                StockStatus.entries.forEach { status ->
                    StocklifyFilterChip(
                        size = StocklifyChipSize.Large,
                        label = status.label,
                        selected = draft.stockStatus == status,
                        onToggle = { draft = draft.copy(stockStatus = status) },
                    )
                }
            }

            Spacer(modifier = Modifier.height(StocklifySpacing.xs))

            Text(
                text = "Sort by",
                style = StocklifyTextStyles.subtitleXs,
                color = StocklifyTheme.colors.typography400,
            )

            StocklifyDropdownField(
                value = draft.sortOption.label,
                options = SortOption.entries.map { it.label },
                onValueSelected = { label ->
                    val selected = SortOption.entries.first { it.label == label }
                    draft = draft.copy(sortOption = selected)
                },
            )

            Spacer(modifier = Modifier.height(StocklifySpacing.xs))

            StocklifyPrimaryButton(
                text = "Apply",
                fullWidth = true,
                onClick = {
                    onApply(draft)
                    onDismiss()
                },
            )
        }
    }
}
