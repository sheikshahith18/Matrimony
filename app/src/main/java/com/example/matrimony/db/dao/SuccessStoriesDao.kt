package com.example.matrimony.db.dao

import android.graphics.Bitmap
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import com.example.matrimony.db.entities.SuccessStories

@Dao
interface SuccessStoriesDao {

//    @PrimaryKey(autoGenerate = true) val id: Int,
//    val image: Bitmap,
//    val couple_name:String,
//    val description: String


    @Query("SELECT * FROM success_stories")
    suspend fun getSuccessStories():List<SuccessStories>

    @Insert
    suspend fun addSuccessStory(successStories: SuccessStories)

}