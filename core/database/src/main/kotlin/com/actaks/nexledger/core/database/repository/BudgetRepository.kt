package com.actaks.nexledger.core.database.repository

import com.actaks.nexledger.core.model.Budget
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    fun getAll(): Flow<List<Budget>>
    suspend fun getById(id: Long): Budget?
    suspend fun getByCategory(categoryId: Long): Budget?
    suspend fun create(budget: Budget): Long
    suspend fun update(budget: Budget)
    suspend fun delete(budget: Budget)
    suspend fun deleteById(id: Long)
}