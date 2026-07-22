package com.actaks.nexledger.feature.categories.presentation.form

sealed interface CategoryFormEvent {
    data object NavigateBack : CategoryFormEvent
}