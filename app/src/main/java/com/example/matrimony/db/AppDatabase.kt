package com.example.matrimony.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.matrimony.db.converters.*
import com.example.matrimony.db.dao.*
import com.example.matrimony.db.entities.*


@Database(
    entities = [
        Account::class,
        Connections::class,
        FamilyDetails::class,
        Habits::class,
        Hobbies::class,
        Meetings::class,
        Album::class,
        PartnerPreferences::class,
        PrivacySettings::class,
        ProfileView::class,
        Shortlists::class,
        SuccessStories::class,
        User::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(BitmapConverter::class, DateConverter::class, ListConverter::class)
abstract class AppDatabase : RoomDatabase() {


    abstract fun accountDao(): AccountDao
    abstract fun connectionsDao(): ConnectionsDao
    abstract fun hobbiesDao(): HobbiesDao
    abstract fun meetingsDao(): MeetingsDao
    abstract fun albumDao(): AlbumDao
    abstract fun partnerPreferenceDao(): PartnerPreferenceDao
    abstract fun privacySettingsDao(): PrivacySettingsDao
    abstract fun shortlistsDao(): ShortlistsDao
    abstract fun successStoriesDao(): SuccessStoriesDao
    abstract fun userDao(): UserDao
    abstract fun habitsDao(): HabitsDao
    abstract fun familyDetailsDao(): FamilyDetailsDao


}