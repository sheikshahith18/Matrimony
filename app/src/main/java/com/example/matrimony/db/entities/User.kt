package com.example.matrimony.db.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date


@Entity(
    tableName = "user",
    foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = arrayOf("userId"),
        childColumns = arrayOf("userId"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class User(
    @PrimaryKey val userId: String,
    val name: String,
    val gender: String,
    val dob: Date,
    val religion: String,
    val motherTongue: String,
    val maritalStatus: String,
    val noOfChildren: Int?,
    val caste: String,
    val zodiac: String,
    val star: String,
    val country: String,
    val state: String,
    val city: String,
    val height: String,
    val profilePic: Bitmap?,
    val physicalStatus: String,
    val education: String,
    val employedIn: String,
    val occupation: String,
    val annualIncome: String,
    val familyStatus: String,
    val familyType: String,
    val about: String
)
