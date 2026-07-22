package com.actaks.nexledger.core.database.repository

import com.actaks.nexledger.core.model.Transaction
import com.actaks.nexledger.core.model.TransactionType
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAll(): Flow<List<Transaction>>
    fun getByAccount(accountId: Long): Flow<List<Transaction>>
    fun getByCategory(categoryId: Long): Flow<List<Transaction>>
    fun getByType(type: TransactionType): Flow<List<Transaction>>
    fun getByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>>
    fun getRecent(limit: Int = 5): Flow<List<Transaction>>
    fun search(query: String): Flow<List<Transaction>>
    suspend fun getById(id: Long): Transaction?
    suspend fun sumByType(type: TransactionType): Double
    suspend fun sumByTypeAndDateRange(type: TransactionType, startDate: Long, endDate: Long): Double
    suspend fun create(transaction: Transaction): Long
    suspend fun update(transaction: Transaction)
    suspend fun delete(transaction: Transaction)
    suspend fun deleteById(id: Long)
}
