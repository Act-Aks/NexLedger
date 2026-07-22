package com.actaks.nexledger.feature.dashboard.presentation

sealed interface DashboardAction {
    data object OnRefresh : DashboardAction
    data class OnTransactionClick(val transactionId: Long) : DashboardAction
    data object OnViewAllTransactions : DashboardAction
    data object OnViewAccounts : DashboardAction
    data object OnViewBudgets : DashboardAction
    data object OnAddTransaction : DashboardAction
    data object OnErrorDismissed : DashboardAction
}