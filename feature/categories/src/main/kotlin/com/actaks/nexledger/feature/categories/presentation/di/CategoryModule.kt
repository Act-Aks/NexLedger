package com.actaks.nexledger.feature.categories.presentation.di

import com.actaks.nexledger.feature.categories.presentation.CategoryViewModel
import com.actaks.nexledger.feature.categories.presentation.form.CategoryFormViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val categoryModule = module {
    viewModelOf(::CategoryViewModel)
    viewModelOf(::CategoryFormViewModel)
}