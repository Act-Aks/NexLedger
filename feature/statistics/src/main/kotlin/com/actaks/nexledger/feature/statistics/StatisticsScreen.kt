package com.actaks.nexledger.feature.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.actaks.nexledger.core.domain.formatCurrency
import com.actaks.nexledger.core.ui.components.NexErrorState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StatisticsRoot(
    viewModel: StatisticViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    StatisticsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatisticsScreen(
    state: StatisticState,
    onAction: (StatisticAction) -> Unit
) {
    Scaffold(topBar = { TopAppBar(title = { Text("Statistics") }) }) { innerPadding ->
        when {
            state.error != null -> NexErrorState(
                message = state.error,
                onRetry = { onAction(StatisticAction.OnRefresh) },
                modifier = Modifier.padding(innerPadding)
            )

            state.loading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            else -> LazyColumn(
                modifier = Modifier.padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                        Column(Modifier.padding(20.dp)) {
                            Text(
                                text = "This Month",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(Modifier.height(8.dp))
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Income",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        text = formatCurrency(state.totalIncome),
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Column {
                                    Text(
                                        text = "Expenses",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        text = formatCurrency(state.totalExpenses),
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                                Column {
                                    Text(
                                        text = "Net",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        text = formatCurrency(state.totalIncome - state.totalExpenses),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
                item {
                    Text(
                        text = "Spending by Category",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                if (state.categoryBreakdown.isEmpty()) {
                    item {
                        Text(
                            text = "No spending this month",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    items(state.categoryBreakdown) { cat ->
                        CategoryBreakdownRow(cat)
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryBreakdownRow(categorySpending: CategorySpending) {
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = categorySpending.categoryName, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = formatCurrency(categorySpending.amount),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { categorySpending.percentage },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Spacer(Modifier.height(2.dp))
        Text(
            "${(categorySpending.percentage * 100).toInt()}%",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}