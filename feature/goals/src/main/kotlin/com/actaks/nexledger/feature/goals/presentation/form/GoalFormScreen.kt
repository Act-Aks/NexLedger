package com.actaks.nexledger.feature.goals.presentation.form

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.actaks.nexledger.core.ui.util.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@Composable
fun GoalFormRoot(
    onNavigateBack: () -> Unit,
    viewModel: GoalFormViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.saved) { if (state.saved) onNavigateBack() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            GoalFormEvent.NavigateBack -> onNavigateBack()
        }
    }

    GoalFormScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoalFormScreen(
    state: GoalFormState,
    onAction: (GoalFormAction) -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isEditing) "Edit Goal" else "Add Goal") },
                navigationIcon = {
                    IconButton(onClick = { onAction(GoalFormAction.OnBackNavigationClick) }) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = { onAction(GoalFormAction.OnNameChange(it)) },
                label = { Text("Goal Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = state.targetAmount,
                onValueChange = { onAction(GoalFormAction.OnTargetAmountChange(it)) },
                label = { Text("Target Amount") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = state.currentAmount,
                onValueChange = { onAction(GoalFormAction.OnCurrentAmountChange(it)) },
                label = { Text("Current Savings") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            val dateStr = if (state.deadline > 0) SimpleDateFormat(
                "MMM dd, yyyy",
                LocalLocale.current.platformLocale
            ).format(Date(state.deadline)) else "No deadline"

            OutlinedTextField(
                value = dateStr,
                onValueChange = {},
                readOnly = true,
                label = { Text("Deadline (optional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val cal = Calendar.getInstance()
                        if (state.deadline > 0) cal.timeInMillis = state.deadline
                        DatePickerDialog(
                            context,
                            { _, y, m, d ->
                                cal.set(y, m, d)
                                onAction(GoalFormAction.OnDeadlineSelect(cal.timeInMillis))
                            },
                            cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    },
                trailingIcon = { Icon(Icons.Rounded.CalendarToday, null) })

            if (state.error != null) Text(state.error, color = MaterialTheme.colorScheme.error)

            Button(
                onClick = { onAction(GoalFormAction.OnSave) },
                Modifier.fillMaxWidth(),
                enabled = !state.saving
            ) {
                Icon(Icons.Rounded.Save, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp))
                Text(if (state.saving) "Saving..." else "Save Goal")
            }
        }
    }
}