package com.actaks.nexledger.feature.categories.presentation.form

import com.actaks.nexledger.core.model.TransactionType

data class CategoryFormState(
    val name: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val color: String = "#4ADE80",
    val icon: String = "shopping_cart",
    val isEditing: Boolean = false,
    val saving: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null
)