package com.actaks.nexledger.core.database.repository

import com.actaks.nexledger.core.database.dao.TransactionDao
import com.actaks.nexledger.core.database.mapper.toDomain
import com.actaks.nexledger.core.database.mapper.toEntity
import com.actaks.nexledger.core.model.Transaction
import com.actaks.nexledger.core.model.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepositoryImpl(
    private val dao: TransactionDao
) : TransactionRepository {

    override fun getAll(): Flow<List<Transaction>> =
        dao.getAll().map { entities -> entities.map { it.toDomain() } }

    override fun getByAccount(accountId: Long): Flow<List<Transaction>> =
        dao.getByAccount(accountId).map { entities -> entities.map { it.toDomain() } }

    override fun getByCategory(categoryId: Long): Flow<List<Transaction>> =
        dao.getByCategory(categoryId).map { entities -> entities.map { it.toDomain() } }

    override fun getByType(type: TransactionType): Flow<List<Transaction>> =
        dao.getByType(type).map { entities -> entities.map { it.toDomain() } }

    override fun getByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>> =
        dao.getByDateRange(startDate, endDate).map { entities -> entities.map { it.toDomain() } }

    override fun search(query: String): Flow<List<Transaction>> =
        dao.search(query).map { entities -> entities.map { it.toDomain() } }

    override fun getRecent(limit: Int): Flow<List<Transaction>> =
        dao.getRecent(limit).map { entities -> entities.map { it.toDomain() } }

    override suspend fun getById(id: Long): Transaction? = dao.getById(id)?.toDomain()
    override suspend fun sumByType(type: TransactionType): Double = dao.sumByType(type) ?: 0.0
    override suspend fun sumByTypeAndDateRange(
        type: TransactionType, startDate: Long, endDate: Long
    ): Double = dao.sumByTypeAndDateRange(type, startDate, endDate) ?: 0.0

    override suspend fun create(transaction: Transaction): Long = dao.insert(transaction.toEntity())
    override suspend fun update(transaction: Transaction) = dao.update(transaction.toEntity())
    override suspend fun delete(transaction: Transaction) = dao.delete(transaction.toEntity())
    override suspend fun deleteById(id: Long) = dao.deleteById(id)
}
