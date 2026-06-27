package com.scalo.stocklify.feature.salesmode.ui

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.scalo.stocklify.R
import com.scalo.stocklify.core.model.models.Category
import com.scalo.stocklify.core.util.categoryIcon
import com.scalo.stocklify.core.ui.components.EmptyState
import com.scalo.stocklify.core.ui.components.buttons.StocklifyOutlineButton
import com.scalo.stocklify.core.ui.components.chips.StocklifyFilterChip
import com.scalo.stocklify.core.ui.components.texts.StocklifyTextField
import com.scalo.stocklify.core.ui.theme.StocklifyRadius
import com.scalo.stocklify.core.ui.theme.StocklifySize
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyStaticColors
import com.scalo.stocklify.core.ui.theme.StocklifyStroke
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme

@Composable
fun SalesModeScreen(
    onAddManually: () -> Unit,
    onScanProduct: () -> Unit,
    viewModel: SalesModeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    SalesModeContent(
        state = state,
        onAction = viewModel::onAction,
        onAddManually = onAddManually,
        onScanProduct = onScanProduct,
    )
}

@Composable
private fun SalesModeContent(
    state: SalesModeUiState,
    onAction: (SalesModeAction) -> Unit,
    onAddManually: () -> Unit,
    onScanProduct: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap)
    ) {
        SearchField()

        CategoryChips(
            categories = state.categories,
            selectedCategoryId = state.selectedCategoryId,
            onSelect = { onAction(SalesModeAction.SelectCategory(it)) },
        )

        Products(
            products = state.products,
            onAddManually = onAddManually,
            onScanProduct = onScanProduct,
        )
    }
}

@Composable
private fun CategoryChips(
    categories: List<Category>,
    selectedCategoryId: Long?,
    onSelect: (Long?) -> Unit,
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap)
    ) {
        item {
            StocklifyFilterChip(
                label = "All",
                selected = selectedCategoryId == null,
                onToggle = { onSelect(null) },
                topIcon = {
                    Image(
                        painter = painterResource(R.drawable.outlined_dashboard),
                        contentDescription = null,
                        modifier = Modifier.size(StocklifySize.extraSmall),
                        colorFilter = ColorFilter.tint(
                            if (selectedCategoryId == null) {
                                StocklifyStaticColors.white
                            } else {
                                StocklifyTheme.colors.primary700
                            }
                        )
                    )
                },
                contentPadding = PaddingValues(
                    horizontal = StocklifySpacing.xl,
                    vertical = StocklifySpacing.m
                ),
            )
        }
        items(items = categories, key = { it.id }) { category ->
            val iconRes = categoryIcon(category.name)
            val isSelected = selectedCategoryId == category.id
            StocklifyFilterChip(
                label = category.name,
                selected = isSelected,
                onToggle = { onSelect(category.id) },
                topIcon = iconRes?.let { res ->
                    {
                        Image(
                            painter = painterResource(res),
                            contentDescription = null,
                            modifier = Modifier.size(StocklifySize.extraSmall),
                            colorFilter = ColorFilter.tint(
                                if (isSelected) StocklifyStaticColors.white
                                else StocklifyTheme.colors.primary700
                            )
                        )
                    }
                },
                contentPadding = PaddingValues(
                    horizontal = StocklifySpacing.xl,
                    vertical = StocklifySpacing.m
                ),
            )
        }
    }
}

