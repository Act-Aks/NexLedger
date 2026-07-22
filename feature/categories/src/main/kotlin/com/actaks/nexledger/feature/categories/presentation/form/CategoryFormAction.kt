package com.actaks.nexledger.feature.categories.presentation.form

import com.actaks.nexledger.core.model.TransactionType

sealed interface CategoryFormAction {
    data class OnNameChange(val name: String) : CategoryFormAction
    data class OnTypeSelect(val type: TransactionType) : CategoryFormAction
    data class OnColorSelect(val color: String) : CategoryFormAction
    data object OnSave : CategoryFormAction
    data object OnErrorDismissed : CategoryFormAction
    data object OnBackNavigationClick : CategoryFormAction
}