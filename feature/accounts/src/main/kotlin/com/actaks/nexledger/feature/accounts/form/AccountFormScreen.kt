package com.actaks.nexledger.feature.accounts.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.actaks.nexledger.core.model.AccountType
import com.actaks.nexledger.core.ui.util.ObserveAsEvents
import com.actaks.nexledger.core.ui.util.UiText
import com.actaks.nexledger.feature.accounts.R
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AccountFormRoot(
    onNavigateBack: () -> Unit,
    viewModel: AccountFormViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.saved) { if (state.saved) onNavigateBack() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            AccountFormEvent.NavigateBack -> onNavigateBack()
        }
    }

    AccountFormScreen(
        state = state, onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountFormScreen(
    state: AccountFormState,
    onAction: (AccountFormAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    if (state.isEditing) UiText.StringResourceId(R.string.edit_account).asString()
                    else UiText.StringResourceId(R.string.add_account).asString()
                )
            }, navigationIcon = {
                IconButton(onClick = { onAction(AccountFormAction.OnBackNavigationClick) }) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        UiText.StringResourceId(R.string.back).asString()
                    )
                }
            })
        }) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = { onAction(AccountFormAction.OnNameChanged(it)) },
                label = { Text(UiText.StringResourceId(R.string.account_name).asString()) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Text(
                text = UiText.StringResourceId(R.string.account_type).asString(),
                style = MaterialTheme.typography.labelLarge
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AccountType.entries.forEach { type ->
                    FilterChip(
                        selected = state.type == type,
                        onClick = { onAction(AccountFormAction.OnTypeSelected(type)) },
                        label = { Text(type.displayName) })
                }
            }

            OutlinedTextField(
                value = state.balance,
                onValueChange = { onAction(AccountFormAction.OnBalanceChanged(it)) },
                label = { Text(UiText.StringResourceId(R.string.initial_balance).asString()) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            if (state.error != null) Text(state.error, color = MaterialTheme.colorScheme.error)

            Button(
                onClick = { onAction(AccountFormAction.OnSave) },
                Modifier.fillMaxWidth(),
                enabled = !state.saving
            ) {
                Icon(Icons.Rounded.Save, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    if (state.saving) UiText.StringResourceId(R.string.saving).asString()
                    else UiText.StringResourceId(R.string.save_account).asString()
                )
            }
        }
    }
}

private val AccountType.displayName: String
    get() = name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }