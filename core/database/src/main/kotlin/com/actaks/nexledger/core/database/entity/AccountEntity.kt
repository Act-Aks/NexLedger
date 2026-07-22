package com.actaks.nexledger.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.actaks.nexledger.core.model.AccountType

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: AccountType,
    val balance: Double = 0.0,
    val currency: String = "INR"
)