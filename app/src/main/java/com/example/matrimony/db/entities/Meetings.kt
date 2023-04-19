package com.example.matrimony.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date


@Entity(
    tableName = "meetings",
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
data class Meetings(
    @PrimaryKey val id: Int,
    val senderUserId: String,
    val receiverUserId: String,
    val title: String,
    val date: Date,
    val time: String,
    val place: String,
    val status: String
)
