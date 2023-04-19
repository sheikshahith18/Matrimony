package com.example.matrimony.db.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "my_album", foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = arrayOf("userId"),
        childColumns = arrayOf("userId"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class MyAlbum(
    @PrimaryKey val image_id: Int,
    val userId: String,
    val image: Bitmap,
    val addedTime: String,
)
