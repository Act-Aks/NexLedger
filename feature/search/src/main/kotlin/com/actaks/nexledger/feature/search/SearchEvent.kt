package com.actaks.nexledger.feature.search

sealed interface SearchEvent {
    data class NavigateToTransactionDetail(val id: Long) : SearchEvent
}