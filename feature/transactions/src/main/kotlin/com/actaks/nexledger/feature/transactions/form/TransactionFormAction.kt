package com.actaks.nexledger.feature.transactions.form

import com.actaks.nexledger.core.model.TransactionType

sealed interface TransactionFormAction {
    data class OnAmountChange(val amount: String) : TransactionFormAction
    data class OnTypeSelect(val type: TransactionType) : TransactionFormAction
    data class OnCategorySelect(val categoryId: Long) : TransactionFormAction
    data class OnAccountSelect(val accountId: Long) : TransactionFormAction
    data class OnDateSelect(val date: Long) : TransactionFormAction
    data class OnNoteChange(val note: String) : TransactionFormAction
    data class OnMerchantChange(val merchant: String) : TransactionFormAction
    data object OnSave : TransactionFormAction
    data object OnErrorDismissed : TransactionFormAction
    data object OnBackNavigationClick : TransactionFormAction
}