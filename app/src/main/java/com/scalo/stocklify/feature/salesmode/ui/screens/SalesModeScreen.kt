package com.scalo.stocklify.feature.salesmode.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.io.File
import com.scalo.stocklify.R
import com.scalo.stocklify.core.model.models.Category
import com.scalo.stocklify.core.util.categoryIcon
import com.scalo.stocklify.core.ui.components.EmptyState
import com.scalo.stocklify.core.ui.components.buttons.StocklifyOutlineButton
import com.scalo.stocklify.core.ui.components.cards.StocklifyPrimaryCard
import com.scalo.stocklify.core.ui.components.chips.StocklifyFilterChip
import com.scalo.stocklify.core.ui.components.texts.StocklifyTextField
import com.scalo.stocklify.core.ui.theme.StocklifyRadius
import com.scalo.stocklify.core.ui.theme.StocklifySize
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyStaticColors
import com.scalo.stocklify.core.ui.theme.StocklifyStroke
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme
import com.scalo.stocklify.feature.salesmode.ui.CartItem
import com.scalo.stocklify.feature.salesmode.ui.ProductModel
import com.scalo.stocklify.feature.salesmode.ui.SalesModeAction
import com.scalo.stocklify.feature.salesmode.ui.SalesModeFilterSheet
import com.scalo.stocklify.feature.salesmode.ui.SalesModeProductSheet
import com.scalo.stocklify.feature.salesmode.ui.SalesModeUiState
import com.scalo.stocklify.feature.salesmode.ui.SalesModeViewModel
import com.scalo.stocklify.feature.salesmode.ui.rememberProductImageBitmap

@Composable
fun SalesModeScreen(
    onAddManually: () -> Unit,
    onScanProduct: () -> Unit,
    onCurrentSales: () -> Unit = {},
    viewModel: SalesModeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    SalesModeContent(
        state = state,
        onAction = viewModel::onAction,
        onAddManually = onAddManually,
        onScanProduct = onScanProduct,
        onCurrentSales = onCurrentSales,
        resolveImageFile = viewModel::resolveProductImageFile,
    )
}

@Composable
private fun SalesModeContent(
    state: SalesModeUiState,
    onAction: (SalesModeAction) -> Unit,
    onAddManually: () -> Unit,
    onScanProduct: () -> Unit,
    onCurrentSales: () -> Unit,
    resolveImageFile: suspend (String, String?) -> File?,
) {

    var showFilterSheet by remember { mutableStateOf(false) }

    if (showFilterSheet) {
        SalesModeFilterSheet(
            current = state.filter,
            categories = state.categories,
            onApply = { onAction(SalesModeAction.ApplyFilter(it)) },
            onDismiss = { showFilterSheet = false },
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap)
        ) {
            SearchField(
                value = state.searchQuery,
                onValueChange = { onAction(SalesModeAction.Search(it)) },
                filterCount = state.filter.activeCount,
                onFilterClick = { showFilterSheet = true },
            )

            CategoryChips(
                categories = state.categories,
                selectedCategoryCode = state.filter.categoryCode,
                onSelect = { onAction(SalesModeAction.SelectCategory(it)) },
            )

            Products(
                products = state.products,
                cartItems = state.cartItems,
                categories = state.categories,
                onAddManually = onAddManually,
                onScanProduct = onScanProduct,
                onAddToCart = { product, qty -> onAction(SalesModeAction.AddToCart(product, qty)) },
                onRemoveFromCart = { onAction(SalesModeAction.RemoveFromCart(it)) },
                resolveImageFile = resolveImageFile,
            )
        }

        if (state.cartItems.isNotEmpty()) {
            TotalSales(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                itemCount = state.cartItems.sumOf { it.quantity },
                total = state.cartTotal,
                onClick = onCurrentSales,
            )
        }

    }
}

