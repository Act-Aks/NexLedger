package com.actaks.nexledger.feature.budgets.presentation

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.actaks.nexledger.core.model.Budget
import com.actaks.nexledger.core.ui.components.NexEmptyState
import com.actaks.nexledger.core.ui.components.NexErrorState
import com.actaks.nexledger.core.ui.util.ObserveAsEvents
import com.actaks.nexledger.core.ui.util.UiText
import com.actaks.nexledger.feature.budgets.R
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BudgetRoot(
    onNavigateToAddBudget: () -> Unit,
    onNavigateToEditBudget: (budgetId: Long) -> Unit,
    viewModel: BudgetViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is BudgetEvent.NavigateToAddBudget -> onNavigateToAddBudget()
            is BudgetEvent.NavigateToEditBudget -> onNavigateToEditBudget(event.budgetId)
        }
    }

    BudgetsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BudgetsScreen(
    state: BudgetState,
    onAction: (BudgetAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    UiText.StringResourceId(R.string.budgets).asString()
                )
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAction(BudgetAction.OnAddBudgetClick) }) {
                Icon(Icons.Rounded.Add, UiText.StringResourceId(R.string.add_budget).asString())
            }
        }
    ) { innerPadding ->
        when {
            state.error != null -> NexErrorState(
                state.error,
                { onAction(BudgetAction.OnRefresh) },
                Modifier.padding(innerPadding)
            )

            state.loading -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            state.budgets.isEmpty() -> NexEmptyState(
                message = UiText.StringResourceId(R.string.budget_empty_title).asString(),
                subtitle = UiText.StringResourceId(R.string.budget_empty_description).asString(),
                modifier = Modifier.padding(innerPadding)
            )

            else -> LazyColumn(
                modifier = Modifier.padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.budgets, key = { it.id }) { budget ->
                    val category = state.categories.find { it.id == budget.categoryId }
                    val spent = state.spendingByCategory[budget.categoryId] ?: 0.0
                    val progress =
                        if (budget.amount > 0) (spent / budget.amount).coerceIn(0.0, 1.0) else 0.0
                    BudgetCard(
                        budget = budget,
                        categoryName = category?.name ?: UiText.StringResourceId(R.string.category)
                            .asString(),
                        spent = spent,
                        progress = progress,
                        onClick = { onAction(BudgetAction.OnBudgetClick(budget.id)) })
                }
            }
        }
    }
}

@Composable
private fun BudgetCard(
    budget: Budget,
    categoryName: String,
    spent: Double,
    progress: Double,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = categoryName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${formatCurrency(spent)} / ${formatCurrency(budget.amount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (progress >= 1.0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = when {
                    progress >= 1.0 -> MaterialTheme.colorScheme.error
                    progress >= 0.75 -> MaterialTheme.colorScheme.tertiary
                    else -> MaterialTheme.colorScheme.primary
                },
                trackColor = MaterialTheme.colorScheme.surface
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "${(progress * 100).toInt()}${R.string.percent_used}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}