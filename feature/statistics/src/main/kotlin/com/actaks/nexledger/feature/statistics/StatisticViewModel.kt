package com.actaks.nexledger.feature.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actaks.nexledger.core.database.repository.CategoryRepository
import com.actaks.nexledger.core.database.repository.TransactionRepository
import com.actaks.nexledger.core.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

class StatisticViewModel(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(StatisticState())
    val state: StateFlow<StatisticState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun onAction(action: StatisticAction) {
        when (action) {
            StatisticAction.OnRefresh -> refresh()
            StatisticAction.OnErrorDismissed -> _state.update { it.copy(error = null) }
        }
    }

    private fun refresh() {
        _state.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            combine(
                categoryRepository.getAll(),
                transactionRepository.getAll()
            ) { categories, transactions ->
                val now = Calendar.getInstance()
                val start = now.clone() as Calendar
                start.set(Calendar.DAY_OF_MONTH, 1)
                start.set(Calendar.HOUR_OF_DAY, 0)
                start.set(Calendar.MINUTE, 0)
                start.set(Calendar.SECOND, 0)

                val monthTxs = transactions.filter { it.date >= start.timeInMillis }
                val income =
                    monthTxs.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
                val expenses =
                    monthTxs.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }

                val expenseByCategory = monthTxs.filter { it.type == TransactionType.EXPENSE }
                    .groupBy { it.categoryId }.mapValues { e -> e.value.sumOf { it.amount } }
                val breakdown = expenseByCategory.map { (categoryId, amount) ->
                    val category = categories.find { it.id == categoryId }
                    CategorySpending(
                        categoryId = categoryId,
                        categoryName = category?.name ?: "Unknown",
                        amount = amount,
                        percentage = if (expenses > 0) (amount / expenses).toFloat() else 0f
                    )
                }.sortedByDescending { it.amount }

                _state.update {
                    it.copy(
                        totalIncome = income,
                        totalExpenses = expenses,
                        categoryBreakdown = breakdown,
                        categories = categories,
                        loading = false
                    )
                }
            }.catch { e ->
                _state.update { it.copy(loading = false, error = e.message) }
            }.collect {}
        }
    }
}
