package com.actaks.nexledger.feature.goals.presentation.di

import com.actaks.nexledger.feature.goals.presentation.GoalViewModel
import com.actaks.nexledger.feature.goals.presentation.form.GoalFormViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val goalsModule = module {
    viewModel { GoalViewModel(get()) }
    viewModel { GoalFormViewModel(get(), get()) }
}