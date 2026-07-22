package com.actaks.nexledger.core.database.repository

import com.actaks.nexledger.core.model.Goal
import kotlinx.coroutines.flow.Flow

interface GoalRepository {
    fun getAll(): Flow<List<Goal>>
    suspend fun getById(id: Long): Goal?
    suspend fun create(goal: Goal): Long
    suspend fun update(goal: Goal)
    suspend fun delete(goal: Goal)
    suspend fun deleteById(id: Long)
}