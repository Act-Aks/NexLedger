package com.actaks.nexledger.feature.accounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.actaks.nexledger.core.designsystem.theme.NexLedgerTheme
import com.actaks.nexledger.core.domain.formatCurrency
import com.actaks.nexledger.core.model.Account
import com.actaks.nexledger.core.model.AccountType
import com.actaks.nexledger.core.ui.components.NexEmptyState
import com.actaks.nexledger.core.ui.components.NexErrorState
import com.actaks.nexledger.core.ui.util.ObserveAsEvents
import com.actaks.nexledger.core.ui.util.UiText
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AccountsRoot(
    onNavigateToAddAccount: () -> Unit,
    onNavigateToEditAccount: (accountId: Long) -> Unit,
    viewModel: AccountViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is AccountEvent.NavigateToAddAccount -> onNavigateToAddAccount()
            is AccountEvent.NavigateToEditAccount -> onNavigateToEditAccount(event.accountId)
        }
    }

    AccountsScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountsScreen(
    state: AccountState,
    onAction: (AccountAction) -> Unit,
) {
    // Delete confirmation dialog
    state.showDeleteDialog?.let {
        AlertDialog(
            onDismissRequest = { onAction(AccountAction.OnDeleteDismissed) },
            title = { Text(UiText.StringResourceId(R.string.delete_account).asString()) },
            text = {
                Text(
                    UiText.StringResourceId(R.string.delete_account_description).asString()
                )
            },
            confirmButton = {
                TextButton(onClick = { onAction(AccountAction.OnDeleteConfirmed) }) {
                    Text(
                        UiText.StringResourceId(R.string.delete).asString(),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { onAction(AccountAction.OnDeleteDismissed) }) {
                    Text(UiText.StringResourceId(R.string.cancel).asString())
                }
            })
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(UiText.StringResourceId(R.string.accounts).asString()) })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAction(AccountAction.OnAddAccountClick) }) {
                Icon(Icons.Rounded.Add, UiText.StringResourceId(R.string.add_account).asString())
            }
        }
    ) { innerPadding ->
        when {
            state.error != null -> NexErrorState(
                message = state.error,
                onRetry = { onAction(AccountAction.OnRefresh) },
                modifier = Modifier.padding(innerPadding)
            )

            state.loading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            state.accounts.isEmpty() -> NexEmptyState(
                message = UiText.StringResourceId(R.string.no_accounts_yet).asString(),
                subtitle = UiText.StringResourceId(R.string.empty_account_description).asString(),
                modifier = Modifier.padding(innerPadding)
            )

            else -> LazyColumn(
                modifier = Modifier.padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Total balance header
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ), modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(20.dp)) {
                            Text(
                                UiText.StringResourceId(R.string.total_balance).asString(),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                formatCurrency(state.totalBalance),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                items(state.accounts, key = { it.id }) { account ->
                    AccountCard(
                        account = account,
                        onClick = { onAction(AccountAction.OnAccountClick(account.id)) },
                        onDelete = { onAction(AccountAction.OnDeleteAccountClick(account.id)) })
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun AccountScreenPreview() {
    NexLedgerTheme {
        AccountsScreen(
            state = AccountState(
                accounts = listOf(Account(
                    name = "Akash",
                    type = AccountType.SAVINGS
                )),
                totalBalance = 0.0,
                loading = false,
            )
        ) {}
    }
}