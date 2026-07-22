package com.actaks.nexledger.feature.transactions

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
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.actaks.nexledger.core.model.TransactionType
import com.actaks.nexledger.core.ui.components.NexEmptyState
import com.actaks.nexledger.core.ui.components.NexErrorState
import com.actaks.nexledger.core.ui.components.NexTransactionRow
import com.actaks.nexledger.core.ui.util.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TransactionsRoot(
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToEditTransaction: (id: Long) -> Unit,
    onNavigateToTransactionDetail: (id: Long) -> Unit,
    viewModel: TransactionViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            TransactionEvent.NavigateToAddTransaction -> onNavigateToAddTransaction()
            is TransactionEvent.NavigateToEditTransaction -> onNavigateToEditTransaction(event.id)
            is TransactionEvent.NavigateToTransactionDetail -> onNavigateToTransactionDetail(event.id)
        }
    }

    TransactionsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionsScreen(
    state: TransactionState,
    onAction: (TransactionAction) -> Unit
) {
    var showSortMenu by remember { mutableStateOf(false) }

    // Filter bottom sheet
    if (state.showFilterSheet) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { onAction(TransactionAction.OnToggleFilterSheet) },
            sheetState = sheetState
        ) {
            FilterSheetContent(state = state, onAction = onAction)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transactions") },
                actions = {
                    IconButton(onClick = { showSortMenu = true }) {
                        Icon(Icons.AutoMirrored.Rounded.Sort, "Sort")
                    }
                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false }
                    ) {
                        SortOrder.entries.forEach { order ->
                            DropdownMenuItem(
                                text = { Text(order.displayName) },
                                onClick = {
                                    onAction(TransactionAction.OnSortSelect(order))
                                    showSortMenu = false
                                }
                            )
                        }
                    }
                    IconButton(onClick = { onAction(TransactionAction.OnToggleFilterSheet) }) {
                        Icon(Icons.Rounded.FilterList, "Filter")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAction(TransactionAction.OnAddTransactionClick) }) {
                Icon(Icons.Rounded.Add, "Add")
            }
        }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            // Search bar
            SearchBar(
                query = state.searchQuery,
                onQueryChange = { onAction(TransactionAction.OnSearchChange(it)) },
                onSearch = { },
                active = false,
                onActiveChange = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search transactions...") },
                leadingIcon = { },
                trailingIcon = {
                    if (state.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onAction(TransactionAction.OnSearchChange("")) }) {
                            Icon(Icons.Rounded.Clear, "Clear")
                        }
                    }
                }
            ) { }

            // Active filter chips
            if (state.filterType != null || state.filterCategoryId != null || state.filterAccountId != null) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.filterType?.let { type ->
                        FilterChip(
                            selected = true,
                            onClick = { onAction(TransactionAction.OnFilterTypeSelect(null)) },
                            label = { Text(type.name) }
                        )
                    }
                    TextButton(onClick = { onAction(TransactionAction.OnClearFilters) }) {
                        Text("Clear all")
                    }
                }
                Spacer(Modifier.height(4.dp))
            }

            // Content
            when {
                state.error != null -> NexErrorState(
                    message = state.error,
                    onRetry = { onAction(TransactionAction.OnRefresh) }
                )

                state.loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }

                state.transactions.isEmpty() -> NexEmptyState(
                    message = if (state.searchQuery.isNotEmpty() || state.filterType != null)
                        "No matching transactions" else "No transactions yet",
                    subtitle = "Tap + to add one"
                )

                else -> LazyColumn(
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(state.transactions, key = { it.id }) { transaction ->
                        NexTransactionRow(
                            transaction = transaction,
                            onClick = { onAction(TransactionAction.OnTransactionClick(transaction.id)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterSheetContent(
    state: TransactionState,
    onAction: (TransactionAction) -> Unit
) {
    Column(Modifier.padding(24.dp)) {
        Text("Filter by Type", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TransactionType.entries.forEach { type ->
                FilterChip(
                    selected = state.filterType == type,
                    onClick = {
                        onAction(
                            if (state.filterType == type) TransactionAction.OnFilterTypeSelect(null)
                            else TransactionAction.OnFilterTypeSelect(type)
                        )
                    },
                    label = { Text(type.name) }
                )
            }
        }
        Spacer(Modifier.height(16.dp))

        Text("Filter by Category", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            state.categories.take(6).forEach { category ->
                FilterChip(
                    selected = state.filterCategoryId == category.id,
                    onClick = {
                        onAction(
                            if (state.filterCategoryId == category.id) TransactionAction.OnFilterCategorySelect(
                                null
                            )
                            else TransactionAction.OnFilterCategorySelect(category.id)
                        )
                    },
                    label = { Text(category.name) }
                )
            }
        }
        Spacer(Modifier.height(16.dp))

        Text("Filter by Account", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            state.accounts.take(6).forEach { account ->
                FilterChip(
                    selected = state.filterAccountId == account.id,
                    onClick = {
                        onAction(
                            if (state.filterAccountId == account.id) TransactionAction.OnFilterAccountSelect(
                                null
                            )
                            else TransactionAction.OnFilterAccountSelect(account.id)
                        )
                    },
                    label = { Text(account.name) }
                )
            }
        }
        Spacer(Modifier.height(32.dp))
    }
}

private val SortOrder.displayName: String
    get() = when (this) {
        SortOrder.DATE_DESC -> "Newest first"
        SortOrder.DATE_ASC -> "Oldest first"
        SortOrder.AMOUNT_DESC -> "Highest amount"
        SortOrder.AMOUNT_ASC -> "Lowest amount"
    }