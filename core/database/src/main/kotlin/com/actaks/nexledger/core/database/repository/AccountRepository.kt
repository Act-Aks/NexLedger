package com.actaks.nexledger.core.database.repository

import com.actaks.nexledger.core.model.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun getAll(): Flow<List<Account>>
    suspend fun getById(id: Long): Account?
    suspend fun getTotalBalance(): Double
    suspend fun create(account: Account): Long
    suspend fun update(account: Account)
    suspend fun delete(account: Account)
    suspend fun deleteById(id: Long)
}