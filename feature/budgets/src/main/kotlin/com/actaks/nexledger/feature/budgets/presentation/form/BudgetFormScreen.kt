package com.actaks.nexledger.feature.budgets.presentation.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.actaks.nexledger.core.ui.util.ObserveAsEvents
import com.actaks.nexledger.core.ui.util.UiText
import com.actaks.nexledger.feature.budgets.R
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BudgetFormRoot(
    onNavigateBack: () -> Unit, viewModel: BudgetFormViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.saved) { if (state.saved) onNavigateBack() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            BudgetFormEvent.NavigateBack -> onNavigateBack()
        }
    }

    BudgetFormScreen(
        state = state, onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BudgetFormScreen(
    state: BudgetFormState, onAction: (BudgetFormAction) -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    if (state.isEditing) UiText.StringResourceId(R.string.edit_budget).asString()
                    else UiText.StringResourceId(R.string.add_budget).asString()
                )
            }, navigationIcon = {
                IconButton(onClick = { onAction(BudgetFormAction.OnBackNavigationClick) }) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        UiText.StringResourceId(R.string.back).asString()
                    )
                }
            })
        }) { innerPadding ->
        if (state.loading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) { CircularProgressIndicator() }
        } else {
            Column(
                Modifier
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                    val categoryName =
                        state.categories.find { it.id == state.categoryId }?.name ?: "Select"
                    OutlinedTextField(
                        value = categoryName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(UiText.StringResourceId(R.string.category).asString()) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) })
                    ExposedDropdownMenu(
                        expanded = expanded, onDismissRequest = { expanded = false }) {
                        state.categories.forEach { category ->
                            DropdownMenuItem(text = { Text(category.name) }, onClick = {
                                onAction(BudgetFormAction.OnCategorySelect(category.id))
                                expanded = false
                            })
                        }
                    }
                }
                OutlinedTextField(
                    value = state.amount,
                    onValueChange = { onAction(BudgetFormAction.OnAmountChange(it)) },
                    label = { Text(UiText.StringResourceId(R.string.monthly_limit).asString()) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                if (state.error != null) Text(state.error, color = MaterialTheme.colorScheme.error)

                Button(
                    onClick = { onAction(BudgetFormAction.OnSave) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.saving
                ) {
                    Icon(Icons.Rounded.Save, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        if (state.saving) UiText.StringResourceId(R.string.saving)
                            .asString()
                        else UiText.StringResourceId(R.string.save_budget)
                            .asString()
                    )
                }
            }
        }
    }
}
