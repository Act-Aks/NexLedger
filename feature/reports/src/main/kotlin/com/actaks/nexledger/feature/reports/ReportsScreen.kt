package com.actaks.nexledger.feature.reports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.actaks.nexledger.core.domain.formatCurrency
import com.actaks.nexledger.core.ui.components.NexErrorState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ReportsRoot(
    viewModel: ReportViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ReportsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReportsScreen(
    state: ReportState,
    onAction: (ReportAction) -> Unit
) {
    Scaffold(topBar = { TopAppBar(title = { Text("Reports") }) }) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ReportPeriod.entries.forEach { period ->
                    FilterChip(
                        selected = state.period == period,
                        onClick = { onAction(ReportAction.OnPeriodSelect(period)) },
                        label = { Text(period.name) })
                }
            }
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = { onAction(ReportAction.OnGenerate) },
                Modifier.fillMaxWidth()
            ) {
                Text("Generate Report")
            }
            if (state.error != null) NexErrorState(
                state.error,
                { onAction(ReportAction.OnGenerate) })
            if (state.loading) {
                Spacer(Modifier.height(16.dp)); CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            }
            if (!state.loading && state.totalIncome == 0.0 && state.totalExpenses == 0.0) {
                Spacer(Modifier.height(32.dp))
                Text(
                    "Select a period and tap Generate",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else if (!state.loading) {
                Spacer(Modifier.height(24.dp))
                ReportRow("Total Income", state.totalIncome, MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(12.dp))
                ReportRow("Total Expenses", state.totalExpenses, MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(Modifier.height(12.dp))
                ReportRow(
                    "Net Savings",
                    state.netSavings,
                    if (state.netSavings >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun ReportRow(label: String, amount: Double, color: Color) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.titleMedium)
        Text(
            formatCurrency(amount),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}