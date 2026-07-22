package com.actaks.nexledger.feature.security.di

import com.actaks.nexledger.feature.security.SecurityViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val securityModule = module {
    viewModel { SecurityViewModel(get()) }
}