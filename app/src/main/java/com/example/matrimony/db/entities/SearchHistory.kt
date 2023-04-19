package com.example.matrimony.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "search_history", foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = arrayOf("userId"),
        childColumns = arrayOf("userId"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class SearchHistory(
    @PrimaryKey val id: Int,
    val userId: String,
    val searchText: String
)
