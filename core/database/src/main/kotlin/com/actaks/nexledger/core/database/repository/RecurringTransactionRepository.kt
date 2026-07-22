package com.actaks.nexledger.core.database.repository

import com.actaks.nexledger.core.model.RecurringTransaction
import kotlinx.coroutines.flow.Flow

interface RecurringTransactionRepository {
    fun getAll(): Flow<List<RecurringTransaction>>
    suspend fun getById(id: Long): RecurringTransaction?
    suspend fun create(recurring: RecurringTransaction): Long
    suspend fun update(recurring: RecurringTransaction)
    suspend fun delete(recurring: RecurringTransaction)
    suspend fun deleteById(id: Long)
}