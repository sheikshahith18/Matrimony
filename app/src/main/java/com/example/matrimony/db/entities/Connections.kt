package com.example.matrimony.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "connections",
    foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = arrayOf("userId"),
            childColumns = arrayOf("senderUserId"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Account::class,
            parentColumns = arrayOf("userId"),
            childColumns = arrayOf("receiverUserId"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class Connections(
    @PrimaryKey val id: Int,
    val senderUserId: String,
    val receiverUserId: String,
    val status: String
)
