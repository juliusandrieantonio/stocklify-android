package com.scalo.stocklify.feature.products.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.scalo.stocklify.R
import com.scalo.stocklify.core.ui.components.buttons.StocklifyOutlineButton
import com.scalo.stocklify.core.ui.components.buttons.StocklifyPrimaryButton
import com.scalo.stocklify.core.ui.components.topbar.StocklifyTopBar
import com.scalo.stocklify.core.ui.theme.StocklifySize
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme
import com.scalo.stocklify.feature.products.model.ScannedProductResult
import com.scalo.stocklify.feature.products.ui.ScanProductViewModel
import com.scalo.stocklify.feature.products.ui.ScanningState
import com.scalo.stocklify.feature.products.ui.component.ProductCard

@Composable
fun ReviewImportedProductScreen(
    onBack: () -> Unit,
    onImport: () -> Unit,
    onEditManually: () -> Unit,
    viewModel: ScanProductViewModel = hiltViewModel(),
) {
    val scanningState by viewModel.scanningState.collectAsState()
    val results = (scanningState as? ScanningState.Success)?.results ?: emptyList()

    var selectedIndices by remember(results) {
        mutableStateOf(results.indices.toSet())
    }

    val selectedCount = selectedIndices.size

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
        ) {
            HeaderSection(
                count = results.size,
                modifier = Modifier.padding(horizontal = StocklifySpacing.screenPadding)
            )
            
            Spacer(modifier = Modifier.height(StocklifySize.medium))

            ProductList(
                results = results,
                selectedIndices = selectedIndices,
                onToggle = { index ->
                    selectedIndices = if (index in selectedIndices)
                        selectedIndices - index
                    else
                        selectedIndices + index
                },
                modifier = Modifier.weight(1f)
            )

            BottomActions(
                selectedCount = selectedCount,
                onImport = onImport,
                onEditManually = onEditManually,
                modifier = Modifier.padding(StocklifySpacing.screenPadding)
            )
        }
    }
}

@Composable
private fun HeaderSection(count: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap)
    ) {
        Text(
            text = "Review Imported Products",
            style = StocklifyTextStyles.heading4,
            color = StocklifyTheme.colors.primary700
        )
        Text(
            text = "We found $count product${if (count != 1) "s" else ""}. Select which ones to import.",
            style = StocklifyTextStyles.subtitleS,
            color = StocklifyTheme.colors.typography400,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ProductList(
    results: List<ScannedProductResult>,
    selectedIndices: Set<Int>,
    onToggle: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            horizontal = StocklifySpacing.screenPadding,
            vertical = StocklifySpacing.columnGap
        ),
        verticalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap)
    ) {
        itemsIndexed(results) { index, result ->
            val isSelected = index in selectedIndices
            ProductCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle(index) },
                title = result.productName,
                price = result.price,
                category = result.matchedCategory?.name,
                isSelected = isSelected,
                onSelectionChange = { onToggle(index) },
                onEditClick = { /* TODO: inline edit */ }
            )
        }
    }
}

@Composable
private fun BottomActions(
    selectedCount: Int,
    onImport: () -> Unit,
    onEditManually: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap)
    ) {
        StocklifyPrimaryButton(
            text = "Import $selectedCount Product${if (selectedCount != 1) "s" else ""}",
            onClick = onImport,
            fullWidth = true,
        )
        StocklifyOutlineButton(
            text = "Edit Manually",
            onClick = onEditManually,
            fullWidth = true
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReviewImportedProductScreenPreview() {
    ReviewImportedProductScreen(
        onBack = {},
        onImport = {},
        onEditManually = {}
    )
}