@Composable
private fun CategoryChips(
    categories: List<Category>,
    selectedCategoryCode: String?,
    onSelect: (String?) -> Unit,
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap)
    ) {
        item {
            StocklifyFilterChip(
                label = "All",
                selected = selectedCategoryCode == null,
                onToggle = { onSelect(null) },
                topIcon = {
                    Image(
                        painter = painterResource(R.drawable.outlined_dashboard),
                        contentDescription = null,
                        modifier = Modifier.size(StocklifySize.extraSmall),
                        colorFilter = ColorFilter.tint(
                            if (selectedCategoryCode == null) {
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
        items(items = categories, key = { it.code }) { category ->
            val iconRes = categoryIcon(category.name)
            val isSelected = selectedCategoryCode == category.code
            StocklifyFilterChip(
                label = category.name,
                selected = isSelected,
                onToggle = { onSelect(category.code) },
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
    cartItems: List<CartItem>,
    categories: List<Category>,
    onAddManually: () -> Unit,
    onScanProduct: () -> Unit,
    onAddToCart: (ProductModel, Int) -> Unit,
    onRemoveFromCart: (String) -> Unit,
    resolveImageFile: suspend (String, String?) -> File?,
) {
    var selectedProduct by remember { mutableStateOf<ProductModel?>(null) }

    selectedProduct?.let { product ->
        val categoryName = categories.find { it.code == product.categoryCode }?.name
        val existingQty = cartItems.find { it.product.id == product.id }?.quantity ?: 1
        SalesModeProductSheet(
            product = product,
            categoryName = categoryName,
            existingQuantity = existingQty,
            onAddToSales = { qty -> onAddToCart(product, qty) },
            onDismiss = { selectedProduct = null },
            resolveImageFile = resolveImageFile,
        )
    }

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
            items(products) { product ->
                val cartItem = cartItems.find { it.product.id == product.id }
                ProductCard(
                    product = product,
                    inCart = cartItem != null,
                    onTap = { selectedProduct = product },
                    onRemove = { onRemoveFromCart(product.id) },
                    resolveImageFile = resolveImageFile,
                )
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: ProductModel,
    inCart: Boolean,
    onTap: () -> Unit,
    onRemove: () -> Unit,
    resolveImageFile: suspend (String, String?) -> File?,
) {
    val imageBitmap by rememberProductImageBitmap(product.id, product.imageUrl, resolveImageFile)

    Column(
        modifier = Modifier
            .border(
                width = StocklifyStroke.s,
                color = if (inCart) StocklifyTheme.colors.primary500 else StocklifyTheme.colors.transparentPrimary600,
                shape = RoundedCornerShape(StocklifyRadius.s)
            )
            .background(
                if (inCart) StocklifyTheme.colors.primary100 else Color.Transparent
            )
            .clickable(onClick = onTap)
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
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
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

            IconButton(onClick = if (inCart) onRemove else onTap) {
                Icon(
                    painter = painterResource(
                        if (inCart) R.drawable.outlined_circle_minus
                        else R.drawable.outlined_circle_plus
                    ),
                    contentDescription = if (inCart) "Remove" else "Add",
                    tint = StocklifyTheme.colors.primary700
                )
            }
        }
    }
}

@Composable
private fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    filterCount: Int,
    onFilterClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap)
    ) {
        StocklifyTextField(
            modifier = Modifier.weight(1f),
            value = value,
            onValueChange = onValueChange,
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

        Box {
            IconButton(onClick = onFilterClick) {
                Icon(
                    painter = painterResource(if (filterCount > 0) R.drawable.filled_filter else R.drawable.outlined_filter),
                    contentDescription = "Filter",
                    tint = StocklifyTheme.colors.primary700,
                    modifier = Modifier.size(20.dp)
                )
            }
            if (filterCount > 0) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(StocklifyTheme.colors.primary500)
                        .align(Alignment.TopEnd),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = filterCount.toString(),
                        style = StocklifyTheme.textStyles.bodyS.copy(
                            fontSize = TextUnit(8f, TextUnitType.Sp)
                        ),
                        color = StocklifyStaticColors.white,
                    )
                }
            }
        }
    }
}

@Composable
fun TotalSales(modifier: Modifier, itemCount: Int, total: Double, onClick: (() -> Unit)? = null) {
    StocklifyPrimaryCard(
        modifier = modifier,
        title = "Current Sale ($itemCount ${if (itemCount == 1) "item" else "items"})",
        content = "₱ ${"%.2f".format(total)}",
        titleColor = StocklifyTheme.colors.primary500,
        contentColor = StocklifyTheme.colors.primary700,
        titleStyle = StocklifyTextStyles.subtitleXs,
        contentStyle = StocklifyTextStyles.subtitleL,
        leadingIcon = painterResource(R.drawable.filled_cart_3),
        leadingIconSize = StocklifySize.small,
        trailingIcon = painterResource(R.drawable.outlined_right_arrow),
        onClick = onClick,
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SalesModeScreenPreview() {
    StocklifyTheme {
        SalesModeContent(
            onAddManually = {},
            onScanProduct = {},
            onCurrentSales = {},
            state = SalesModeUiState(
                categories = listOf(
                    Category(code = "CANNED_GOODS", name = "Canned Goods"),
                    Category(code = "BEVERAGES", name = "Beverages"),
                    Category(code = "SNACKS", name = "Snacks"),
                ),
                products = listOf(
                    ProductModel(
                        id = "1",
                        storeProductId = "1",
                        code = "P0001",
                        name = "Ligo Sardines 155g",
                        sellingPrice = 28.50,
                        categoryCode = "CANNED_GOODS",
                        stock = 48
                    ),
                    ProductModel(
                        id = "2",
                        storeProductId = "2",
                        code = "P0002",
                        name = "Coca-Cola 1.5L",
                        sellingPrice = 75.00,
                        categoryCode = "BEVERAGES",
                        stock = 12
                    ),
                ),
            ),
            onAction = {},
            resolveImageFile = { _, _ -> null },
        )
    }
}