@Composable
private fun Products(
    products: List<ProductModel>,
    onAddManually: () -> Unit,
    onScanProduct: () -> Unit,
) {
    when {
        products.isEmpty() -> EmptyState(
            image = painterResource(R.drawable.sales_11),
            title = "No products yet",
            description = "Add products to start selling.",
            titleColor = StocklifyTheme.colors.primary700,
            titleStyle = StocklifyTextStyles.subtitleM,
            descriptionColor = StocklifyTheme.colors.primary700,
            descriptionStyle = StocklifyTextStyles.bodyXs,
            action = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap,
                        Alignment.CenterHorizontally)
                ) {

                    StocklifyOutlineButton(
                        modifier = Modifier.weight(1f),
                        text = "Add Manually",
                        onClick = onAddManually,
                        leadingIcon = {
                            Image(
                                painter = painterResource(R.drawable.outlined_pen),
                                contentDescription = "Edit",
                                modifier = Modifier.size(StocklifySize.extraSmall),
                                colorFilter = ColorFilter.tint(StocklifyTheme.colors.primary700)
                            )
                        }
                    )

                    StocklifyOutlineButton(
                        modifier = Modifier.weight(1f),
                        text = "Scan Product",
                        onClick = onScanProduct,
                        leadingIcon = {
                            Image(
                                painter = painterResource(R.drawable.outlined_scan),
                                contentDescription = "Edit",
                                modifier = Modifier.size(StocklifySize.extraSmall),
                                colorFilter = ColorFilter.tint(StocklifyTheme.colors.primary700)
                            )
                        }
                    )
                }
            }
        )
        else -> LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products) { ProductCard(it) }
        }
    }
}

@Composable
private fun ProductCard(product: ProductModel) {
    val context = LocalContext.current

    val imageBitmap by produceState<ImageBitmap?>(initialValue = null, key1 = product.imageUrl) {
        value = withContext(Dispatchers.IO) {
            product.imageUrl?.let { url ->
                runCatching {
                    context.contentResolver
                        .openInputStream(Uri.parse(url))
                        ?.use { BitmapFactory.decodeStream(it)?.asImageBitmap() }
                }.getOrNull()
            }
        }
    }

    Column(
        modifier = Modifier
            .border(
                width = StocklifyStroke.s,
                color = StocklifyTheme.colors.transparentPrimary600,
                shape = RoundedCornerShape(StocklifyRadius.s)
            )
            .padding(StocklifySpacing.m)
    ) {
        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap!!,
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
            )
        } else {
            Image(
                painter = painterResource(R.drawable.gardenia),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
            )
        }

        Spacer(modifier = Modifier.height(StocklifySize.extraSmall))
        Text(
            text = product.name,
            style = StocklifyTextStyles.subtitleXs,
            color = StocklifyTheme.colors.primary700
        )

        Text(
            text = "₱${product.sellingPrice}",
            style = StocklifyTextStyles.subtitleXs,
            color = StocklifyTheme.colors.typography400
        )

        Text(
            text = "Stock ${product.stock}",
            style = StocklifyTextStyles.subtitleXxs,
            color = StocklifyTheme.colors.typography100
        )
    }
}

@Composable
private fun SearchField() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap)
    ) {
        StocklifyTextField(
            modifier = Modifier.weight(1f),
            value = "",
            onValueChange = { },
            placeholder = "Search by Product...",
            leadingIcon = {
                Image(
                    painter = painterResource(R.drawable.outlined_search_1),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(StocklifyTheme.colors.grayscale400),
                    modifier = Modifier
                        .padding(horizontal = 14.dp)
                        .size(StocklifySize.extraSmall)
                )
            },
            trailingIcon = {
                Image(
                    painter = painterResource(R.drawable.outlined_mic),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(StocklifyTheme.colors.grayscale400),
                )
            },
        )

        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(R.drawable.outlined_filter),
                contentDescription = "Filter",
                tint = StocklifyTheme.colors.primary700
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SalesModeScreenPreview() {
    StocklifyTheme {
        SalesModeContent(
            onAddManually = {},
            onScanProduct = {},
            state = SalesModeUiState(
                categories = listOf(
                    Category(id = 1, name = "Canned Goods"),
                    Category(id = 2, name = "Beverages"),
                    Category(id = 3, name = "Snacks"),
                ),
                products = listOf(
                    ProductModel(id = 1, code = "P0001", name = "Ligo Sardines 155g", sellingPrice = 28.50, categoryId = 1, stock = 48),
                    ProductModel(id = 2, code = "P0002", name = "Coca-Cola 1.5L", sellingPrice = 75.00, categoryId = 2, stock = 12),
                ),
                selectedCategoryId = null,
            ),
            onAction = {},
        )
    }
}
