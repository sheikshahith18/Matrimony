package com.example.matrimony

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.matrimony.db.converters.*
import com.example.matrimony.db.entities.*


@Database(
    entities = [
        Account::class,
        Connections::class,
        EducationPreference::class,
        Hobbies::class,
        Meetings::class,
        MyAlbum::class,
        OccupationPreference::class,
        PartnerPreferences::class,
        PrivacySettings::class,
        ProfileView::class,
        SearchHistory::class,
        SuccessStories::class,
        User::class],
    version = 1
)
@TypeConverters(BitmapConverter::class, DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

}