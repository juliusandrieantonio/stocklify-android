package com.scalo.stocklify.feature.salesmode.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.scalo.stocklify.R
import com.scalo.stocklify.core.ui.components.buttons.StocklifyPrimaryButton
import com.scalo.stocklify.core.ui.components.cards.StocklifyOutlineCard
import com.scalo.stocklify.core.ui.components.chips.StocklifyFilterChip
import com.scalo.stocklify.core.ui.components.texts.StocklifyTextField
import com.scalo.stocklify.core.ui.components.topbar.StocklifyTopBar
import com.scalo.stocklify.core.ui.theme.StocklifyRadius
import com.scalo.stocklify.core.ui.theme.StocklifySize
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyStroke
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme

private val quickCashOptions = listOf(100, 200, 500, 1000)

@Composable
fun CheckoutScreen(
    total: Double,
    onBack: () -> Unit,
    onCompleteSales: (payment: Double, change: Double) -> Unit,
) {
    var paymentInput by remember { mutableStateOf("") }
    val paymentReceived = paymentInput.toDoubleOrNull() ?: 0.0
    val change = paymentReceived - total

    Scaffold(
        topBar = {
            StocklifyTopBar(
                title = "Checkout",
                centerTitle = true,
                titleStyle = StocklifyTextStyles.heading5,
                titleColor = StocklifyTheme.colors.primary700,
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.outlined_left_arrow),
                            contentDescription = "Back",
                            tint = StocklifyTheme.colors.primary700,
                        )
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(StocklifySpacing.screenPadding),
            verticalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap),
        ) {
            StocklifyOutlineCard(
                modifier = Modifier.fillMaxWidth(),
                title = "Total Amount",
                content = "₱ ${"%.2f".format(total)}",
                titleColor = StocklifyTheme.colors.primary700,
                contentColor = StocklifyTheme.colors.primary700,
                titleStyle = StocklifyTextStyles.subtitleS,
                contentStyle = StocklifyTextStyles.heading4,
                trailingIcon = painterResource(R.drawable.checkout_2),
                trailingIconSize = StocklifySize.large
            )

            Spacer(modifier = Modifier.height(StocklifySpacing.m))

            StocklifyTextField(
                label = "Payment Received",
                value = paymentInput,
                onValueChange = { paymentInput = it },
                placeholder = "0.00",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            )

            Spacer(modifier = Modifier.height(StocklifySpacing.xs))

            Text(
                text = "Quick Cash Buttons",
                style = StocklifyTextStyles.subtitleXs,
                color = StocklifyTheme.colors.primary700
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap),
            ) {
                quickCashOptions.forEach { amount ->
                    StocklifyFilterChip(
                        modifier = Modifier.weight(1f),
                        label = "₱$amount",
                        selected = paymentInput == amount.toString(),
                        onToggle = { paymentInput = amount.toString() },
                    )
                }
            }

            Spacer(modifier = Modifier.height(StocklifySpacing.xl))

            SalesSummaryCard(
                total = total,
                paymentReceived = paymentReceived,
                change = change,
            )

            Spacer(modifier = Modifier.weight(1f))

            StocklifyPrimaryButton(
                text = "Complete Sales",
                onClick = { onCompleteSales(paymentReceived, if (change < 0) 0.0 else change) },
                fullWidth = true,
                enabled = paymentReceived >= total,
            )

        }
    }
}

@Composable
private fun SalesSummaryCard(
    total: Double,
    paymentReceived: Double,
    change: Double,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = StocklifyStroke.s, color = StocklifyTheme.colors.transparentPrimary600, shape = RoundedCornerShape(StocklifyRadius.s))
            .background(StocklifyTheme.colors.primary100, RoundedCornerShape(StocklifyTheme.radius.s))
            .padding(StocklifyTheme.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(StocklifySpacing.s),
    ) {
        SummaryRow(
            label = "Total Amount",
            value = "₱ ${"%.2f".format(total)}",
            labelStyle = StocklifyTextStyles.subtitleXs,
            valueStyle = StocklifyTextStyles.subtitleXs,
        )

        SummaryRow(
            label = "Payment Received",
            value = "₱ ${"%.2f".format(paymentReceived)}",
            labelStyle = StocklifyTextStyles.subtitleXs,
            valueStyle = StocklifyTextStyles.subtitleXs,
        )

        HorizontalDivider(color = StocklifyTheme.colors.primary300, thickness = StocklifyStroke.s)

        SummaryRow(
            label = "Change",
            value = "₱ ${"%.2f".format(if (change < 0) 0.0 else change)}",
            labelStyle = StocklifyTextStyles.subtitleS,
            valueStyle = StocklifyTextStyles.subtitleM,
            valueColor = StocklifyTheme.colors.success
        )
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    labelStyle: TextStyle,
    valueStyle: TextStyle,
    valueColor: Color? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = labelStyle,
            color = StocklifyTheme.colors.primary700,
        )
        Text(
            text = value,
            style = valueStyle,
            color = valueColor ?: Color.Unspecified
        )
    }
}
