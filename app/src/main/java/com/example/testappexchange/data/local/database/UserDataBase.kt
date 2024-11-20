package com.example.testappexchange.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.testappexchange.data.local.models.UserAccountEntity
import com.example.testappexchange.data.local.models.UserProfileEntity


@Database(
    entities = [UserProfileEntity::class,
                UserAccountEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(CurrencyTypeConverter::class)
abstract class UserDataBase: RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun userAccountDao(): UserAccountDao
}