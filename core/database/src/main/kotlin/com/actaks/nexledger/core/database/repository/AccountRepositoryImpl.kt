package com.actaks.nexledger.core.database.repository

import com.actaks.nexledger.core.database.dao.AccountDao
import com.actaks.nexledger.core.database.mapper.toDomain
import com.actaks.nexledger.core.database.mapper.toEntity
import com.actaks.nexledger.core.model.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AccountRepositoryImpl(
    private val dao: AccountDao
) : AccountRepository {
    override fun getAll(): Flow<List<Account>> =
        dao.getAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getById(id: Long): Account? = dao.getById(id)?.toDomain()
    override suspend fun getTotalBalance(): Double = dao.getTotalBalance() ?: 0.0
    override suspend fun create(account: Account): Long = dao.insert(account.toEntity())
    override suspend fun update(account: Account) = dao.update(account.toEntity())
    override suspend fun delete(account: Account) = dao.delete(account.toEntity())
    override suspend fun deleteById(id: Long) = dao.deleteById(id)
}