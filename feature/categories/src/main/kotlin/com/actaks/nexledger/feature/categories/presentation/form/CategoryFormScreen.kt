package com.actaks.nexledger.feature.categories.presentation.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.actaks.nexledger.core.model.TransactionType
import com.actaks.nexledger.core.ui.util.ObserveAsEvents
import com.actaks.nexledger.core.ui.util.UiText
import com.actaks.nexledger.feature.categories.R
import org.koin.compose.viewmodel.koinViewModel

private val presetColors =
    listOf("#4ADE80", "#60A5FA", "#F472B6", "#FBBF24", "#A78BFA", "#FB923C", "#EF4444", "#34D399")

@Composable
fun CategoryFormRoot(
    onNavigateBack: () -> Unit,
    viewModel: CategoryFormViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.saved) { if (state.saved) onNavigateBack() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            CategoryFormEvent.NavigateBack -> onNavigateBack()
        }
    }

    CategoryFormScreen(
        state = state, onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryFormScreen(
    state: CategoryFormState,
    onAction: (CategoryFormAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    if (state.isEditing) UiText.StringResourceId(R.string.edit_category)
                        .asString()
                    else UiText.StringResourceId(R.string.add_category).asString()
                )
            }, navigationIcon = {
                IconButton(onClick = { onAction(CategoryFormAction.OnBackNavigationClick) }) {
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
                onValueChange = { onAction(CategoryFormAction.OnNameChange(it)) },
                label = { Text(UiText.StringResourceId(R.string.category_name).asString()) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Text(
                UiText.StringResourceId(R.string.type).asString(),
                style = MaterialTheme.typography.labelLarge
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TransactionType.entries.forEach { type ->
                    FilterChip(
                        selected = state.type == type,
                        onClick = { onAction(CategoryFormAction.OnTypeSelect(type)) },
                        label = { Text(type.name) })
                }
            }

            Text(
                UiText.StringResourceId(R.string.color).asString(),
                style = MaterialTheme.typography.labelLarge
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                presetColors.forEach { color ->
                    FilterChip(
                        selected = state.color == color,
                        onClick = { onAction(CategoryFormAction.OnColorSelect(color)) },
                        label = {
                            Box(Modifier.size(20.dp)) {
                                Surface(
                                    Modifier.fillMaxSize(),
                                    color = Color(color.toColorInt()),
                                    shape = MaterialTheme.shapes.small
                                ) {}
                            }
                        })
                }
            }

            if (state.error != null) Text(state.error, color = MaterialTheme.colorScheme.error)

            Button(
                onClick = { onAction(CategoryFormAction.OnSave) },
                Modifier.fillMaxWidth(),
                enabled = !state.saving
            ) {
                Icon(Icons.Rounded.Save, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    if (state.saving) UiText.StringResourceId(R.string.saving).asString()
                    else UiText.StringResourceId(R.string.save_category).asString()
                )
            }
        }
    }
}
