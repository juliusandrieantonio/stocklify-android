package com.scalo.stocklify.feature.salesmode.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import java.io.File
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.scalo.stocklify.R
import com.scalo.stocklify.core.ui.components.buttons.StocklifyPrimaryButton
import com.scalo.stocklify.core.ui.components.texts.StocklifyTextField
import com.scalo.stocklify.core.ui.theme.StocklifyRadius
import com.scalo.stocklify.core.ui.theme.StocklifySize
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyStaticColors
import com.scalo.stocklify.core.ui.theme.StocklifyStroke
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesModeProductSheet(
    product: ProductModel,
    categoryName: String?,
    existingQuantity: Int,
    onAddToSales: (quantity: Int) -> Unit,
    onDismiss: () -> Unit,
    resolveImageFile: suspend (String, String?) -> File?,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var quantity by remember(product.id) { mutableIntStateOf(existingQuantity.coerceAtLeast(1)) }

    val imageBitmap by rememberProductImageBitmap(product.id, product.imageUrl, resolveImageFile)

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
                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap!!,
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(StocklifySize.xLarge)
                            .border(
                                width = StocklifyStroke.s,
                                color = StocklifyTheme.colors.grayscale200,
                                shape = RoundedCornerShape(StocklifyRadius.s),
                            ),
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.gardenia),
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(StocklifySize.xLarge)
                            .border(
                                width = StocklifyStroke.s,
                                color = StocklifyTheme.colors.grayscale200,
                                shape = RoundedCornerShape(StocklifyRadius.s),
                            ),
                    )
                }

                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(StocklifySpacing.s)) {
                    Text(
                        text = product.name,
                        style = StocklifyTextStyles.subtitleM,
                        color = StocklifyTheme.colors.primary700,
                    )
                    Text(
                        text = "₱${"%.2f".format(product.sellingPrice)}",
                        style = StocklifyTextStyles.subtitleS,
                        color = StocklifyTheme.colors.typography400,
                    )
                    Text(
                        text = "Stock: ${product.stock}",
                        style = StocklifyTextStyles.bodyS,
                        color = if (product.stock == 0) StocklifyTheme.colors.error
                                else StocklifyTheme.colors.typography100,
                    )
                    categoryName?.let {
                        Text(
                            text = it,
                            style = StocklifyTextStyles.bodyS,
                            color = StocklifyTheme.colors.typography100,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(StocklifySpacing.xs))

            Text(
                text = "Quantity",
                style = StocklifyTextStyles.subtitleXs,
                color = StocklifyTheme.colors.typography400,
            )

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
                    onClick = { if (quantity > 1) quantity-- },
                    enabled = quantity > 1,
                    modifier = Modifier
                        .border(StocklifyTheme.stroke.s, borderColor, decreaseShape)
                        .clip(decreaseShape)
                        .padding(StocklifySpacing.xs),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outlined_minus),
                        contentDescription = "Decrease",
                        tint = if (quantity > 1) StocklifyTheme.colors.primary700
                        else StocklifyTheme.colors.grayscale300,
                    )
                }

                StocklifyTextField(
                    modifier = Modifier.weight(1f),
                    value = quantity.toString(),
                    onValueChange = { input ->
                        val parsed = input.toIntOrNull()
                        if (parsed != null) {
                            quantity = parsed.coerceIn(1, product.stock.toInt())
                        } else if (input.isEmpty()) {
                            quantity = 1
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textAlign = TextAlign.Center,
                    singleLine = true,
                )

                IconButton(
                    onClick = { if (quantity < product.stock) quantity++ },
                    enabled = quantity < product.stock,
                    modifier = Modifier
                        .border(StocklifyTheme.stroke.s, borderColor, increaseShape)
                        .clip(increaseShape)
                        .padding(StocklifySpacing.xs),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outlined_plus),
                        contentDescription = "Increase",
                        tint = if (quantity < product.stock) StocklifyTheme.colors.primary700
                        else StocklifyTheme.colors.grayscale300,
                    )
                }
            }

            StocklifyPrimaryButton(
                text = "Add to Sales",
                onClick = {
                    onAddToSales(quantity)
                    onDismiss()
                },
                fullWidth = true,
                enabled = product.stock > 0,
            )

            Spacer(modifier = Modifier.height(StocklifySpacing.l))
        }
    }
}
