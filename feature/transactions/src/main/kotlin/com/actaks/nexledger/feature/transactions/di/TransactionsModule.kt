package com.actaks.nexledger.feature.transactions.di

import com.actaks.nexledger.feature.transactions.TransactionViewModel
import com.actaks.nexledger.feature.transactions.detail.TransactionDetailViewModel
import com.actaks.nexledger.feature.transactions.form.TransactionFormViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val transactionsModule = module {
    viewModel { TransactionViewModel(get(), get(), get()) }
    viewModel { TransactionDetailViewModel(get(), get(), get(), get()) }
    viewModel { TransactionFormViewModel(get(), get(), get(), get()) }
}