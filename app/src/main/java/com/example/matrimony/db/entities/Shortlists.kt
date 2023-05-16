package com.example.matrimony.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "shortlists",
    foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = arrayOf("user_id"),
            childColumns = arrayOf("user_id"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
    ]
)
data class Shortlists(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val user_id: Int,
    val shortlisted_user_id: Int,
)
