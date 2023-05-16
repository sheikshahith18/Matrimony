package com.example.matrimony.db.repository

import androidx.lifecycle.LiveData
import com.example.matrimony.db.dao.AlbumDao
import com.example.matrimony.db.entities.Album

class AlbumRepository(private val albumDao: AlbumDao) {

    suspend fun getProfilePic(userId: Int):LiveData<Album?>{
        return albumDao.getProfilePic(userId)
    }

    suspend fun addAlbum(album: Album){
        albumDao.addAlbum(album)
    }


    suspend fun removePicture(userId: Int,imageId: Int){
        albumDao.removePicture(userId, imageId)
    }


    suspend fun getImage(userId:Int,imageId:Int): LiveData<Album>{
        return albumDao.getImage(userId, imageId)
    }

    suspend fun getUserAlbum(userId:Int): LiveData<List<Album?>?>{
        return albumDao.getUserAlbum(userId)
    }

    suspend fun getUserAlbumCount(userId: Int):LiveData<Int>{
        return albumDao.getUserAlbumCount(userId)
    }
}