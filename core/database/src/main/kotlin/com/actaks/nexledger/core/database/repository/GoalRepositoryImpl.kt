package com.actaks.nexledger.core.database.repository

import com.actaks.nexledger.core.database.dao.GoalDao
import com.actaks.nexledger.core.database.mapper.toDomain
import com.actaks.nexledger.core.database.mapper.toEntity
import com.actaks.nexledger.core.model.Goal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GoalRepositoryImpl(
    private val dao: GoalDao
) : GoalRepository {

    override fun getAll(): Flow<List<Goal>> =
        dao.getAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getById(id: Long): Goal? = dao.getById(id)?.toDomain()
    override suspend fun create(goal: Goal): Long = dao.insert(goal.toEntity())
    override suspend fun update(goal: Goal) = dao.update(goal.toEntity())
    override suspend fun delete(goal: Goal) = dao.delete(goal.toEntity())
    override suspend fun deleteById(id: Long) = dao.deleteById(id)
}