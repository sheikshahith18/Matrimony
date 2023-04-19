package com.example.matrimony.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "profile_views",
    foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = arrayOf("userId"),
            childColumns = arrayOf("userId"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Account::class,
            parentColumns = arrayOf("userId"),
            childColumns = arrayOf("viewerUserId"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class ProfileView(
    @PrimaryKey val id: Int,
    val userId: String,
    val viewerUserId: String
)
