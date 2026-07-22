package com.actaks.nexledger.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.actaks.nexledger.core.model.TransactionType

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: TransactionType,
    val icon: String = "",
    val color: String = ""
)