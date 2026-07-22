package com.actaks.nexledger.feature.statistics

sealed interface StatisticAction {
    data object OnRefresh : StatisticAction
    data object OnErrorDismissed : StatisticAction
}