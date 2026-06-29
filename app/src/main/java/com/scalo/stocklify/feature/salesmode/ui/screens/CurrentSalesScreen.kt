package com.scalo.stocklify.feature.salesmode.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.scalo.stocklify.R
import com.scalo.stocklify.core.ui.components.buttons.StocklifyOutlineButton
import com.scalo.stocklify.core.ui.components.buttons.StocklifyPrimaryButton
import androidx.compose.foundation.background
import com.scalo.stocklify.core.ui.components.topbar.StocklifyTopBar
import com.scalo.stocklify.core.ui.theme.StocklifyRadius
import com.scalo.stocklify.core.ui.theme.StocklifySize
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyStroke
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme
import com.scalo.stocklify.feature.salesmode.ui.CartItem
import com.scalo.stocklify.feature.salesmode.ui.SalesModeAction
import com.scalo.stocklify.feature.salesmode.ui.SalesModeViewModel

@Composable
fun CurrentSalesScreen(
    viewModel: SalesModeViewModel,
    onBack: () -> Unit,
    onCheckout: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    Scaffold(
        topBar = {
            StocklifyTopBar(
                title = "Current Sales",
                centerTitle = true,
                titleStyle = StocklifyTextStyles.heading5,
                titleColor = StocklifyTheme.colors.primary700,
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
                .padding(StocklifySpacing.screenPadding),
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${state.cartItems.size} items",
                    style = StocklifyTextStyles.subtitleS,
                    color = StocklifyTheme.colors.primary700,
                    modifier = Modifier.weight(1f),
                )

                Text(
                    text = "Clear",
                    style = StocklifyTextStyles.subtitleS,
                    modifier = Modifier.clickable { viewModel.onAction(SalesModeAction.ClearCart) }
                )
            }

            Spacer(modifier = Modifier.height(StocklifySpacing.columnGap))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap),
            ) {
                items(state.cartItems, key = { it.product.id }) { cartItem ->
                    CartItemRow(
                        cartItem = cartItem,
                        onRemove = { viewModel.onAction(SalesModeAction.RemoveFromCart(cartItem.product.id)) },
                        onQuantityChange = { qty -> viewModel.onAction(SalesModeAction.AddToCart(cartItem.product, qty)) },
                    )
                }
            }


            Spacer(modifier = Modifier.height(StocklifySpacing.columnGap))

            StocklifyOutlineButton(
                text = "Add More Product",
                onClick = onBack,
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.outlined_plus),
                        contentDescription = null,
                        tint = StocklifyTheme.colors.primary700,
                        modifier = Modifier.size(StocklifySize.extraSmall),
                    )
                },
                fullWidth = true
            )

            Spacer(modifier = Modifier.height(StocklifySpacing.columnGap))

            OrderSummary(
                total = state.cartTotal,
            )

            Spacer(modifier = Modifier.height(StocklifySpacing.columnGap))

            StocklifyPrimaryButton(
                text = "Checkout",
                onClick = onCheckout,
                fullWidth = true,
                enabled = state.cartItems.isNotEmpty(),
            )

        }
    }
}

