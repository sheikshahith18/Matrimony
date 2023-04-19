package com.example.matrimony.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "privacy_settings", foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = arrayOf("userId"),
        childColumns = arrayOf("userId"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class PrivacySettings(
    @PrimaryKey val id: Int,
    val userId: String,
    val viewProfilePic: String,
    val viewContactNum: String,
    val viewMyAlbum: String
)
