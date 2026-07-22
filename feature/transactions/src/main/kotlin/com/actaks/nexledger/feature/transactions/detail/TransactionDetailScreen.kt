package com.actaks.nexledger.feature.transactions.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.actaks.nexledger.core.domain.formatCurrency
import com.actaks.nexledger.core.model.TransactionType
import com.actaks.nexledger.core.ui.util.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun TransactionDetailRoot(
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (id: Long) -> Unit,
    viewModel: TransactionDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            TransactionDetailEvent.Deleted -> onNavigateBack()
            TransactionDetailEvent.NavigateBack -> onNavigateBack()
            is TransactionDetailEvent.NavigateToEdit -> onNavigateToEdit(event.id)
        }
    }

    TransactionDetailScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionDetailScreen(
    state: TransactionDetailState,
    onAction: (TransactionDetailAction) -> Unit
) {
    if (state.error == "confirm_delete") {
        AlertDialog(
            onDismissRequest = { onAction(TransactionDetailAction.OnDeleteDismissed) },
            title = { Text("Delete Transaction") },
            text = { Text("This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = { onAction(TransactionDetailAction.OnDeleteConfirm) }) {
                    Text(
                        "Delete",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { onAction(TransactionDetailAction.OnDeleteDismissed) }) {
                    Text(
                        "Cancel"
                    )
                }
            })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transaction") },
                navigationIcon = {
                    IconButton(onClick = { onAction(TransactionDetailAction.OnBackNavigationClick) }) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onAction(TransactionDetailAction.OnDelete) }) {
                        Icon(
                            Icons.Rounded.Delete,
                            "Delete"
                        )
                    }
                    state.transaction?.let { transaction ->
                        IconButton(onClick = { onAction(TransactionDetailAction.OnEdit(transaction.id)) }) {
                            Icon(
                                Icons.Rounded.Edit,
                                "Edit"
                            )
                        }
                    }
                })
        }
    ) { innerPadding ->
        when {
            state.loading -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            state.transaction == null -> Column(
                Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
            ) { Text("Transaction not found") }

            else -> {
                val transaction = state.transaction
                Column(
                    Modifier
                        .padding(innerPadding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Amount
                    Text(
                        text = formatCurrency(transaction.amount),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (transaction.type == TransactionType.INCOME) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                    Text(text = transaction.type.name, style = MaterialTheme.typography.titleMedium)

                    HorizontalDivider()

                    DetailRow("Category", state.category?.name ?: "Unknown")
                    DetailRow("Account", state.account?.name ?: "Unknown")
                    DetailRow(
                        "Date",
                        SimpleDateFormat("MMM dd, yyyy", LocalLocale.current.platformLocale).format(
                            Date(transaction.date)
                        )
                    )
                    if (transaction.merchant.isNotBlank()) DetailRow(
                        "Merchant",
                        transaction.merchant
                    )
                    if (transaction.note.isNotBlank()) DetailRow("Note", transaction.note)
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}