@Composable
private fun CartItemRow(
    cartItem: CartItem,
    onRemove: () -> Unit,
    onQuantityChange: (Int) -> Unit,
) {
    val context = LocalContext.current
    val product = cartItem.product
    var quantity by remember(cartItem.quantity) { mutableIntStateOf(cartItem.quantity) }

    val imageBitmap by produceState<ImageBitmap?>(initialValue = null, key1 = product.imageUrl) {
        value = withContext(Dispatchers.IO) {
            product.imageUrl?.let { url ->
                runCatching {
                    context.contentResolver
                        .openInputStream(url.toUri())
                        ?.use { BitmapFactory.decodeStream(it)?.asImageBitmap() }
                }.getOrNull()
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = StocklifyStroke.s,
                color = StocklifyTheme.colors.grayscale200,
                shape = RoundedCornerShape(StocklifyRadius.s)
            )
            .padding(horizontal = StocklifySpacing.m, vertical = StocklifySpacing.xl),
        horizontalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap),
        verticalAlignment = Alignment.CenterVertically,

    ) {
        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap!!,
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(StocklifySize.medium)
                    .clip(RoundedCornerShape(StocklifyRadius.s))
                    .border(StocklifyStroke.s, StocklifyTheme.colors.grayscale200, RoundedCornerShape(StocklifyRadius.s)),
            )
        } else {
            Image(
                painter = painterResource(R.drawable.gardenia),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(StocklifySize.medium)
                    .clip(RoundedCornerShape(StocklifyRadius.s))
                    .border(StocklifyStroke.s, StocklifyTheme.colors.grayscale200, RoundedCornerShape(StocklifyRadius.s)),
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = product.name,
                style = StocklifyTextStyles.subtitleS,
                color = StocklifyTheme.colors.primary700,
            )
            Spacer(modifier = Modifier.height(StocklifySpacing.xs))
            Text(
                text = "₱${product.sellingPrice}",
                style = StocklifyTextStyles.subtitleS,
                color = StocklifyTheme.colors.typography400,
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val decreaseShape = RoundedCornerShape(
                    topStart = StocklifyTheme.radius.s,
                    bottomStart = StocklifyTheme.radius.s,
                    topEnd = 0.dp,
                    bottomEnd = 0.dp,
                )
                val increaseShape = RoundedCornerShape(
                    topStart = 0.dp,
                    bottomStart = 0.dp,
                    topEnd = StocklifyTheme.radius.s,
                    bottomEnd = StocklifyTheme.radius.s,
                )
                val borderColor = StocklifyTheme.colors.grayscale200

                IconButton(
                    onClick = { if (quantity > 1) { quantity--; onQuantityChange(quantity) } },
                    enabled = quantity > 1,
                    modifier = Modifier
                        .size(28.dp)
                        .border(StocklifyTheme.stroke.s, borderColor, decreaseShape)
                        .clip(decreaseShape),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outlined_minus),
                        contentDescription = "Decrease",
                        modifier = Modifier.size(12.dp),
                        tint = if (quantity > 1) StocklifyTheme.colors.primary700
                        else StocklifyTheme.colors.grayscale300,
                    )
                }

                BasicTextField(
                    value = quantity.toString(),
                    onValueChange = { input ->
                        val parsed = input.toIntOrNull()
                        if (parsed != null) {
                            quantity = parsed.coerceIn(1, product.stock.toInt())
                            onQuantityChange(quantity)
                        } else if (input.isEmpty()) {
                            quantity = 1
                            onQuantityChange(quantity)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    textStyle = StocklifyTextStyles.bodyS.copy(
                        textAlign = TextAlign.Center,
                        color = StocklifyTheme.colors.primary700,
                    ),
                    modifier = Modifier
                        .width(48.dp)
                        .height(28.dp)
                        .border(StocklifyTheme.stroke.s, borderColor)
                        .padding(horizontal = 4.dp, vertical = 6.dp),
                )

                IconButton(
                    onClick = { if (quantity < product.stock) { quantity++; onQuantityChange(quantity) } },
                    enabled = quantity < product.stock,
                    modifier = Modifier
                        .size(28.dp)
                        .border(StocklifyTheme.stroke.s, borderColor, increaseShape)
                        .clip(increaseShape),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outlined_plus),
                        contentDescription = "Increase",
                        modifier = Modifier.size(12.dp),
                        tint = if (quantity < product.stock) StocklifyTheme.colors.primary700
                        else StocklifyTheme.colors.grayscale300,
                    )
                }

                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(28.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outlined_trashbin),
                        contentDescription = "Delete",
                        modifier = Modifier.size(16.dp),
                        tint = StocklifyTheme.colors.primary700
                    )
                }
            }

            Spacer(modifier = Modifier.height(StocklifySpacing.xs))

            Text(
                text = "₱${product.sellingPrice * cartItem.quantity}",
                style = StocklifyTextStyles.subtitleS,
                color = StocklifyTheme.colors.primary700,
            )
        }
    }
}

@Composable
private fun OrderSummary(total: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = StocklifyStroke.s,
                color = StocklifyTheme.colors.primary300,
                shape = RoundedCornerShape(StocklifyRadius.s)
            )
            .background(StocklifyTheme.colors.primary100, RoundedCornerShape(StocklifyTheme.radius.s))
            .padding(StocklifyTheme.spacing.l),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Total",
            style = StocklifyTextStyles.heading6,
            color = StocklifyTheme.colors.primary700,
        )
        Text(
            text = "₱ ${"%.2f".format(total)}",
            style = StocklifyTextStyles.heading5,
            color = StocklifyTheme.colors.primary700,
        )
    }
}
