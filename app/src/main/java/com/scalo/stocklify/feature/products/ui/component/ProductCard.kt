package com.scalo.stocklify.feature.products.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.scalo.stocklify.R
import com.scalo.stocklify.core.ui.components.texts.DrawableText
import com.scalo.stocklify.core.ui.theme.StocklifyRadius
import com.scalo.stocklify.core.ui.theme.StocklifySize
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyStaticColors
import com.scalo.stocklify.core.ui.theme.StocklifyStroke
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme
import com.scalo.stocklify.core.util.IconPosition

@Composable
fun ProductCard(
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
    image: Painter? = null,
    title: String? = null,
    sellingPrice: Double? = null,
    costPrice: Double? = null,
    stock: Int? = null,
    category: String? = null,
    isSelected: Boolean? = null,
    onSelectionChange: ((Boolean) -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = StocklifyStroke.s,
                color = StocklifyTheme.colors.primary200,
                shape = RoundedCornerShape(StocklifyRadius.s)
            )
            .padding(StocklifySpacing.screenPadding),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(
            StocklifySpacing.columnGap
        )
    ) {
        if (isSelected != null) {
            CompositionLocalProvider(
                LocalMinimumInteractiveComponentSize provides Dp.Unspecified
            ) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = onSelectionChange,
                    modifier = Modifier.requiredSize(12.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = StocklifyTheme.colors.primary500,
                        uncheckedColor = StocklifyTheme.colors.grayscale300,
                        checkmarkColor = StocklifyStaticColors.white
                    )
                )
            }
        }

        Image(
            painter = image ?: painterResource(R.drawable.filled_photo),
            contentDescription = title ?: "Product",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(72.dp)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            DrawableText(
                text = buildAnnotatedString {
                    append(title ?: "Set Product Name")
                },
                painter = painterResource(R.drawable.outlined_pen),
                iconPosition = IconPosition.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEditClick() },
                textStyle = StocklifyTextStyles.subtitleS,
                textColor = if (title != null) StocklifyTheme.colors.primary700
                else StocklifyTheme.colors.error,
                iconTint = StocklifyTheme.colors.grayscale500,
                iconSize = StocklifySize.extraSmall,
                fullWidth = true
            )

            Spacer(modifier = Modifier.height(StocklifySpacing.s))

            ProductDetailRow(
                label = "Selling Price",
                value = if (sellingPrice != null) "₱%.2f".format(sellingPrice) else "Set Selling Price",
                valueColor = if (sellingPrice != null) StocklifyTheme.colors.typography500
                             else StocklifyTheme.colors.error
            )

            ProductDetailRow(
                label = "Cost Price",
                value = if (costPrice != null) "₱%.2f".format(costPrice) else "Set Cost Price",
                valueColor = if (costPrice != null) StocklifyTheme.colors.typography500
                             else StocklifyTheme.colors.error
            )

            ProductDetailRow(
                label = "Stock",
                value = stock?.toString() ?: "Set Stock",
                valueColor = if (stock != null) StocklifyTheme.colors.typography500
                             else StocklifyTheme.colors.error
            )

            ProductDetailRow(
                label = "Category",
                value = category ?: "Set Category",
                valueColor = StocklifyTheme.colors.typography500
            )
        }
    }
}

@Composable
private fun ProductDetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: androidx.compose.ui.graphics.Color = StocklifyTheme.colors.typography500,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = StocklifyTextStyles.subtitleXs,
            color = StocklifyTheme.colors.typography100
        )

        Text(
            text = value,
            style = StocklifyTextStyles.bodyXs,
            color = valueColor
        )
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun ProductCardPreview() {
    ProductCard(
        onEditClick = {},
        title = "Ligo Sardines",
        sellingPrice = 25.50,
        costPrice = 18.00,
        stock = 120,
        category = "Canned Goods"
    )
}