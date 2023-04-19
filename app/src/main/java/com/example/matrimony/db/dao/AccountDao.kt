package com.example.matrimony.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.matrimony.db.entities.Account


@Dao
interface AccountDao {
    @Insert
    suspend fun addAccount(account: Account)

    @Query("SELECT userId FROM account WHERE email= :email AND password = :password")
    suspend fun getUserUsingMail(email:String,password:String)
}