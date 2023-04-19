package com.example.matrimony.db.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "success_stories")
data class SuccessStories(
    @PrimaryKey val id: Int,
    val image: Bitmap,
    val description: String
)
