package com.actaks.nexledger.feature.backup.domain

import com.actaks.nexledger.core.model.Account
import com.actaks.nexledger.core.model.AccountType
import com.actaks.nexledger.core.model.Category
import com.actaks.nexledger.core.model.Transaction
import com.actaks.nexledger.core.model.TransactionType

fun BackupTransaction.toDomain() = Transaction(
    amount = amount,
    type = TransactionType.valueOf(type),
    categoryId = categoryId,
    accountId = accountId,
    date = date,
    note = note,
    merchant = merchant
)

fun BackupAccount.toDomain() = Account(
    name = name,
    type = AccountType.valueOf(type),
    balance = balance,
    currency = currency
)

fun BackupCategory.toDomain() = Category(
    name = name,
    type = TransactionType.valueOf(type),
    icon = icon, color = color
)

fun Transaction.toBackupTransaction() = BackupTransaction(
    amount = amount,
    type = type.name,
    categoryId = categoryId,
    accountId = accountId,
    date = date,
    note = note,
    merchant = merchant
)

fun Account.toBackupAccount() = BackupAccount(
    name = name,
    type = type.name,
    balance = balance,
    currency = currency
)

fun Category.toBackupCategory() = BackupCategory(
    name = name,
    type = type.name,
    icon = icon,
    color = color
)