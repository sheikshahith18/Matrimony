package com.example.matrimony.db.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "success_stories")
data class SuccessStories(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val image: Bitmap,
    val couple_name:String,
    val description: String
)
