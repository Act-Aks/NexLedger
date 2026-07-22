package com.actaks.nexledger.core.database.di

import com.actaks.nexledger.core.database.repository.AccountRepository
import com.actaks.nexledger.core.database.repository.AccountRepositoryImpl
import com.actaks.nexledger.core.database.repository.BudgetRepository
import com.actaks.nexledger.core.database.repository.BudgetRepositoryImpl
import com.actaks.nexledger.core.database.repository.CategoryRepository
import com.actaks.nexledger.core.database.repository.CategoryRepositoryImpl
import com.actaks.nexledger.core.database.repository.GoalRepository
import com.actaks.nexledger.core.database.repository.GoalRepositoryImpl
import com.actaks.nexledger.core.database.repository.RecurringTransactionRepository
import com.actaks.nexledger.core.database.repository.RecurringTransactionRepositoryImpl
import com.actaks.nexledger.core.database.repository.TransactionRepository
import com.actaks.nexledger.core.database.repository.TransactionRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<AccountRepository> { AccountRepositoryImpl(get()) }
    single<BudgetRepository> { BudgetRepositoryImpl(get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get()) }
    single<GoalRepository> { GoalRepositoryImpl(get()) }
    single<RecurringTransactionRepository> { RecurringTransactionRepositoryImpl(get()) }
    single<TransactionRepository> { TransactionRepositoryImpl(get()) }
}