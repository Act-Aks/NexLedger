package com.actaks.nexledger.feature.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actaks.nexledger.core.database.repository.AccountRepository
import com.actaks.nexledger.core.database.repository.BudgetRepository
import com.actaks.nexledger.core.database.repository.CategoryRepository
import com.actaks.nexledger.core.database.repository.TransactionRepository
import com.actaks.nexledger.core.model.TransactionType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

class DashboardViewModel(
    private val accountRepository: AccountRepository,
    private val budgetRepository: BudgetRepository,
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    private val _events = Channel<DashboardEvent>()
    val events = _events.receiveAsFlow()

    init {
        refresh()
    }

    fun onAction(action: DashboardAction) {
        when (action) {
            DashboardAction.OnRefresh -> refresh()
            DashboardAction.OnErrorDismissed -> _state.update { it.copy(error = null) }
            is DashboardAction.OnTransactionClick -> navigateToTransactionDetail(action.transactionId)
            DashboardAction.OnViewAllTransactions -> navigateToTransactions()
            DashboardAction.OnViewAccounts -> navigateToAccounts()
            DashboardAction.OnViewBudgets -> navigateToBudgets()
            DashboardAction.OnAddTransaction -> navigateToAddTransaction()
        }
    }

    private fun refresh() {
        _state.update { it.copy(loading = true, error = null) }
        getAccountStatistics()
        getAllAccountDetails()
    }

    private fun getAccountStatistics() {
        viewModelScope.launch {
            val now = Calendar.getInstance()
            val startOfMonth = now.clone() as Calendar
            startOfMonth.set(Calendar.DAY_OF_MONTH, 1)
            startOfMonth.set(Calendar.HOUR_OF_DAY, 0)
            startOfMonth.set(Calendar.MINUTE, 0)
            startOfMonth.set(Calendar.SECOND, 0)
            startOfMonth.set(Calendar.MILLISECOND, 0)

            val endOfMonth = now.clone() as Calendar
            endOfMonth.set(
                Calendar.DAY_OF_MONTH, endOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
            )
            endOfMonth.set(Calendar.HOUR_OF_DAY, 23)
            endOfMonth.set(Calendar.MINUTE, 59)
            endOfMonth.set(Calendar.SECOND, 59)
            endOfMonth.set(Calendar.MILLISECOND, 999)

            val totalBalance = accountRepository.getTotalBalance()
            val monthlyIncome = transactionRepository.sumByTypeAndDateRange(
                TransactionType.INCOME, startOfMonth.timeInMillis, endOfMonth.timeInMillis
            )
            val monthlyExpense = transactionRepository.sumByTypeAndDateRange(
                TransactionType.EXPENSE, startOfMonth.timeInMillis, endOfMonth.timeInMillis
            )
            _state.update {
                it.copy(
                    totalBalance = totalBalance,
                    incomeThisMonth = monthlyIncome,
                    expensesThisMonth = monthlyExpense
                )
            }
        }
    }

    private fun getAllAccountDetails() {
        // Observe reactive streams
        viewModelScope.launch {
            combine(
                transactionRepository.getRecent(10),
                budgetRepository.getAll(),
                categoryRepository.getAll(),
                accountRepository.getAll()
            ) { transactions, budgets, categories, accounts ->
                _state.update {
                    it.copy(
                        recentTransactions = transactions,
                        budgets = budgets,
                        categories = categories,
                        accounts = accounts,
                        loading = false
                    )
                }
            }.catch { e ->
                _state.update {
                    it.copy(
                        loading = false, error = e.message ?: "Failed to load dashboard"
                    )
                }
            }.collect { }
        }
    }

    private fun navigateToBudgets() {
        viewModelScope.launch {
            _events.send(DashboardEvent.NavigateToBudgets)
        }
    }

    private fun navigateToTransactions() {
        viewModelScope.launch {
            _events.send(DashboardEvent.NavigateToTransactions)
        }
    }

    private fun navigateToAccounts() {
        viewModelScope.launch {
            _events.send(DashboardEvent.NavigateToAccounts)
        }
    }

    private fun navigateToAddTransaction() {
        viewModelScope.launch {
            _events.send(DashboardEvent.NavigateToAddTransaction)
        }
    }

    private fun navigateToTransactionDetail(transactionId: Long) {
        viewModelScope.launch {
            _events.send(DashboardEvent.NavigateToTransactionDetail(transactionId))
        }
    }
}