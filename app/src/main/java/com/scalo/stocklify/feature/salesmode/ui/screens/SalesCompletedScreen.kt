package com.scalo.stocklify.feature.salesmode.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.scalo.stocklify.R
import com.scalo.stocklify.core.ui.components.buttons.StocklifyOutlineButton
import com.scalo.stocklify.core.ui.components.buttons.StocklifyPrimaryButton
import com.scalo.stocklify.core.ui.components.texts.DrawableText
import com.scalo.stocklify.core.ui.theme.StocklifyRadius
import com.scalo.stocklify.core.ui.theme.StocklifySize
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyStroke
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun SalesCompletedScreen(
    total: Double,
    paymentReceived: Double,
    change: Double,
    onNewSales: () -> Unit,
    onViewRecords: () -> Unit,
) {
    val now = remember { LocalDateTime.now() }
    val dateTimeFormatter = remember { DateTimeFormatter.ofPattern("MMMM dd, yyyy • hh:mm a") }
    val transactionCode = remember { "TXN-${now.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))}" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(StocklifySpacing.screenPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ImageSection()

        Spacer(modifier = Modifier.height(StocklifySize.smallest))

        HeaderSection(
            transactionCode = transactionCode,
            dateTime = now.format(dateTimeFormatter),
        )

        Spacer(modifier = Modifier.height(StocklifySize.medium))

        SalesSummaryCard(
            total = total,
            paymentReceived = paymentReceived,
            change = change,
        )

        Spacer(modifier = Modifier.height(StocklifySize.extraSmall))

        DrawableText(
            text = buildAnnotatedString { append("Transaction saved successfully.") },
            textStyle = StocklifyTextStyles.subtitleXs,
            textColor = StocklifyTheme.colors.primary700,
            painter = painterResource(R.drawable.outlined_bill),
            iconTint = StocklifyTheme.colors.primary700,
            modifier = Modifier
                .background(StocklifyTheme.colors.primary100, RoundedCornerShape(StocklifyTheme.radius.s))
                .fillMaxWidth()
                .padding(StocklifySpacing.m)
        )

        Spacer(modifier = Modifier.height(StocklifySize.medium))

        StocklifyPrimaryButton(
            text = "New Sales",
            onClick = onNewSales,
            fullWidth = true,
        )

        Spacer(modifier = Modifier.height(StocklifySpacing.columnGap))

        StocklifyOutlineButton(
            text = "View Records",
            onClick = onViewRecords,
            fullWidth = true,
        )

        Spacer(modifier = Modifier.height(StocklifySpacing.columnGap))
    }
}

@Composable
private fun HeaderSection(transactionCode: String, dateTime: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(StocklifySpacing.columnGap),
    ) {
        Text(
            text = "Sale Completed!",
            style = StocklifyTextStyles.heading4,
            color = StocklifyTheme.colors.primary700,
        )
        Text(
            text = transactionCode,
            style = StocklifyTextStyles.subtitleXs,
            color = StocklifyTheme.colors.typography400,
            textAlign = TextAlign.Center,
        )
        Text(
            text = dateTime,
            style = StocklifyTextStyles.subtitleXxs,
            color = StocklifyTheme.colors.typography100,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ImageSection() {
    Image(
        painter = painterResource(R.drawable.store_6_no_bg),
        contentDescription = null,
        modifier = Modifier.fillMaxWidth(),
    )
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
            .border(
                width = StocklifyStroke.s,
                color = StocklifyTheme.colors.transparentPrimary600,
                shape = RoundedCornerShape(StocklifyRadius.s),
            )
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
        SummaryRow(
            label = "Change",
            value = "₱ ${"%.2f".format(if (change < 0) 0.0 else change)}",
            labelStyle = StocklifyTextStyles.subtitleXs,
            valueStyle = StocklifyTextStyles.subtitleXs,
            valueColor = StocklifyTheme.colors.success,
        )
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    labelStyle: TextStyle,
    valueStyle: TextStyle,
    valueColor: Color? = null,
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
            color = valueColor ?: StocklifyTheme.colors.primary700,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SalesCompletedScreenPreview() {
    SalesCompletedScreen(
        total = 250.00,
        paymentReceived = 300.00,
        change = 50.00,
        onNewSales = {},
        onViewRecords = {},
    )
}
