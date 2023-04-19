package com.example.matrimony.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "account")
data class Account(
    @PrimaryKey val userId: Int,
    val email: String,
    val mobileNo:String,
    val password:String
)