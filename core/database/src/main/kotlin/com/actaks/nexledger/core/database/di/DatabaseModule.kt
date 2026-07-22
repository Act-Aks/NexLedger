package com.actaks.nexledger.core.database.di

import androidx.room.Room
import com.actaks.nexledger.core.database.NexLedgerDatabase
import com.actaks.nexledger.core.database.dao.AccountDao
import com.actaks.nexledger.core.database.dao.BudgetDao
import com.actaks.nexledger.core.database.dao.CategoryDao
import com.actaks.nexledger.core.database.dao.GoalDao
import com.actaks.nexledger.core.database.dao.RecurringTransactionDao
import com.actaks.nexledger.core.database.dao.TransactionDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room
            .databaseBuilder(
                androidContext(),
                NexLedgerDatabase::class.java,
                "nexledger.db"
            )
            .fallbackToDestructiveMigration(false)
            .build()
    }
    single<TransactionDao> { get<NexLedgerDatabase>().transactionDao() }
    single<AccountDao> { get<NexLedgerDatabase>().accountDao() }
    single<CategoryDao> { get<NexLedgerDatabase>().categoryDao() }
    single<BudgetDao> { get<NexLedgerDatabase>().budgetDao() }
    single<GoalDao> { get<NexLedgerDatabase>().goalDao() }
    single<RecurringTransactionDao> { get<NexLedgerDatabase>().recurringTransactionDao() }
}