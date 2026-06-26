package com.scalo.stocklify.feature.products.ui.screens

import android.graphics.BitmapFactory
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
import com.scalo.stocklify.feature.products.ui.ImportState
import com.scalo.stocklify.feature.products.ui.ScanProductViewModel
import com.scalo.stocklify.feature.products.ui.ScanningState
import com.scalo.stocklify.feature.products.ui.component.ProductCard

@Composable
fun ReviewImportedProductScreen(
    onBack: () -> Unit,
    onImport: (Int) -> Unit,
    onEditManually: () -> Unit,
    onEditProduct: (index: Int, ScannedProductResult) -> Unit,
    viewModel: ScanProductViewModel = hiltViewModel(),
) {
    val scanningState by viewModel.scanningState.collectAsState()
    val importState by viewModel.importState.collectAsState()
    val results = (scanningState as? ScanningState.Success)?.results ?: emptyList()

    var selectedIndices by remember(results.size) {
        mutableStateOf(results.indices.toSet())
    }

    val selectedCount = selectedIndices.size
    val isImporting = importState is ImportState.Loading

    LaunchedEffect(importState) {
        if (importState is ImportState.Success) {
            onImport((importState as ImportState.Success).importedCount)
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
                onEdit = { index -> onEditProduct(index, results[index]) },
                modifier = Modifier.weight(1f)
            )

            BottomActions(
                selectedCount = selectedCount,
                isImporting = isImporting,
                onImport = { viewModel.importProducts(selectedIndices) },
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
    onEdit: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

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
            val imagePainter by produceState<BitmapPainter?>(null, result.imageUri) {
                value = result.imageUri?.let { uri ->
                    withContext(Dispatchers.IO) {
                        context.contentResolver.openInputStream(uri)?.use {
                            BitmapFactory.decodeStream(it)?.asImageBitmap()?.let(::BitmapPainter)
                        }
                    }
                }
            }
            ProductCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle(index) },
                image = imagePainter,
                title = result.productName,
                price = result.price,
                stock = result.stock?.toInt(),
                category = result.matchedCategory?.name,
                isSelected = isSelected,
                onSelectionChange = { onToggle(index) },
                onEditClick = { onEdit(index) }
            )
        }
    }
}

@Composable
private fun BottomActions(
    selectedCount: Int,
    isImporting: Boolean,
    onImport: () -> Unit,
    onEditManually: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap)
    ) {
        StocklifyPrimaryButton(
            text = if (isImporting) "Importing…" else "Import $selectedCount Product${if (selectedCount != 1) "s" else ""}",
            onClick = onImport,
            fullWidth = true,
            enabled = !isImporting && selectedCount > 0,
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
        onImport = { _ -> },
        onEditManually = {},
        onEditProduct = { _, _ -> }
    )
}
