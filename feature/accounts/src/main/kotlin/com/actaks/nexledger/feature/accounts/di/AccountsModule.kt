package com.actaks.nexledger.feature.accounts.di

import com.actaks.nexledger.feature.accounts.AccountViewModel
import com.actaks.nexledger.feature.accounts.form.AccountFormViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val accountsModule = module {
    viewModelOf(::AccountViewModel)
    viewModelOf(::AccountFormViewModel)
}