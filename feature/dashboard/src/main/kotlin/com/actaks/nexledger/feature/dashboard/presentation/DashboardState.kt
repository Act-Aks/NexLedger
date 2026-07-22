package com.actaks.nexledger.feature.dashboard.presentation

import com.actaks.nexledger.core.model.Account
import com.actaks.nexledger.core.model.Budget
import com.actaks.nexledger.core.model.Category
import com.actaks.nexledger.core.model.Transaction

data class DashboardState(
    val totalBalance: Double = 0.0,
    val incomeThisMonth: Double = 0.0,
    val expensesThisMonth: Double = 0.0,
    val recentTransactions: List<Transaction> = emptyList(),
    val budgets: List<Budget> = emptyList(),
    val categories: List<Category> = emptyList(),
    val accounts: List<Account> = emptyList(),
    val loading: Boolean = true,
    val error: String? = null
)