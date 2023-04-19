package com.example.matrimony.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "partner_preferences", foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = arrayOf("userId"),
        childColumns = arrayOf("userId"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class PartnerPreferences(
    @PrimaryKey val id: Int,
    val userId: String,
    val ageFrom: Int,
    val ageTo: Int,
    val heightFrom: Int,
    val heightTo: Int,
    val religion: String,
    val caste: String,    //
    val zodiac: String,   //
    val star: String,     //
    val education: String,
    val occupation: String,
    val annualIncome: String
)
