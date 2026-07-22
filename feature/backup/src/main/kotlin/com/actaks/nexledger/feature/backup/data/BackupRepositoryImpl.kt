package com.actaks.nexledger.feature.backup.data

import android.content.Context
import android.net.Uri
import com.actaks.nexledger.core.database.repository.AccountRepository
import com.actaks.nexledger.core.database.repository.CategoryRepository
import com.actaks.nexledger.core.database.repository.TransactionRepository
import com.actaks.nexledger.feature.backup.domain.BackupData
import com.actaks.nexledger.feature.backup.domain.toBackupAccount
import com.actaks.nexledger.feature.backup.domain.toBackupCategory
import com.actaks.nexledger.feature.backup.domain.toBackupTransaction
import com.actaks.nexledger.feature.backup.domain.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader

class BackupRepositoryImpl(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
    private val context: Context
) : BackupRepository {

    override suspend fun exportJson(uri: Uri): Int {
        return withContext(Dispatchers.IO) {
            val transactions = transactionRepository.getAll().first()
            val accounts = accountRepository.getAll().first()
            val categories = categoryRepository.getAll().first()
            val data = BackupData(
                transactions = transactions.map { it.toBackupTransaction() },
                accounts = accounts.map { it.toBackupAccount() },
                categories = categories.map { it.toBackupCategory() }
            )
            val json = Json { prettyPrint = true }.encodeToString(data)
            context.contentResolver.openOutputStream(uri)?.use { os ->
                os.write(json.toByteArray())
            }

            transactions.size
        }
    }

    override suspend fun exportCsv(uri: Uri): Int {
        return withContext(Dispatchers.IO) {
            val transactions = transactionRepository.getAll().first()
            val csv = buildString {
                appendLine("id,amount,type,categoryId,accountId,date,note,merchant")
                transactions.forEach { tx ->
                    appendLine("${tx.id},${tx.amount},${tx.type},${tx.categoryId},${tx.accountId},${tx.date},\"${tx.note}\",\"${tx.merchant}\"")
                }
            }
            context.contentResolver.openOutputStream(uri)?.use { os ->
                os.write(csv.toByteArray())
            }

            transactions.size
        }
    }

    override suspend fun importFrom(uri: Uri): BackupData {
        return withContext(Dispatchers.IO) {
            val inputStream = context.contentResolver.openInputStream(uri)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val json = reader.readText(); reader.close(); inputStream?.close()
            val data: BackupData = Json.decodeFromString(json)
            for (backupAccount in data.accounts) accountRepository.create(backupAccount.toDomain())
            for (backupCategory in data.categories) categoryRepository.create(backupCategory.toDomain())
            for (backupTransaction in data.transactions) transactionRepository.create(
                backupTransaction.toDomain()
            )

            data
        }
    }
}