package com.actaks.nexledger.feature.dashboard.presentation

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.actaks.nexledger.core.domain.formatCurrency
import com.actaks.nexledger.core.model.Budget
import com.actaks.nexledger.core.model.Category
import com.actaks.nexledger.core.ui.components.NexEmptyState
import com.actaks.nexledger.core.ui.components.NexErrorState
import com.actaks.nexledger.core.ui.components.NexSummaryRow
import com.actaks.nexledger.core.ui.components.NexTransactionRow
import com.actaks.nexledger.core.ui.util.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardRoot(
    onNavigateToTransactions: () -> Unit,
    onNavigateToAccounts: () -> Unit,
    onNavigateToBudgets: () -> Unit,
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToTransactionDetail: (categoryId: Long) -> Unit,
    viewModel: DashboardViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is DashboardEvent.NavigateToAccounts -> onNavigateToAccounts()
            is DashboardEvent.NavigateToAddTransaction -> onNavigateToAddTransaction()
            is DashboardEvent.NavigateToBudgets -> onNavigateToBudgets()
            is DashboardEvent.NavigateToTransactionDetail -> onNavigateToTransactionDetail(event.transactionId)
            is DashboardEvent.NavigateToTransactions -> onNavigateToTransactions()
        }
    }

    DashboardScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreen(
    state: DashboardState,
    onAction: (DashboardAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "NexLedger",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(DashboardAction.OnAddTransaction) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add transaction")
            }
        }
    ) { innerPadding ->
        when {
            state.error != null -> {
                NexErrorState(
                    message = state.error,
                    onRetry = { onAction(DashboardAction.OnRefresh) },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            state.loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            else -> {
                PullToRefreshBox(
                    isRefreshing = state.loading,
                    onRefresh = { onAction(DashboardAction.OnRefresh) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    if (state.recentTransactions.isEmpty() && state.accounts.isEmpty()) {
                        NexEmptyState(
                            message = "Welcome to NexLedger",
                            subtitle = "Add your first account or transaction to get started.",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        DashboardContent(
                            state = state,
                            onAction = onAction
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardContent(
    state: DashboardState,
    onAction: (DashboardAction) -> Unit
) {
    val currency = state.accounts.firstOrNull()?.currency ?: "INR"

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ── Summary cards ──
        item {
            NexSummaryRow(
                balance = state.totalBalance,
                income = state.incomeThisMonth,
                expenses = state.expensesThisMonth,
                currencyCode = currency
            )
        }

        // ── Budget progress ──
        if (state.budgets.isNotEmpty()) {
            item {
                Text(
                    text = "Budgets",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            item {
                BudgetProgressSection(
                    budgets = state.budgets,
                    categories = state.categories,
                    expenses = state.expensesThisMonth
                )
            }
        }

        // ── Recent transactions header ──
        item {
            Text(
                text = "Recent Transactions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (state.recentTransactions.isEmpty()) {
            item {
                Spacer(Modifier.height(32.dp))
                NexEmptyState(
                    message = "No transactions yet",
                    subtitle = "Tap + to add your first transaction"
                )
            }
        } else {
            items(state.recentTransactions, key = { it.id }) { transaction ->
                NexTransactionRow(
                    transaction = transaction,
                    onClick = { onAction(DashboardAction.OnTransactionClick(transaction.id)) }
                )
            }
        }

        // Bottom spacer for FAB
        item { Spacer(Modifier.height(72.dp)) }
    }
}

@Composable
private fun BudgetProgressSection(
    budgets: List<Budget>,
    categories: List<Category>,
    expenses: Double
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        budgets.take(3).forEach { budget ->
            val category = categories.find { it.id == budget.categoryId }
            val categoryName = category?.name ?: "Category"
            val progress =
                if (budget.amount > 0) (expenses / budget.amount).coerceIn(0.0, 1.0) else 0.0

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = categoryName,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "${formatCurrency(expenses)} / ${formatCurrency(budget.amount)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (progress >= 0.9) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { progress.toFloat() },
                    modifier = Modifier.fillMaxWidth(),
                    color = when {
                        progress >= 0.9 -> MaterialTheme.colorScheme.error
                        progress >= 0.7 -> MaterialTheme.colorScheme.tertiary
                        else -> MaterialTheme.colorScheme.primary
                    },
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
    }
}
