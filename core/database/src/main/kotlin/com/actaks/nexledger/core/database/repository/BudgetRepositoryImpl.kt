package com.actaks.nexledger.core.database.repository

import com.actaks.nexledger.core.database.dao.BudgetDao
import com.actaks.nexledger.core.database.mapper.toDomain
import com.actaks.nexledger.core.database.mapper.toEntity
import com.actaks.nexledger.core.model.Budget
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BudgetRepositoryImpl(
    private val dao: BudgetDao
) : BudgetRepository {

    override fun getAll(): Flow<List<Budget>> =
        dao.getAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getById(id: Long): Budget? = dao.getById(id)?.toDomain()
    override suspend fun getByCategory(categoryId: Long): Budget? =
        dao.getByCategory(categoryId)?.toDomain()

    override suspend fun create(budget: Budget): Long = dao.insert(budget.toEntity())
    override suspend fun update(budget: Budget) = dao.update(budget.toEntity())
    override suspend fun delete(budget: Budget) = dao.delete(budget.toEntity())
    override suspend fun deleteById(id: Long) = dao.deleteById(id)
}