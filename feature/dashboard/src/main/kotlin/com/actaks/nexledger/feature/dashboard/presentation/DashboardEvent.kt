package com.actaks.nexledger.feature.dashboard.presentation

sealed interface DashboardEvent {
    data object NavigateToTransactions : DashboardEvent
    data object NavigateToAccounts : DashboardEvent
    data object NavigateToBudgets : DashboardEvent
    data object NavigateToAddTransaction : DashboardEvent
    data class NavigateToTransactionDetail(val transactionId: Long) : DashboardEvent
}