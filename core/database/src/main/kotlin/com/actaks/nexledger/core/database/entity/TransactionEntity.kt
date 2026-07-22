package com.actaks.nexledger.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.actaks.nexledger.core.model.TransactionType

/**
 * Room entity for transactions.
 *
 * Uses foreign keys to [CategoryEntity] and [AccountEntity] for referential integrity.
 * Indexed on [date] for fast chronological queries.
 */
@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["date"]),
        Index(value = ["categoryId"]),
        Index(value = ["accountId"])
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val type: TransactionType,
    val categoryId: Long,
    val accountId: Long,
    val date: Long,
    val note: String = "",
    val merchant: String = "",
    val paymentMethod: String = "",
    val createdAt: Long = System.currentTimeMillis()
)