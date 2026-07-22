package com.actaks.nexledger.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingDown
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Summary row of three cards: balance, income, and expenses.
 */
@Composable
fun NexSummaryRow(
    balance: Double,
    income: Double,
    expenses: Double,
    currencyCode: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        NexSummaryCard(
            label = "Balance",
            value = balance,
            currencyCode = currencyCode,
            icon = Icons.Rounded.AccountBalance,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(8.dp))
        NexSummaryCard(
            label = "Income",
            value = income,
            currencyCode = currencyCode,
            icon = Icons.AutoMirrored.Rounded.TrendingUp,
            modifier = Modifier.weight(1f),
            valueColor = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.width(8.dp))
        NexSummaryCard(
            label = "Expenses",
            value = expenses,
            currencyCode = currencyCode,
            icon = Icons.AutoMirrored.Rounded.TrendingDown,
            modifier = Modifier.weight(1f),
            valueColor = MaterialTheme.colorScheme.error
        )
    }
}