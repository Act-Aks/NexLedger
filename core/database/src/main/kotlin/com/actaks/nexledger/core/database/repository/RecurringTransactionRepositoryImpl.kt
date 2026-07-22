package com.actaks.nexledger.core.database.repository

import com.actaks.nexledger.core.database.dao.RecurringTransactionDao
import com.actaks.nexledger.core.database.mapper.toDomain
import com.actaks.nexledger.core.database.mapper.toEntity
import com.actaks.nexledger.core.model.RecurringTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecurringTransactionRepositoryImpl(
    private val dao: RecurringTransactionDao
) : RecurringTransactionRepository {

    override fun getAll(): Flow<List<RecurringTransaction>> =
        dao.getAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getById(id: Long): RecurringTransaction? = dao.getById(id)?.toDomain()
    override suspend fun create(recurring: RecurringTransaction): Long =
        dao.insert(recurring.toEntity())

    override suspend fun update(recurring: RecurringTransaction) = dao.update(recurring.toEntity())
    override suspend fun delete(recurring: RecurringTransaction) = dao.delete(recurring.toEntity())
    override suspend fun deleteById(id: Long) = dao.deleteById(id)
}