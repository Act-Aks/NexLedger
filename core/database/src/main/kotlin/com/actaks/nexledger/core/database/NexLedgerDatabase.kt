package com.actaks.nexledger.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.actaks.nexledger.core.database.dao.AccountDao
import com.actaks.nexledger.core.database.dao.BudgetDao
import com.actaks.nexledger.core.database.dao.CategoryDao
import com.actaks.nexledger.core.database.dao.GoalDao
import com.actaks.nexledger.core.database.dao.RecurringTransactionDao
import com.actaks.nexledger.core.database.dao.TransactionDao
import com.actaks.nexledger.core.database.entity.AccountEntity
import com.actaks.nexledger.core.database.entity.BudgetEntity
import com.actaks.nexledger.core.database.entity.CategoryEntity
import com.actaks.nexledger.core.database.entity.GoalEntity
import com.actaks.nexledger.core.database.entity.RecurringTransactionEntity
import com.actaks.nexledger.core.database.entity.TransactionEntity

/**
 * Room database that holds all NexLedger financial data.
 *
 * Version 1 — initial schema.
 */
@Database(
    entities = [
        TransactionEntity::class,
        AccountEntity::class,
        CategoryEntity::class,
        BudgetEntity::class,
        GoalEntity::class,
        RecurringTransactionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NexLedgerDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun budgetDao(): BudgetDao
    abstract fun goalDao(): GoalDao
    abstract fun recurringTransactionDao(): RecurringTransactionDao
}