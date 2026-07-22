package com.actaks.nexledger.feature.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actaks.nexledger.core.database.repository.TransactionRepository
import com.actaks.nexledger.core.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

class ReportViewModel(
    private val transactionRepo: TransactionRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ReportState())
    val state: StateFlow<ReportState> = _state.asStateFlow()

    fun onAction(action: ReportAction) {
        when (action) {
            is ReportAction.OnPeriodSelect -> {
                _state.update { it.copy(period = action.period) }
                generate()
            }

            ReportAction.OnGenerate -> generate()
            ReportAction.OnErrorDismissed -> _state.update { it.copy(error = null) }
        }
    }

    private fun generate() {
        _state.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            try {
                val cal = Calendar.getInstance()
                val now = cal.timeInMillis
                cal.set(Calendar.DAY_OF_MONTH, 1); cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0); cal.set(
                    Calendar.SECOND,
                    0
                ); cal.set(Calendar.MILLISECOND, 0)
                if (_state.value.period == ReportPeriod.YEARLY) cal.set(Calendar.DAY_OF_YEAR, 1)
                val start = cal.timeInMillis

                val income =
                    transactionRepo.sumByTypeAndDateRange(TransactionType.INCOME, start, now)
                val expense =
                    transactionRepo.sumByTypeAndDateRange(TransactionType.EXPENSE, start, now)
                _state.update {
                    it.copy(
                        totalIncome = income,
                        totalExpenses = expense,
                        netSavings = income - expense,
                        loading = false
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(loading = false, error = e.message) }
            }
        }
    }
}
