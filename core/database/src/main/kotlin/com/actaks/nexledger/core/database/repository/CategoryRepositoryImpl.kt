package com.actaks.nexledger.core.database.repository

import com.actaks.nexledger.core.database.dao.CategoryDao
import com.actaks.nexledger.core.database.mapper.toDomain
import com.actaks.nexledger.core.database.mapper.toEntity
import com.actaks.nexledger.core.model.Category
import com.actaks.nexledger.core.model.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(
    private val dao: CategoryDao
) : CategoryRepository {

    override fun getAll(): Flow<List<Category>> =
        dao.getAll().map { entities -> entities.map { it.toDomain() } }

    override fun getByType(type: TransactionType): Flow<List<Category>> =
        dao.getByType(type).map { entities -> entities.map { it.toDomain() } }

    override suspend fun getById(id: Long): Category? = dao.getById(id)?.toDomain()
    override suspend fun create(category: Category): Long = dao.insert(category.toEntity())
    override suspend fun update(category: Category) = dao.update(category.toEntity())
    override suspend fun delete(category: Category) = dao.delete(category.toEntity())
    override suspend fun deleteById(id: Long) = dao.deleteById(id)
}