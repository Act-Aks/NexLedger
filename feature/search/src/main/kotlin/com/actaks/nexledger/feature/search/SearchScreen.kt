package com.actaks.nexledger.feature.search

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
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
fun SearchRoot(
    onNavigateToTransactionDetail: (id: Long) -> Unit,
    viewModel: SearchViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is SearchEvent.NavigateToTransactionDetail -> onNavigateToTransactionDetail(event.id)
        }
    }

    SearchScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreen(
    state: SearchState,
    onAction: (SearchAction) -> Unit
) {
    Scaffold(topBar = { TopAppBar(title = { Text("Search") }) }) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            OutlinedTextField(
                value = state.query,
                onValueChange = { onAction(SearchAction.OnQueryChange(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text("Search by note, merchant...") },
                leadingIcon = { Icon(Icons.Rounded.Search, null) },
                trailingIcon = {
                    if (state.query.isNotEmpty()) IconButton(onClick = {
                        onAction(
                            SearchAction.OnQueryChange("")
                        )
                    }) { Icon(Icons.Rounded.Clear, "Clear") }
                },
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))
            Row(
                Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TransactionType.entries.forEach { type ->
                    FilterChip(
                        selected = state.filterType == type,
                        onClick = { onAction(SearchAction.OnFilterTypeSelect(if (state.filterType == type) null else type)) },
                        label = { Text(type.name) })
                }
            }

            if (state.error != null) NexErrorState(
                state.error,
                { onAction(SearchAction.OnClear) })
            else if (state.loading) Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
            else if (state.results.isNotEmpty()) {
                LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
                    items(state.results, key = { it.id }) { transaction ->
                        NexTransactionRow(
                            transaction,
                            onClick = { onAction(SearchAction.OnResultClick(transaction.id)) })
                    }
                }
            } else if (state.query.isNotEmpty() || state.filterType != null) {
                NexEmptyState(
                    message = "No results found",
                    subtitle = "Try a different search term"
                )
            } else {
                NexEmptyState(
                    message = "Search transactions",
                    subtitle = "Type a keyword or select a filter to begin"
                )
            }
        }
    }
}
