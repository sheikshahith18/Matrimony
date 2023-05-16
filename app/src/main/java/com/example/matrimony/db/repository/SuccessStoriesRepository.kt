package com.example.matrimony.db.repository

import androidx.room.Insert
import androidx.room.Query
import com.example.matrimony.db.dao.SuccessStoriesDao
import com.example.matrimony.db.entities.SuccessStories

class SuccessStoriesRepository(private val successStoriesDao: SuccessStoriesDao) {


    suspend fun getSuccessStories():List<SuccessStories>{
        return successStoriesDao.getSuccessStories()
    }

    suspend fun addSuccessStory(successStories: SuccessStories){
        successStoriesDao.addSuccessStory(successStories)
    }
}