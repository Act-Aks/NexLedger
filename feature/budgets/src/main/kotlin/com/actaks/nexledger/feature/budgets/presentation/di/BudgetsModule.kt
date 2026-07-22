package com.actaks.nexledger.feature.budgets.presentation.di

import com.actaks.nexledger.feature.budgets.presentation.BudgetViewModel
import com.actaks.nexledger.feature.budgets.presentation.form.BudgetFormViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val budgetsModule = module {
    viewModelOf(::BudgetViewModel)
    viewModelOf(::BudgetFormViewModel)
}