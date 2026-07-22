package com.actaks.nexledger.core.database.repository

import com.actaks.nexledger.core.model.Category
import com.actaks.nexledger.core.model.TransactionType
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAll(): Flow<List<Category>>
    fun getByType(type: TransactionType): Flow<List<Category>>
    suspend fun getById(id: Long): Category?
    suspend fun create(category: Category): Long
    suspend fun update(category: Category)
    suspend fun delete(category: Category)
    suspend fun deleteById(id: Long)
}